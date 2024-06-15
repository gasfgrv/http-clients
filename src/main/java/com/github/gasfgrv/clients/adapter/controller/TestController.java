package com.github.gasfgrv.clients.adapter.controller;

import com.github.gasfgrv.clients.adapter.controller.model.LoginForm;
import com.github.gasfgrv.clients.domain.entity.User;
import com.github.gasfgrv.clients.domain.port.HttpClientPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/teste")
public class TestController {

    @Qualifier("restTemplateAdapter")
    private final HttpClientPort restTemplateAdapter;

    @Qualifier("openFeignAdapter")
    private final HttpClientPort openFeignAdapter;

    @Qualifier("okHttpAdapter")
    private final HttpClientPort okHttpAdapter;

    @Qualifier("restClientAdapter")
    private final HttpClientPort restClientAdapter;

    @Autowired
    public TestController(HttpClientPort restTemplateAdapter,
                          HttpClientPort openFeignAdapter,
                          HttpClientPort okHttpAdapter,
                          HttpClientPort restClientAdapter) {
        this.restTemplateAdapter = restTemplateAdapter;
        this.openFeignAdapter = openFeignAdapter;
        this.okHttpAdapter = okHttpAdapter;
        this.restClientAdapter = restClientAdapter;
    }

    @GetMapping("/restTemplate")
    public ResponseEntity<User> restTemplate() {
        LoginForm form = getLoginForm();
        return ResponseEntity.ok(callApi(restTemplateAdapter, form));
    }
    
    @GetMapping("/openFeign")
    public ResponseEntity<User> openFeign() {
        LoginForm form = getLoginForm();
        return ResponseEntity.ok(callApi(openFeignAdapter, form));
    }

    @GetMapping("/okHttp")
    public ResponseEntity<User> okHttp() {
        LoginForm form = getLoginForm();
        return ResponseEntity.ok(callApi(okHttpAdapter, form));
    }

    @GetMapping("/restClient")
    public ResponseEntity<User> restClient() {
        LoginForm form = getLoginForm();
        return ResponseEntity.ok(callApi(restClientAdapter, form));
    }

    @GetMapping("/all")
    public ResponseEntity<Set<String>> all() {
        LoginForm form = getLoginForm();

        Set<String> responses = new HashSet<>();
        Set.of(restTemplateAdapter, openFeignAdapter, okHttpAdapter, restClientAdapter).forEach(adapter -> {
            callApiAndCalculate(adapter, form, responses);
        });

        return ResponseEntity.ok(responses);
    }

    private void callApiAndCalculate(HttpClientPort adapter, LoginForm form, Set<String> responses) {
        long startTime = System.currentTimeMillis();
        callApi(adapter, form);
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        responses.add(String.format("[%s] executed in %d ms", adapter.getClass().getSimpleName(), executionTime));
    }


    private static LoginForm getLoginForm() {
        LoginForm form = new LoginForm();
        form.setUsername("emilys");
        form.setPassword("emilyspass");
        form.setExpiresInMins(30);
        return form;
    }

    private User callApi(HttpClientPort restTemplateAdapter, LoginForm form) {
        return restTemplateAdapter.getUserData(restTemplateAdapter.login(form));
    }

}
