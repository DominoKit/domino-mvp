package com.progressoft.brix.domino.api.server;

import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.PACKAGE)
public @interface ServerModule {

    @NotNull
    String name();
}
