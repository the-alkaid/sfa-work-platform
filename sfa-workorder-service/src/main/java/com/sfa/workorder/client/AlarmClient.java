package com.sfa.workorder.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("sfa-alarm-service")
public interface AlarmClient {

    @PostMapping("/api/alarm/status")
    Map<String, Object> updateStatus(
            @RequestParam("id") Long id,
            @RequestParam("status") Integer status,
            @RequestHeader("userId") String userId);
}
