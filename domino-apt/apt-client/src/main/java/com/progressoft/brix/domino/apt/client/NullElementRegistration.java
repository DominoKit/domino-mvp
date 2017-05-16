package com.progressoft.brix.domino.apt.client;

public class NullElementRegistration implements ElementRegistration {

    static final NullElementRegistration NULL_REGISTRATION = new NullElementRegistration();

    private NullElementRegistration() {
    }

    @Override
    public String registrationMethod() {
        return "";
    }

    @Override
    public String imports() {
        return "";
    }
}
