package org.dominokit.domino.apt.server.interceptors;

import com.google.auto.common.MoreElements;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.interceptor.Interceptor;
import org.dominokit.domino.api.server.interceptor.InterceptorsRegistry;
import org.dominokit.domino.api.server.interceptor.RequestInterceptor;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.Map;

public class InterceptorsRegisterMethodWriter extends AbstractRegisterMethodWriter<InterceptorsEntry, ProcessorElement> {
    public InterceptorsRegisterMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerInterceptors";
    }

    @Override
    protected Class<?> registryClass() {
        return InterceptorsRegistry.class;
    }

    @Override
    protected void registerItem(InterceptorsEntry entry, MethodSpec.Builder methodBuilder) {
        FullClassName entryPoint = new FullClassName(new FullClassName(entry.element.getInterfaceFullQualifiedGenericName(RequestInterceptor.class)).allImports().get(2));
        methodBuilder.addStatement("registry.registerInterceptor($T.class.getCanonicalName(), $T.class.getCanonicalName(), new $T())",
                ClassName.bestGuess(getHandler(entry)), ClassName.get(entryPoint.asPackage(), entryPoint.asSimpleGenericName()), ClassName.get(entry.element.elementPackage(), entry.element.simpleName()));
    }

    private String getHandler(InterceptorsEntry entry) {
        AnnotationMirror
                providerAnnotation = MoreElements.getAnnotationMirror(entry.element.asTypeElement(), Interceptor.class).get();
        DeclaredType providerInterface = this.getProviderInterface(providerAnnotation);
        TypeElement typeElement = asTypeElement(providerInterface);
        return typeElement.getQualifiedName().toString();

    }

    private TypeElement asTypeElement(DeclaredType p) {
        return (TypeElement) p.asElement();
    }

    private DeclaredType getProviderInterface(AnnotationMirror providerAnnotation) {
        Map valueIndex = providerAnnotation.getElementValues();
        AnnotationValue value = (AnnotationValue) valueIndex.values().iterator().next();
        return (DeclaredType) value.getValue();
    }

    @Override
    protected InterceptorsEntry parseEntry(ProcessorElement element) {
        return new InterceptorsEntry(element);
    }
}
