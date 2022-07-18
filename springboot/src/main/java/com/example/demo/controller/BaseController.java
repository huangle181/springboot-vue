package com.example.demo.controller;

import com.auth0.jwt.JWT;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
public class BaseController {

    @Resource
    private UserMapper userMapper;
    @Resource
    private HttpServletRequest request;

    /**
     * 根据token获取用户信息
     * @return user
     */

    public User getUser() {
        String token = request.getHeader("token");
        String aud = JWT.decode(token).getAudience().get(0);
        Integer userId = Integer.valueOf(aud);
        return userMapper.selectById(userId);
    }
}