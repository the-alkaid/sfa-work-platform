package com.sfa.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sfa.common.util.JwtUtil;
import com.sfa.user.entity.User;
import com.sfa.user.mapper.UserMapper;
import com.sfa.user.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public String login(String username, String password) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = this.getOne(wrapper);
        if(user == null){
            throw new RuntimeException("账号不存在");
        }
        if(!user.getPassword().equals(password)){
            throw new RuntimeException("密码错误");
        }
        // 直接调用静态方法，无需new对象
        return JwtUtil.generateToken(user.getId(), user.getUsername());
    }
}
