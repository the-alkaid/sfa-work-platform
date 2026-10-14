package com.sfa.alarm.controller;

import com.sfa.alarm.entity.AlarmRecord;
import com.sfa.alarm.service.AlarmService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alarm")
public class AlarmController {

    @Resource
    private AlarmService alarmService;

    /**
     * 创建告警
     */
    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody AlarmRecord alarm, HttpServletRequest request) {
        String userId = request.getHeader("userId");
        alarm.setCreatorId(Long.valueOf(userId));
        
        Long id = alarmService.createAlarm(alarm);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "创建成功");
        result.put("id", id);
        return result;
    }

    /**
     * 查询待处理告警
     */
    @GetMapping("/pending")
    public Map<String, Object> pending() {
        List<AlarmRecord> list = alarmService.listPending();
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    /**
     * 按级别查询告警
     */
    @GetMapping("/level/{level}")
    public Map<String, Object> listByLevel(@PathVariable Integer level) {
        List<AlarmRecord> list = alarmService.listByLevel(level);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", list);
        return result;
    }

    /**
     * 更新告警状态
     */
    @PostMapping("/status")
    public Map<String, Object> updateStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") Integer status,
            @RequestHeader("userId") String userId) {

        boolean success = alarmService.updateStatus(id, status, Long.valueOf(userId));
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "更新成功" : "更新失败");
        return result;
    }

    /**
     * 告警详情
     */
    @GetMapping("/{id}")
    public Map<String, Object> detail(@PathVariable Long id) {
        AlarmRecord alarm = alarmService.getById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", alarm);
        return result;
    }
}
