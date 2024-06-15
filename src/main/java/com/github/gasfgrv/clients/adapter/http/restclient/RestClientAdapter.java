package com.github.gasfgrv.clients.adapter.http.restclient;

import com.github.gasfgrv.clients.adapter.controller.model.LoginResponse;
import com.github.gasfgrv.clients.domain.entity.User;
import com.github.gasfgrv.clients.domain.port.HttpClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
@Component("restClientAdapter")
public class RestClientAdapter implements HttpClientPort {

    private final RestClient restClient;

    @Value("${api.url.login}")
    private String urlLogin;

    @Value("${api.url.user}")
    private String urlGetUser;

    @Override
    public String login(Serializable form) {
        try {
            ResponseEntity<LoginResponse> response = restClient
                    .post()
                    .uri(urlLogin)
                    .body(form)
                    .retrieve()
                    .toEntity(LoginResponse.class);
            return response.getBody().getToken();
        } catch (Exception e) {
            log.error("Erro ao realizar login - Resposta login: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserData(String token) {
        ResponseEntity<User> response = restClient
                .get()
                .uri(urlGetUser)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token))
                .retrieve()
                .toEntity(User.class);
        return response.getBody();
    }

}
