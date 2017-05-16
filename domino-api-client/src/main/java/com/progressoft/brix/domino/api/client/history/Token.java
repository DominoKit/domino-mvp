package com.progressoft.brix.domino.api.client.history;


public interface Token {

    String asTokenString();
    Token appendParameter(String name, String value);
    boolean hasSamePath(Token token);
}
