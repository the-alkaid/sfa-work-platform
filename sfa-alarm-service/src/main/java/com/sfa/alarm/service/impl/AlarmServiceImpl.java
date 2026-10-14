package com.sfa.alarm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sfa.alarm.client.WorkOrderClient;
import com.sfa.alarm.dto.WorkOrderDTO;
import com.sfa.alarm.entity.AlarmRecord;
import com.sfa.alarm.mapper.AlarmRecordMapper;
import com.sfa.alarm.service.AlarmService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlarmServiceImpl extends ServiceImpl<AlarmRecordMapper, AlarmRecord> implements AlarmService {

    @Resource
    private WorkOrderClient workOrderClient;

    @Override
    public Long createAlarm(AlarmRecord alarm) {
        alarm.setStatus(1); // 默认待处理
        this.save(alarm);

        // P1 级告警自动生成处置工单
        if (alarm.getLevel() == 1) {
            try {
                // 构造工单参数
                WorkOrderDTO workOrderParam = new WorkOrderDTO();
                workOrderParam.setTitle("【自动】" + alarm.getTitle());
                workOrderParam.setDescription("告警自动生成：" + alarm.getDescription());
                workOrderParam.setPriority(1);
                workOrderParam.setAlarmId(alarm.getId()); // 回填告警ID，双向关联

                // 调用工单服务，透传创建人 ID
                Map<String, Object> result = workOrderClient.create(
                        workOrderParam,
                        String.valueOf(alarm.getCreatorId())
                );
                
                // 回填工单 ID 到告警记录
                if (result != null && "200".equals(String.valueOf(result.get("code")))) {
                    Long workOrderId = Long.valueOf(String.valueOf(result.get("id")));
                    AlarmRecord update = new AlarmRecord();
                    update.setId(alarm.getId());
                    update.setWorkOrderId(workOrderId);
                    this.updateById(update);
                }
            } catch (Exception e) {
                // 工单创建失败不影响告警主流程
                e.printStackTrace();
            }
        }

        return alarm.getId();
    }

    @Override
    public boolean updateStatus(Long id, Integer status, Long handlerId) {
        AlarmRecord alarm = new AlarmRecord();
        alarm.setId(id);
        alarm.setStatus(status);
        alarm.setHandlerId(handlerId);
        return this.updateById(alarm);
    }

    @Override
    public List<AlarmRecord> listByLevel(Integer level) {
        LambdaQueryWrapper<AlarmRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlarmRecord::getLevel, level)
               .orderByDesc(AlarmRecord::getCreateTime);
        return this.list(wrapper);
    }

    @Override
    public List<AlarmRecord> listPending() {
        LambdaQueryWrapper<AlarmRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AlarmRecord::getStatus, 1)
               .orderByAsc(AlarmRecord::getLevel, AlarmRecord::getCreateTime);
        return this.list(wrapper);
    }
}
