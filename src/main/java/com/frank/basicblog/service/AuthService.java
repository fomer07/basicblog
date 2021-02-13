package com.frank.basicblog.service;


import com.frank.basicblog.dto.LoginRequest;
import com.frank.basicblog.dto.RegisterRequest;
import com.frank.basicblog.model.User;
import com.frank.basicblog.repository.UserRepository;
import com.frank.basicblog.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider jwt;



    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passEncoder(registerRequest.getPassword()));
        userRepository.save(user);
    }

    private String passEncoder(String password){
       return passwordEncoder.encode(password);
    }

    public String  login(LoginRequest loginRequest) {
        Authentication authenticate =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return jwt.generateToken(authenticate);
    }


    public org.springframework.security.core.userdetails.User getCurrentUser() {
       return (org.springframework.security.core.userdetails.User)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
