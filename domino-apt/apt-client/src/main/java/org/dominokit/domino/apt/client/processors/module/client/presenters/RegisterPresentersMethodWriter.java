package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.presenter.PresenterSupplier;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;

import javax.lang.model.element.Modifier;

public class RegisterPresentersMethodWriter extends AbstractRegisterMethodWriter<PresenterEntry, String> {

    public RegisterPresentersMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerPresenters";
    }

    @Override
    protected Class<?> registryClass() {
//        return PresenterRegistry.class;

        return null;
    }

    @Override
    protected void registerItem(PresenterEntry entry, MethodSpec.Builder methodBuilder) {
        MethodSpec makeMethod = MethodSpec.methodBuilder("make")
                .addModifiers(Modifier.PROTECTED)
                .addAnnotation(Override.class)
                .returns(Presentable.class)
                .addStatement("return new $T()", ClassName.bestGuess(entry.name))
                .build();
        TypeSpec lazyLoaderType = TypeSpec.anonymousClassBuilder("$T.class.getCanonicalName(), $T.class.getCanonicalName()"
                , ClassName.bestGuess(entry.name), ClassName.bestGuess(entry.name))
                .superclass(PresenterSupplier.class)
                .addMethod(makeMethod)
                .build();
        methodBuilder.addStatement("registry.registerPresenter($L)", lazyLoaderType);

        methodBuilder
                .addStatement("$T $L = new $T()");
    }

    /*

    SamplePresenterConfig samplePresenterConfig = new SamplePresenterConfig();
    samplePresenterConfig.setPresenterSupplier(new PresenterSupplier<SamplePresenter_Proxy>(true) {
      @Override
      protected SamplePresenter_Proxy make() {
        return new SamplePresenter_Proxy();
      }
    });

    samplePresenterConfig.setViewSupplier(PersonViewImpl::new);

     */

    @Override
    protected PresenterEntry parseEntry(String presenter) {
        return new PresenterEntry(presenter);
    }

}
