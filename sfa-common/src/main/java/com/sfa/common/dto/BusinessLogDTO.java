package com.sfa.common.dto;

import lombok.Data;

@Data
public class BusinessLogDTO {
    /**服务名称*/
    private String serviceName;
    /**日志级别 ERROR/WARN*/
    private String logLevel;
    /**日志内容*/
    private String logContent;
    /**异常堆栈信息*/
    private String stackTrace;
    private Long timestamp;
}
