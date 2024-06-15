package com.github.gasfgrv.clients.domain.port;

import com.github.gasfgrv.clients.domain.entity.User;

import java.io.Serializable;

public interface HttpClientPort {

    String login(Serializable form);

    User getUserData(String token);

}
