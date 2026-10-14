package com.sfa.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sfa.user.entity.User;

public interface UserService extends IService<User> {
    // 登录，成功返回token
    String login(String username, String password);
}
