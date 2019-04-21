package org.dominokit.domino.test.api.client.annotations;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestModules {
    @NotNull
    String[] value();
}
