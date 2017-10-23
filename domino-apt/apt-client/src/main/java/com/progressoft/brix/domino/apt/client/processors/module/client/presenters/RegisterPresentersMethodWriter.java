package com.progressoft.brix.domino.apt.client.processors.module.client.presenters;

import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.apt.client.processors.module.client.AbstractRegisterMethodWriter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

public class RegisterPresentersMethodWriter extends AbstractRegisterMethodWriter<PresenterEntry> {

    public RegisterPresentersMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerPresenters";
    }

    @Override
    protected Class<?> registryClass() {
        return PresenterRegistry.class;
    }

    @Override
    protected void registerItem(PresenterEntry entry, MethodSpec.Builder methodBuilder) {
        MethodSpec makeMethod = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(Presentable.class)
                .addStatement("return new $T()", ClassName.bestGuess(entry.impl))
                .build();
        TypeSpec lazyLoaderType = TypeSpec.anonymousClassBuilder("$T.class.getCanonicalName(), $T.class.getCanonicalName()"
                , ClassName.bestGuess(entry.name), ClassName.bestGuess(entry.impl))
                .superclass(LazyPresenterLoader.class)
                .addMethod(makeMethod)
                .build();
        methodBuilder.addStatement("registry.registerPresenter($L)", lazyLoaderType);
    }

    @Override
    protected PresenterEntry parseEntry(String presenter) {
        String[] presenterPair = presenter.split(":");
        return new PresenterEntry(presenterPair[0], presenterPair[1]);
    }

}
