package com.sfa.workorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sfa.workorder.client.AlarmClient;
import com.sfa.workorder.entity.WorkOrder;
import com.sfa.workorder.mapper.WorkOrderMapper;
import com.sfa.workorder.service.WorkOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkOrderServiceImpl extends ServiceImpl<WorkOrderMapper, WorkOrder> implements WorkOrderService {

    // 新增：注入告警服务 Feign 客户端
    @Resource
    private AlarmClient alarmClient;

    @Override
    public Long createWorkOrder(WorkOrder workOrder) {
        workOrder.setStatus(1); // 默认待处理
        this.save(workOrder);
        return workOrder.getId();
    }

    @Override
    public boolean assignWorkOrder(Long id, Long handlerId) {
        WorkOrder order = this.getById(id);
        if (order == null || order.getStatus() != 1) {
            return false; // 仅待处理状态可分派
        }
        order.setHandlerId(handlerId);
        order.setStatus(2); // 变为处理中
        return this.updateById(order);
    }

    @Override
    public boolean completeWorkOrder(Long id, Long handlerId) {
        WorkOrder order = this.getById(id);
        // 仅处理中状态、且操作人是当前处理人，才可提交完成
        if (order == null || order.getStatus() != 2 || !order.getHandlerId().equals(handlerId)) {
            return false;
        }
        order.setStatus(3); // 变为已完成
        boolean success = this.updateById(order);

        // 新增：工单完成后，同步更新关联告警状态为「已解决」
        if (success && order.getAlarmId() != null) {
            try {
                alarmClient.updateStatus(order.getAlarmId(), 3, String.valueOf(handlerId));
            } catch (Exception e) {
                // 告警同步失败不影响工单主流程
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public boolean closeWorkOrder(Long id, Long creatorId) {
        WorkOrder order = this.getById(id);
        // 仅已完成状态、且操作人是创建人，才可验收关闭
        if (order == null || order.getStatus() != 3 || !order.getCreatorId().equals(creatorId)) {
            return false;
        }
        order.setStatus(4); // 变为已关闭
        boolean success = this.updateById(order);

        // 新增：工单关闭后，同步更新关联告警状态为「已关闭」
        if (success && order.getAlarmId() != null) {
            try {
                alarmClient.updateStatus(order.getAlarmId(), 4, String.valueOf(creatorId));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }

    @Override
    public List<WorkOrder> listMyTodo(Long handlerId) {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkOrder::getHandlerId, handlerId)
               .in(WorkOrder::getStatus, 1, 2)
               .orderByDesc(WorkOrder::getPriority, WorkOrder::getCreateTime);
        return this.list(wrapper);
    }
}
