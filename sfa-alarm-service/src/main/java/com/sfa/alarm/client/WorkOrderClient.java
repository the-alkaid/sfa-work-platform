package com.sfa.alarm.client;

import com.sfa.alarm.dto.WorkOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Map;

@FeignClient("sfa-workorder-service")
public interface WorkOrderClient {

    @PostMapping("/api/workorder/create")
    Map<String, Object> create(@RequestBody WorkOrderDTO workOrder, @RequestHeader("userId") String userId);
}
