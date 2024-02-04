package com.wulaizhi.apigateway.enums;


import lombok.Data;

public enum AuthEnum {

    NONCE("NONCE","nonce"),
    SIGN("SIGN","sign"),
    BODY("BODY","body"),
    ACCESSKEY("ACCESSKEY","accesskey"),
    TIMESTAMP("TIMESTAMP","timestamp");
    private final String text;
    private final String value;
    AuthEnum(String text, String value) {
        this.text=text;
        this.value=value;
    }
    public String getText() {
        return text;
    }

    public String getValue() {
        return value;
    }
}
