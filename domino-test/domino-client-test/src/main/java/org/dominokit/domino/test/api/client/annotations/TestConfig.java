package org.dominokit.domino.test.api.client.annotations;

import org.dominokit.domino.api.client.ModuleConfiguration;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TestConfig {


    Class<? extends ModuleConfiguration>[] modules() default {};
}
