package com.progressoft.brix.domino.api.client.annotations;

import com.progressoft.brix.domino.api.client.history.RequestFromPath;
import com.progressoft.brix.domino.api.client.history.TokenizedPath;
import com.progressoft.brix.domino.api.client.request.Request;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Path {

    @NotNull
    String path();

    Class<? extends RequestFromPath> mapper() default DefaultPathToRequestMapper.class;

    final class DefaultPathToRequestMapper implements RequestFromPath {

        @Override
        public Request buildRequest(TokenizedPath path) {
            return null;
        }
    }
}
