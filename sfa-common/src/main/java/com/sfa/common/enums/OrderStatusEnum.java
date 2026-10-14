package com.sfa.common.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
    DRAFT(0,"草稿"),
    WAIT_APPROVE(1,"待审批"),
    PASS(2,"审批通过"),
    REJECT(3,"审批驳回"),
    CLOSED(4,"已关闭");

    private Integer code;
    private String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
