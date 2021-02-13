package com.frank.basicblog.dto;

import java.util.List;

public class JwtReponse {

    private String jwt;
    private String username;
    private Long id;
    private List<String> roles;



    public JwtReponse(String jwt, Long id, String username, List<String> roles) {
        this.jwt=jwt;
        this.username=username;
        this.id=id;
        this.roles=roles;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
