package com.ra.service.auth;

import com.ra.model.dto.request.UserLogin;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.entity.User;

public interface UserService {
    User register(User user);
    UserResponse login(UserLogin userLogin);
}
