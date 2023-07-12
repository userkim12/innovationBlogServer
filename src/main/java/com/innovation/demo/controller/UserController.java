package com.innovation.demo.controller;

import com.innovation.demo.dto.LoginRequestDto;
import com.innovation.demo.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto, HttpServletResponse res){
//        userService.login(loginRequestDto, res);
//
//        return ResponseEntity.ok().body("login ok!");
//    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid LoginRequestDto loginRequestDto){
        userService.signup(loginRequestDto);

        return ResponseEntity.ok().body("Singup Success");
    }

}
