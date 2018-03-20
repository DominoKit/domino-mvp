package org.dominokit.domino.api.client.annotations;


import org.dominokit.domino.api.client.request.ServerRequest;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestSender {

    @NotNull
    Class<? extends ServerRequest> value();

    boolean customServiceRoot() default false;
}
