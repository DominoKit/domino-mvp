package org.dominokit.domino.api.client.annotations;

//import javax.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.PACKAGE})
public @interface ClientModule {

//    @NotNull
    String name();
}
