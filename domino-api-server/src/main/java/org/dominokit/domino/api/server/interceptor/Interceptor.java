package org.dominokit.domino.api.server.interceptor;

import org.dominokit.domino.api.server.handler.RequestHandler;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interceptor {

    @NotNull
    Class<? extends RequestHandler> value();
}