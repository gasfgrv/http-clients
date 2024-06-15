package com.github.gasfgrv.clients.adapter.http.feign;

import com.github.gasfgrv.clients.adapter.controller.model.LoginResponse;
import com.github.gasfgrv.clients.domain.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.io.Serializable;

@FeignClient(name = "dummyFeign", url = "${api.baseUrl}")
public interface FeignClientAdapter {

    @GetMapping("/auth/me")
    User getUser(@RequestHeader("Authorization") String token);

    @PostMapping("/auth/login")
    LoginResponse login(Serializable login);

}
