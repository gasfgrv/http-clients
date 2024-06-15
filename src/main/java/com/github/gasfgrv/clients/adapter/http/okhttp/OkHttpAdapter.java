package com.github.gasfgrv.clients.adapter.http.okhttp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gasfgrv.clients.adapter.controller.model.LoginResponse;
import com.github.gasfgrv.clients.domain.entity.User;
import com.github.gasfgrv.clients.domain.port.HttpClientPort;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
@Component("okHttpAdapter")
public class OkHttpAdapter implements HttpClientPort {

    private final OkHttpClient okHttpClient;
    private final ObjectMapper mapper;

    @Value("${api.url.login}")
    private String urlLogin;

    @Value("${api.url.user}")
    private String urlGetUser;

    @Override
    public String login(Serializable form) {

        try {
            RequestBody formBody = RequestBody.create(MediaType.parse("application/json"), formToString(form));

            Request request = new Request.Builder()
                    .url(urlLogin)
                    .post(formBody)
                    .build();

            Response response = okHttpClient.newCall(request).execute();
            return StringForResponse(getResponseBody(response), LoginResponse.class).getToken();
        } catch (IOException e) {
            log.error("Erro ao realizar login - Resposta login: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserData(String token) {
        try {
            Request request = new Request.Builder()
                    .url(urlGetUser)
                    .header("Authorization", String.format("Bearer %s", token))
                    .get()
                    .build();

            Response response = okHttpClient.newCall(request).execute();

            return StringForResponse(getResponseBody(response), User.class);
        } catch (IOException e) {
            log.error("Erro ao obter dados do usuário: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String formToString(Serializable form) {
        try {
            return mapper.writeValueAsString(form);
        } catch (JsonProcessingException e) {
            log.error("Erro ao fazer o parse do resquest body: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private <T> T StringForResponse(String body, Class<T> responseType) {
        try {
            return mapper.readValue(body, responseType);
        } catch (JsonProcessingException e) {
            log.error("Erro ao converter resposta para objeto: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String getResponseBody(Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
            return responseBody.string();
        }
    }

}
