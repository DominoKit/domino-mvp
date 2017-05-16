package com.progressoft.brix.domino.api.client.history;

@FunctionalInterface
public interface ParameterConverter<P> {
    P convert(String value);
}
