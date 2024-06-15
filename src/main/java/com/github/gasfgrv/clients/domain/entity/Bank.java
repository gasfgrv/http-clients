package com.github.gasfgrv.clients.domain.entity;

import lombok.Data;

@Data
public class Bank {

    private String cardExpire;
    private String cardNumber;
    private String cardType;
    private String currency;
    private String iban;

}
