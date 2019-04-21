package org.dominokit.domino.test.api.client.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface StartServer {
    boolean value() default false;
}
