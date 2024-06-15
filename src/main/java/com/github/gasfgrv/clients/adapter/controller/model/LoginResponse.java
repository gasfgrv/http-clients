package com.github.gasfgrv.clients.adapter.controller.model;

import lombok.Data;

@Data
public class LoginResponse {

    private int id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String image;
    private String token;
    private String refreshToken;

}
