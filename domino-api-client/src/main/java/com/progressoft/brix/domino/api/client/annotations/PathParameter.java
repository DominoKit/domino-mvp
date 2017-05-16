package com.progressoft.brix.domino.api.client.annotations;

import com.progressoft.brix.domino.api.client.history.ParameterConverter;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface PathParameter {

    @NotNull
    String name() default "";

    @NotNull
    Class<? extends ParameterConverter> converter() default DefaultParameterConverter.class;

    final class DefaultParameterConverter implements ParameterConverter {
        @Override
        public Object convert(String value) {
            return value;
        }
    }
}
