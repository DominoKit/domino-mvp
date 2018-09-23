package org.dominokit.domino.apt.client.processors.module.client.views;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.client.mvp.ViewRegistry;
import org.dominokit.domino.api.client.mvp.view.PrototypeViewLoader;
import org.dominokit.domino.api.client.mvp.view.SingletonViewLoader;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

import javax.lang.model.element.Modifier;

public class RegisterViewsMethodWriter extends AbstractRegisterMethodWriter<ViewEntry, String> {

    public RegisterViewsMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerViews";
    }

    @Override
    protected Class<?> registryClass() {
        return ViewRegistry.class;
    }

    @Override
    protected void registerItem(ViewEntry entry, MethodSpec.Builder methodBuilder) {
        MethodSpec makeMethod = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(View.class)
                .addStatement("return new $T()", ClassName.bestGuess(entry.view))
                .build();
        TypeSpec lazyLoaderType = TypeSpec.anonymousClassBuilder("$T.class.getCanonicalName()",
                ClassName.bestGuess(entry.presenter))
                .superclass(entry.singleton ? SingletonViewLoader.class : PrototypeViewLoader.class)
                .addMethod(makeMethod)
                .build();
        methodBuilder.addStatement("registry.registerView($L)", lazyLoaderType);
    }

    @Override
    protected ViewEntry parseEntry(String presenter) {
        String[] viewPair = presenter.split(":");
        return new ViewEntry(viewPair[0], viewPair[1], Boolean.valueOf(viewPair[2]));
    }

}
