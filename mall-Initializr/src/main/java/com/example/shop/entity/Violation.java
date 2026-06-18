package com.example.shop.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Violation {
    private Long id;
    private Long reporterId;      // 举报人ID
    private String reporterName;   // 举报人名称
    private Long targetId;         // 被举报人ID
    private String targetName;     // 被举报人名称
    private Integer targetType;    // 1=用户, 2=商家
    private Integer type;          // 违规类型:1广告,2色情,3违规,4欺诈,5恶意评论,6辱骂,7其他
    private String content;        // 举报内容
    private String evidence;       // 证据图片
    private Integer status;        // 0待处理,1已警告,2已封禁,3已驳回
    private String handleResult;   // 处理结果
    private String handleRemark;   // 处理备注
    private Integer suspendDays;   // 封禁天数
    private String suspendReason;  // 封禁原因
    private LocalDateTime createTime;
    private LocalDateTime handleTime;
}