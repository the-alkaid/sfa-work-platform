package com.sfa.workorder.controller;

import com.sfa.workorder.entity.WorkOrder;
import com.sfa.workorder.service.WorkOrderService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workorder")
public class WorkOrderController {

    @Resource
    private WorkOrderService workOrderService;

    /**
     * 创建工单
     */
    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody WorkOrder workOrder, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        workOrder.setCreatorId(Long.valueOf(userId));
        
        Long id = workOrderService.createWorkOrder(workOrder);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "创建成功");
        result.put("id", id);
        return result;
    }

    /**
     * 分派工单
     */
    @PostMapping("/assign")
    public Map<String, Object> assign(@RequestParam Long id, @RequestParam Long handlerId) {
        boolean success = workOrderService.assignWorkOrder(id, handlerId);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "分派成功" : "分派失败：状态非法");
        return result;
    }

    /**
     * 提交完成工单（处理人操作）
     */
    @PostMapping("/complete")
    public Map<String, Object> complete(@RequestParam Long id, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        boolean success = workOrderService.completeWorkOrder(id, Long.valueOf(userId));
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "提交完成成功" : "提交失败：状态非法或无权限");
        return result;
    }

    /**
     * 验收关闭工单（创建人操作）
     */
    @PostMapping("/close")
    public Map<String, Object> close(@RequestParam Long id, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        boolean success = workOrderService.closeWorkOrder(id, Long.valueOf(userId));
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "验收关闭成功" : "关闭失败：状态非法或无权限");
        return result;
    }

    /**
     * 查询我的待办工单
     */
    @GetMapping("/todo")
    public Map<String, Object> todo(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        List<WorkOrder> list = workOrderService.listMyTodo(Long.valueOf(userId));
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    /**
     * 工单详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Long id) {
        WorkOrder order = workOrderService.getById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", order);
        return result;
    }
}
