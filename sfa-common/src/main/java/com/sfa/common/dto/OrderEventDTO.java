package com.sfa.common.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderEventDTO {
    /**工单id*/
    private Long orderId;
    /**工单编号*/
    private String orderNo;
    /**事件类型：APPROVE_PASS / APPROVE_REJECT*/
    private String eventType;
    /**操作人ID*/
    private Long operateUserId;
    private LocalDateTime eventTime;
}
