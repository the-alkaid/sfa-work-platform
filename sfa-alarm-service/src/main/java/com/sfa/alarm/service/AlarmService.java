package com.sfa.alarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sfa.alarm.entity.AlarmRecord;
import java.util.List;

public interface AlarmService extends IService<AlarmRecord> {
    // 创建告警
    Long createAlarm(AlarmRecord alarm);
    // 更新告警状态
    boolean updateStatus(Long id, Integer status, Long handlerId);
    // 按级别查询告警列表
    List<AlarmRecord> listByLevel(Integer level);
    // 查询待处理告警
    List<AlarmRecord> listPending();
}
