package com.ra.controller.auth;

import com.ra.model.dto.request.UserLogin;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.entity.User;
import com.ra.service.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user){
        User userNew = userService.register(user);
        if(userNew != null){
            return new ResponseEntity<>("register successful !",HttpStatus.CREATED);
        }
        return new ResponseEntity<>("phêu",HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin){
        UserResponse userResponse = userService.login(userLogin);
        return new ResponseEntity<>(userResponse,HttpStatus.OK);
    }
}
