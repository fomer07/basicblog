package com.frank.basicblog.controller;


import com.frank.basicblog.dto.JwtReponse;
import com.frank.basicblog.dto.LoginRequest;
import com.frank.basicblog.dto.RegisterRequest;
import com.frank.basicblog.model.ERole;
import com.frank.basicblog.model.Role;
import com.frank.basicblog.model.User;
import com.frank.basicblog.repository.RoleRepository;
import com.frank.basicblog.repository.UserRepository;
import com.frank.basicblog.security.JwtProvider;
import com.frank.basicblog.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;



    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody RegisterRequest registerRequest){

        User user = new User(registerRequest.getUsername(),
                             encoder.encode(registerRequest.getPassword()),
                             registerRequest.getEmail()
        );
        Set<String> strRoles = registerRequest.getRole();
        Set<Role> roleSet = new HashSet<>();

        if (strRoles==null){
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(()-> new UsernameNotFoundException("role not found"));
            roleSet.add(userRole);
        }else {

            strRoles.forEach(role-> {
           switch (role) {
               case  "admin" :
                   Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                           .orElseThrow(()->new UsernameNotFoundException("role not found"));
                   roleSet.add(adminRole);
                   break;
               case "mod":
                   Role mod = roleRepository.findByName(ERole.ROLE_MODERATOR)
                           .orElseThrow(()->new UsernameNotFoundException("role not found"));
                   roleSet.add(mod);
                   break;
               default:
                   Role userRol = roleRepository.findByName(ERole.ROLE_USER)
                           .orElseThrow(()->new UsernameNotFoundException("role not found"));
                   roleSet.add(userRol);
           }
            });

        }


        user.setRoleSet(roleSet);
        userRepository.save(user);

        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtReponse(
                                                    jwt,
                                                    userDetails.getId(),
                                                    userDetails.getUsername(),
                                                    roles
        ));


    }






}
