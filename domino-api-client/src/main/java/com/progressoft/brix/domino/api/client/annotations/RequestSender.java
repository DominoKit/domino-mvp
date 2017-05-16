package com.progressoft.brix.domino.api.client.annotations;


import com.progressoft.brix.domino.api.client.request.ClientServerRequest;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequestSender {

    @NotNull
    Class<? extends ClientServerRequest> value();
}
