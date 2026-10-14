package com.sfa.workorder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("work_order")
public class WorkOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Integer priority;
    private Integer status;
    private Long creatorId;
    private Long handlerId;
    private Long alarmId;
    private LocalDateTime slaExpireTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
