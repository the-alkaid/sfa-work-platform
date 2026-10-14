package com.sfa.alarm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("alarm_record")
public class AlarmRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private Integer level;
    private Integer status;
    private String source;
    private Long workOrderId;
    private Long creatorId;
    private Long handlerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
