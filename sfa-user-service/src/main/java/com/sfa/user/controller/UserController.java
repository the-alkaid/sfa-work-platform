package com.sfa.user.controller;

import com.sfa.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private UserService userService;

    // 新增hello测试接口
    @GetMapping("/hello")
    public Map<String,Object> hello(){
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        result.put("msg","接口连通成功");
        return result;
    }

    @PostMapping("/login")
    public Map<String,Object> login(@RequestParam String username, @RequestParam String password){
        String token = userService.login(username,password);
        Map<String,Object> result = new HashMap<>();
        result.put("code",200);
        result.put("msg","success");
        result.put("token",token);
        return result;
    }
}
