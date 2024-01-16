package com.ra.service.auth;

import com.ra.model.dto.request.UserLogin;
import com.ra.model.dto.response.UserResponse;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.security.jwt.JwtProvider;
import com.ra.security.user_principal.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;
    @Override
    public User register(User user) {
        // mã hóa mật khẩu
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        // tìm quyên theo tên để set mac dinh register sẽ có quyền là user
        Role role = roleRepository.findByName("ROLE_USER");
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public UserResponse login(UserLogin userLogin) {
        Authentication authentication;
        try {
            authentication = authenticationProvider.
                    authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUserName(),userLogin.getPassword()));
        } catch (AuthenticationException exception){
            throw new RuntimeException("username or password sai cmnr");
        }
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        // tạo ra 1 token
        String token = jwtProvider.generateToken(userPrincipal);
        // covernt sang doi tung UserResoine
        return UserResponse.builder().
                fullName(userPrincipal.getUser().getFullName())
                .id(userPrincipal.getUser().getId()).token(token).
                roles(userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())).
                build();
    }
}
