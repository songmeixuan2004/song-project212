package com.example.shop.controller;

import com.example.shop.common.Result;
import com.example.shop.entity.Cart;
import com.example.shop.entity.Logistics;
import com.example.shop.entity.LogisticsTrack;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.mapper.CartMapper;
import com.example.shop.mapper.LogisticsMapper;
import com.example.shop.mapper.OrderItemMapper;
import com.example.shop.mapper.OrderMapper;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private CartMapper cartMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderItemMapper orderItemMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private LogisticsMapper logisticsMapper;

    @PostMapping("/create")
    public Result<Order> create(@RequestBody Order order) {
        return Result.success(orderService.create(order));
    }

    @GetMapping("/list")
    public Result<List<Order>> list(@RequestParam Long userId) {
        return Result.success(orderService.list(userId));
    }

    @PostMapping("/pay")
    public Result<?> pay(@RequestParam Long orderId) {
        orderService.pay(orderId);
        return Result.success("支付成功");
    }
//创建订单money
    @PostMapping("/createFromCart")
    public Result<?> createFromCart(@RequestParam Long userId) {
        System.out.println("========== 开始创建订单 ==========");

        List<Cart> cartList = cartMapper.listByUserId(userId);
        if (cartList == null || cartList.isEmpty()) {
            return Result.error("购物车为空");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (Cart c : cartList) {
            Product product = productMapper.getById(c.getProductId());
            if (product == null) {
                return Result.error("商品 " + c.getProductId() + " 不存在");
            }

            if (product.getStock() < c.getQuantity()) {
                return Result.error("商品 " + product.getName() + " 库存不足！当前库存：" + product.getStock());
            }

            c.setProductName(product.getName());
            c.setProductPrice(product.getPrice());
            c.setProductPic(product.getPic());

            BigDecimal itemTotal = c.getProductPrice().multiply(new BigDecimal(c.getQuantity()));
            total = total.add(itemTotal);
        }

        System.out.println("订单总价: " + total);

        Order order = new Order();
        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());
        order.setOrderNo("ORDER" + System.currentTimeMillis());

        int insertResult = orderMapper.insert(order);
        System.out.println("订单插入结果: " + insertResult);
        System.out.println("生成的订单ID: " + order.getId());

        if (order.getId() == null) {
            return Result.error("订单创建失败");
        }

        int successCount = 0;
        for (Cart c : cartList) {
            Product product = productMapper.getById(c.getProductId());
            int newStock = product.getStock() - c.getQuantity();
            productMapper.updateStock(c.getProductId(), newStock);

            OrderItem item = new OrderItem();
            item.setOrderId(order.getId());
            item.setProductId(c.getProductId());
            item.setProductName(c.getProductName());
            item.setProductPrice(c.getProductPrice());
            item.setProductPic(c.getProductPic());
            item.setQuantity(c.getQuantity());
            // 重要：保存商家的ID
            item.setMerchantId(product.getMerchantId());
            System.out.println(">>> 商品ID=" + c.getProductId() + ", 商家ID=" + product.getMerchantId() + ", 设置成功");

            int insertItemResult = orderItemMapper.insert(item);
            if (insertItemResult > 0) {
                successCount++;
            }
        }

        System.out.println("成功插入 " + successCount + " 个订单商品");
        cartMapper.clearByUserId(userId);

        return Result.success(order);
    }

    @PostMapping("/finish")
    public Result<?> finish(@RequestParam Long orderId) {
        try {
            Order order = orderMapper.selectById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }
            if (order.getStatus() != 2) {
                return Result.error("只有已发货的订单才能确认收货");
            }

            order.setStatus(3);
            orderMapper.updateById(order);

            Logistics logistics = logisticsMapper.findByOrderId(orderId);
            if (logistics != null) {
                logistics.setStatus(4);
                logistics.setUpdateTime(LocalDateTime.now());
                logisticsMapper.update(logistics);

                LogisticsTrack track = new LogisticsTrack();
                track.setLogisticsId(logistics.getId());
                track.setStatus(4);
                track.setContent("用户已确认收货，订单完成");
                track.setTime(LocalDateTime.now());
                logisticsMapper.insertTrack(track);
            }

            return Result.success("确认收货成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/ship")
    public Result<?> ship(@RequestParam Long orderId){
        orderService.ship(orderId);
        // 记录发货时间
        Order order = orderMapper.selectById(orderId);
        order.setShipTime(LocalDateTime.now());
        orderMapper.updateById(order);
        return Result.success("发货成功");
    }

    // ✅ 修改：商家端查询自己的订单
    @GetMapping("/listAll")
    public Result<List<Order>> listAll(@RequestParam(required = false) Long merchantId){
        if (merchantId != null &&merchantId>0) {
            // 商家端：只查询包含自己商品的订单
            List<Order> merchantOrders = orderMapper.listOrdersByMerchantId(merchantId);
            return Result.success(merchantOrders);
        }
        // 管理员端：查询所有订单（可选）
        return Result.success(orderMapper.listAllOrders());
    }

    @GetMapping("/items")
    public Result<List<OrderItem>> getOrderItems(@RequestParam Long orderId) {
        if (orderId == null) {
            return Result.error("订单ID不能为空");
        }
        List<OrderItem> items = orderService.getOrderItems(orderId);
        System.out.println("订单 " + orderId + " 返回商品数量: " + (items == null ? 0 : items.size()));
        if (items != null) {
            for (OrderItem item : items) {
                System.out.println("  商品: " + item.getProductName() + ", 价格: " + item.getProductPrice());
            }
        }
        return Result.success(items);
    }

    @GetMapping("/debugCart")
    public Result<?> debugCart(@RequestParam Long userId) {
        List<Cart> cartList = cartMapper.listByUserId(userId);
        System.out.println("=== 调试购物车 ===");
        if (cartList == null || cartList.isEmpty()) {
            System.out.println("购物车为空");
            return Result.success("购物车为空");
        }
        for (Cart c : cartList) {
            System.out.println("商品ID: " + c.getProductId());
            System.out.println("商品名称: " + c.getProductName());
            System.out.println("商品价格: " + c.getProductPrice());
            System.out.println("数量: " + c.getQuantity());
            System.out.println("---");
        }
        return Result.success(cartList);
    }

    @DeleteMapping("/delete/{orderId}")
    public Result<?> deleteOrder(@PathVariable Long orderId) {
        try {
            if (orderId == null) {
                return Result.error("订单ID不能为空");
            }

            Order order = orderMapper.selectById(orderId);
            if (order == null) {
                return Result.error("订单不存在");
            }

            if (order.getStatus() == 1 || order.getStatus() == 2) {
                return Result.error("订单已发货，无法删除");
            }

            orderItemMapper.deleteByOrderId(orderId);
            int result = orderMapper.deleteById(orderId);

            if (result > 0) {
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("删除失败：" + e.getMessage());
        }
    }
}