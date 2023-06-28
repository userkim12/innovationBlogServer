package com.innovation.demo.controller;

import com.innovation.demo.dto.LoginRequestDto;
import com.innovation.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    public void login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse res){
        userService.login(loginRequestDto, res);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody LoginRequestDto loginRequestDto){
        userService.signup(loginRequestDto);
    }

}
