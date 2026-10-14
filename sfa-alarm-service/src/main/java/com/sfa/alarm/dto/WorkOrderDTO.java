package com.sfa.alarm.dto;

import lombok.Data;

@Data
public class WorkOrderDTO {
    private String title;
    private String description;
    private Integer priority;
    private Long alarmId;
}
