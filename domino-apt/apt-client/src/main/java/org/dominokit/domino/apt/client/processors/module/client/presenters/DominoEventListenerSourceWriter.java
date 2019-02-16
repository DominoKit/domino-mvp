package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.google.auto.common.MoreElements;
import com.squareup.javapoet.*;
import org.dominokit.domino.api.client.annotations.presenter.ListenTo;
import org.dominokit.domino.api.client.annotations.presenter.Listener;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import java.util.Map;

public class DominoEventListenerSourceWriter extends AbstractSourceBuilder {

    private final Element presenterElement;
    private final Element root;
    private final TypeElement eventType;

    public DominoEventListenerSourceWriter(Element presenterElement, Element listenMethod, ProcessingEnvironment processingEnvironment) {
        super(processingEnvironment);
        this.presenterElement = presenterElement;
        this.root = listenMethod;
        this.eventType = getEventType(root);
    }

    private String makeRequestClassName() {
        return elements.getPackageOf(presenterElement)
                .getQualifiedName().toString()
                + "."
                + presenterElement.getSimpleName().toString()
                + "Command";

    }

    @Override
    public TypeSpec.Builder asTypeBuilder() {

        String eventClassName = presenterElement.getSimpleName() + "ListenFor" + eventType.getSimpleName();
        TypeSpec.Builder listenerType = DominoTypeBuilder.build(eventClassName, PresenterProcessor.class)
                .addAnnotation(Listener.class)
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(DominoEventListener.class), TypeName.get(eventType.asType())))
                .addField(FieldSpec.builder(TypeName.get(presenterElement.asType()), "presenter", Modifier.PRIVATE, Modifier.FINAL).build())
                .addMethod(MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PUBLIC)
                        .addParameter(TypeName.get(presenterElement.asType()), "presenter")
                        .addStatement("this.presenter = presenter")
                        .build())
                .addMethod(makeListenMethod());
        return listenerType;
    }

    private TypeElement getEventType(Element e) {
        AnnotationMirror
                providerAnnotation = MoreElements.getAnnotationMirror(e, ListenTo.class).get();
        DeclaredType providerInterface = this.getProviderInterface(providerAnnotation);
        TypeElement typeElement = asTypeElement(providerInterface);
        return typeElement;

    }

    private DeclaredType getProviderInterface(AnnotationMirror providerAnnotation) {
        Map valueIndex = providerAnnotation.getElementValues();
        AnnotationValue value = (AnnotationValue) valueIndex.values().iterator().next();
        return (DeclaredType) value.getValue();
    }

    private TypeElement asTypeElement(DeclaredType p) {
        return (TypeElement) p.asElement();
    }

    private MethodSpec makeListenMethod() {
        return MethodSpec.methodBuilder("onEventReceived")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(eventType.asType()), "event")
                .addStatement("this.presenter.$L(event.context())", root.getSimpleName().toString())
                .build();
    }
}
