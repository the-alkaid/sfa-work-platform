package com.sfa.workorder.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sfa.workorder.entity.WorkOrder;
import java.util.List;

public interface WorkOrderService extends IService<WorkOrder> {
    // 创建工单
    Long createWorkOrder(WorkOrder workOrder);
    // 分派工单
    boolean assignWorkOrder(Long id, Long handlerId);
    // 完成工单（处理人操作）
    boolean completeWorkOrder(Long id, Long handlerId);
    // 关闭工单（创建人验收）
    boolean closeWorkOrder(Long id, Long creatorId);
    // 查询我的待办
    List<WorkOrder> listMyTodo(Long handlerId);
}
