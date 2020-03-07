package org.dominokit.domino.test.api.client.annotations;

import org.dominokit.domino.api.client.ModuleConfiguration;

//import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestConfig {

//    @NotNull
    Class<? extends ModuleConfiguration>[] modules() default {};
}
