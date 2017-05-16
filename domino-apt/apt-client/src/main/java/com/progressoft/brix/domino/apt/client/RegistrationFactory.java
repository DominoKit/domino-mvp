package com.progressoft.brix.domino.apt.client;

@FunctionalInterface
public interface RegistrationFactory {

    ElementRegistration registration();

    class InvalidDeclarationForAnnotationException extends RuntimeException{
        public InvalidDeclarationForAnnotationException(String message) {
            super(message);
        }
    }
}
