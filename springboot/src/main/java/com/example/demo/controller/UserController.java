package com.example.demo.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;//此处忽略了service层，直接调用mapper
    @PostMapping
    public Result<?> save(@RequestBody User user){
        userMapper.insert(user);

        return Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
       User res= userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()).eq(User::getPassword,user.getPassword()));
        if(res==null){
            return Result.error("-1","用户名或密码错误");
        }
        return Result.success(res);
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        User res= userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if(res==null){
            return Result.error("-1","用户名已存在");
        }
            int re=userMapper.insert(user);
            if(re==0){
                return Result.error("-1","用户名或密码错误");
            }
            return Result.success();

    }
    @PutMapping
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);

        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        userMapper.deleteById(id);

        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam (defaultValue = "")String search){
        Page<User> userPage =userMapper.selectPage(new Page<>(pageNum,pageSize), Wrappers.<User>lambdaQuery().like(User::getNickName,search));

        return Result.success(userPage);
    }

}
