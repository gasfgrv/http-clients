package com.github.gasfgrv.clients.adapter.controller.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginForm implements Serializable {

    private String username;
    private String password;
    private int expiresInMins;

}
