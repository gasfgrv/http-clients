package com.github.gasfgrv.clients.adapter.http.feign;

import com.github.gasfgrv.clients.adapter.controller.model.LoginResponse;
import com.github.gasfgrv.clients.domain.entity.User;
import com.github.gasfgrv.clients.domain.port.HttpClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
@Component("openFeignAdapter")
public class OpenFeignAdapter implements HttpClientPort {

    private final FeignClientAdapter clientAdapter;

    @Override
    public String login(Serializable form) {
        LoginResponse response = clientAdapter.login(form);
        log.info("Resposta login: {}", response);
        return response.getToken();
    }

    @Override
    public User getUserData(String token) {
        User user = clientAdapter.getUser(String.format("Bearer %s", token));
        log.info("Resposta getUser: {}", user);
        return user;
    }

}
