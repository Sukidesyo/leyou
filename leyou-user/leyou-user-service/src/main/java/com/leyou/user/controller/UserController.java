package com.leyou.user.controller;

import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkData(@PathVariable("data")String data,@PathVariable("type")Integer type)
    {
        Boolean aBoolean = userService.checkData(data, type);
        return ResponseEntity.ok(aBoolean);
    }
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code")String code) {
        userService.register(user, code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(@RequestParam("phone")String phone) {
        userService.sendVerifyCode(phone);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username")String username,@RequestParam("password")String password)
    {
        User user = userService.queryUser(username, password);
        System.out.println("conuser=="+user);
        if(user==null)
        {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(user);
    }
}
