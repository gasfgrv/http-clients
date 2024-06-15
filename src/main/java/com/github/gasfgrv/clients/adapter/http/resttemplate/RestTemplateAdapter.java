package com.github.gasfgrv.clients.adapter.http.resttemplate;

import com.github.gasfgrv.clients.adapter.controller.model.LoginForm;
import com.github.gasfgrv.clients.adapter.controller.model.LoginResponse;
import com.github.gasfgrv.clients.domain.entity.User;
import com.github.gasfgrv.clients.domain.port.HttpClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
@Component("restTemplateAdapter")
public class RestTemplateAdapter implements HttpClientPort {

    private final RestTemplate restTemplate;

    @Value("${api.url.login}")
    private String urlLogin;

    @Value("${api.url.user}")
    private String urlGetUser;

    @Override
    public String login(Serializable form) {
        HttpEntity<LoginForm> body = new HttpEntity<>((LoginForm) form);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(urlLogin, body, LoginResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Resposta login: {}", response.getBody());
            return response.getBody().getToken();
        }

        log.error("Erro ao realizar login - Resposta login: {}", response.getBody());
        throw new RuntimeException("Erro ao realizar login");
    }

    @Override
    public User getUserData(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));

        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);

        ResponseEntity<User> response = restTemplate.exchange(urlGetUser, HttpMethod.GET, httpEntity, User.class);

        log.info("Resposta getUser: {}", response.getBody());
        return response.getBody();
    }

}
