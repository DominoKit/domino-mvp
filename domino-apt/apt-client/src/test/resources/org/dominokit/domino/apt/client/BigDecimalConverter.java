package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.history.ParameterConverter;

import java.math.BigDecimal;

public class BigDecimalConverter implements ParameterConverter<BigDecimal> {

    @Override
    public BigDecimal convert(String value) {
        return new BigDecimal(value);
    }
}