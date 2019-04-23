package org.dominokit.domino.apt.client.processors.module.client.presenters;

import com.squareup.javapoet.*;
import org.dominokit.domino.api.client.annotations.Aggregate;
import org.dominokit.domino.api.client.annotations.presenter.*;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.DominoEventListener;
import org.dominokit.domino.api.shared.history.DominoHistory;
import org.dominokit.domino.apt.commons.AbstractSourceBuilder;
import org.dominokit.domino.apt.commons.DominoTypeBuilder;

import javax.annotation.PostConstruct;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.*;

import static java.util.Objects.nonNull;

public class PresenterProxySourceWriter extends AbstractSourceBuilder {

    private final Element proxyElement;

    protected PresenterProxySourceWriter(Element proxyElement, ProcessingEnvironment processingEnv) {
        super(processingEnv);
        this.proxyElement = proxyElement;
    }

    @Override
    public List<TypeSpec.Builder> asTypeBuilder() {
        String proxyClassName = proxyElement.getSimpleName() + "_Presenter";
        TypeSpec.Builder proxyType = DominoTypeBuilder.classBuilder(proxyClassName, PresenterProcessor.class)
                .addAnnotation(Presenter.class)
                .addModifiers(Modifier.PUBLIC)
                .superclass(TypeName.get(proxyElement.asType()));

        if (nonNull(proxyElement.getAnnotation(AutoRoute.class))) {
            AutoRoute autoRoute = proxyElement.getAnnotation(AutoRoute.class);
            proxyType.addAnnotation(AnnotationSpec.builder(AutoRoute.class)
                    .addMember("token", "$S", autoRoute.token())
                    .addMember("routeOnce", "$L", autoRoute.routeOnce())
                    .addMember("reRouteActivated", "$L", autoRoute.reRouteActivated())
                    .build());
        }

        if (nonNull(proxyElement.getAnnotation(AutoReveal.class))) {
            proxyType.addAnnotation(AnnotationSpec.builder(AutoReveal.class)
                    .build());
        }

        if (nonNull(proxyElement.getAnnotation(Slot.class))) {
            Slot slot = proxyElement.getAnnotation(Slot.class);
            proxyType.addAnnotation(AnnotationSpec.builder(Slot.class)
                    .addMember("value", "$S", slot.value())
                    .build());
        }

        if (nonNull(proxyElement.getAnnotation(Singleton.class))) {
            Singleton singleton = proxyElement.getAnnotation(Singleton.class);
            proxyType.addAnnotation(AnnotationSpec.builder(Singleton.class)
                    .addMember("value", "$L", singleton.value())
                    .build());
        }

        generateAutoReveal(proxyType);
        generateOnInit(proxyType);
        generateOnReveal(proxyType);
        generateOnRemove(proxyType);
        generateOnBeforeReveal(proxyType);
        generateOnPostConstruct(proxyType);
        generateListenersMethod(proxyType);
        generateSetState(proxyType);
        generateFireActivationEvent(proxyType);

        return Collections.singletonList(proxyType);
    }

    private void generateSetState(TypeSpec.Builder proxyType) {
        List<Element> routingState = processorUtil.getAnnotatedFields(proxyElement.asType(), RoutingState.class);
        List<Element> pathParameters = processorUtil.getAnnotatedFields(proxyElement.asType(), PathParameter.class);
        List<Element> fragmentParameters = processorUtil.getAnnotatedFields(proxyElement.asType(), FragmentParameter.class);
        List<Element> queryParameters = processorUtil.getAnnotatedFields(proxyElement.asType(), QueryParameter.class);

        if (!routingState.isEmpty() || !pathParameters.isEmpty() || !fragmentParameters.isEmpty() || !queryParameters.isEmpty()) {

            MethodSpec.Builder stateMethod = MethodSpec.methodBuilder("setState")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.VOID)
                    .addParameter(TypeName.get(DominoHistory.State.class), "state");

            routingState.forEach(element -> stateMethod.addStatement("this.$L = state", element.getSimpleName().toString()));

            pathParameters.forEach(element -> {
                PathParameter annotation = element.getAnnotation(PathParameter.class);
                String paramName = annotation.value().trim().isEmpty() ? element.getSimpleName().toString() : annotation.value();
                stateMethod.addStatement("this.$L = state.normalizedToken().getPathParameter($S)", element.getSimpleName().toString(), paramName);
            });

            fragmentParameters.forEach(element -> {
                FragmentParameter annotation = element.getAnnotation(FragmentParameter.class);
                String paramName = annotation.value().trim().isEmpty() ? element.getSimpleName().toString() : annotation.value();
                stateMethod.addStatement("this.$L = state.normalizedToken().getFragmentParameter($S)", element.getSimpleName().toString(), paramName);
            });

            queryParameters.forEach(element -> {
                QueryParameter annotation = element.getAnnotation(QueryParameter.class);
                String paramName = annotation.value().trim().isEmpty() ? element.getSimpleName().toString() : annotation.value();
                stateMethod.addStatement("this.$L = state.token().getQueryParameter($S)", element.getSimpleName().toString(), paramName);
            });

            proxyType.addMethod(stateMethod.build());
        }
    }

    private void generateAutoReveal(TypeSpec.Builder proxyType) {
        Slot slot = proxyElement.getAnnotation(Slot.class);
        if (nonNull(slot) && !slot.value().trim().isEmpty()) {
            proxyType.addMethod(MethodSpec.methodBuilder("revealSlot")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return \"$L\"", slot.value())
                    .build());
        }
    }

    private void generateOnInit(TypeSpec.Builder proxyType) {

        List<Element> initMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), OnInit.class);

        CodeBlock.Builder methodsCall = CodeBlock.builder();
        boolean generateMethod = false;

        if (nonNull(initMethods) && !initMethods.isEmpty()) {
            generateMethod = true;
            initMethods.forEach(element -> methodsCall.addStatement(element.getSimpleName() + "()"));
        }

        if (generateMethod) {
            proxyType.addMethod(MethodSpec.methodBuilder("onActivated")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(TypeName.VOID)
                    .addCode(methodsCall.build())
                    .build());
        }

    }

    private void generateOnReveal(TypeSpec.Builder proxyType) {

        List<Element> revealMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), OnReveal.class);
        if (nonNull(revealMethods) && !revealMethods.isEmpty()) {
            CodeBlock.Builder methodsCall = generateMethodsCall(revealMethods);

            proxyType.addMethod(MethodSpec.methodBuilder("getRevealHandler")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ViewBaseClientPresenter.RevealedHandler.class)
                    .addCode(methodsCall.build())
                    .build());
        }
    }

    private void generateOnBeforeReveal(TypeSpec.Builder proxyType) {
        List<Element> onBeforeRevealMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), OnBeforeReveal.class);
        if (nonNull(onBeforeRevealMethods) && !onBeforeRevealMethods.isEmpty()) {
            CodeBlock.Builder methodsCall = CodeBlock.builder();
            onBeforeRevealMethods.forEach(element -> methodsCall.addStatement(element.getSimpleName().toString() + "()"));

            proxyType.addMethod(MethodSpec.methodBuilder("onBeforeReveal")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(TypeName.VOID)
                    .addCode(methodsCall.build())
                    .build());
        }
    }

    private void generateOnPostConstruct(TypeSpec.Builder proxyType) {
        List<Element> aggregateMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), Aggregate.class);

        aggregateMethods.forEach(element -> {
            String aggregateName = element.getAnnotation(Aggregate.class).name();
            proxyType.addMethod(MethodSpec.methodBuilder(processorUtil.lowerFirstLetter(aggregateName) + "_init")
                    .addModifiers(Modifier.PRIVATE)
                    .returns(TypeName.VOID)
                    .addStatement("$L = new $T().init(this)", processorUtil.lowerFirstLetter(aggregateName), ClassName.get(elements.getPackageOf(element.getEnclosingElement()).toString(), aggregateName))
                    .build());
        });

        CodeBlock.Builder methodsCall = CodeBlock.builder();
        boolean generateMethod = false;

        if (nonNull(aggregateMethods) && !aggregateMethods.isEmpty()) {
            generateMethod = true;
            aggregateMethods.forEach(element -> {
                String aggregateName = element.getAnnotation(Aggregate.class).name();
                methodsCall.addStatement(processorUtil.lowerFirstLetter(aggregateName) + "_init()");
            });
        }


        List<Element> onPostConstructMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), PostConstruct.class);
        if (nonNull(onPostConstructMethods) && !onPostConstructMethods.isEmpty()) {
            generateMethod = true;
            onPostConstructMethods.forEach(element -> methodsCall.addStatement(element.getSimpleName().toString() + "()"));
        }
        if (generateMethod) {
            proxyType.addMethod(MethodSpec.methodBuilder("postConstruct")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PROTECTED)
                    .returns(TypeName.VOID)
                    .addCode(methodsCall.build())
                    .build());
        }
    }

    private void generateOnRemove(TypeSpec.Builder proxyType) {

        List<Element> removeMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), OnRemove.class);
        if (nonNull(removeMethods) && !removeMethods.isEmpty()) {
            CodeBlock.Builder methodsCall = generateMethodsCall(removeMethods);

            proxyType.addMethod(MethodSpec.methodBuilder("getRemoveHandler")
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ViewBaseClientPresenter.RemovedHandler.class)
                    .addCode(methodsCall.build())
                    .build());
        }
    }

    private CodeBlock.Builder generateMethodsCall(List<Element> revealMethods) {
        CodeBlock.Builder methodsCall = CodeBlock.builder()
                .beginControlFlow("return ()->");
        revealMethods.forEach(element -> {
            methodsCall.addStatement(element.getSimpleName().toString() + "()");
        });

        methodsCall.endControlFlow("");

        return methodsCall;
    }

    private void generateListenersMethod(TypeSpec.Builder proxyType) {

        TypeVariableName dominoEventTypeName = TypeVariableName.get("? extends DominoEvent", TypeName.get(DominoEvent.class));
        ParameterizedTypeName keyType = ParameterizedTypeName.get(ClassName.get(Class.class), dominoEventTypeName);
        TypeName valueType = TypeName.get(DominoEventListener.class);
        MethodSpec.Builder listenersMethod = MethodSpec.methodBuilder("getListeners")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PROTECTED)
                .returns(ParameterizedTypeName.get(ClassName.get(Map.class), keyType, valueType))
                .addStatement("$T<Class<? extends $T>, $T> listenersMap = new $T<>()", TypeName.get(Map.class), TypeName.get(DominoEvent.class), valueType, TypeName.get(HashMap.class));

        List<Element> listenToMethods = processorUtil.getAnnotatedMethods(proxyElement.asType(), ListenTo.class);

        listenToMethods.stream()
                .forEach(element -> {
                    Optional<TypeMirror> event = processorUtil.getClassValueFromAnnotation(element, ListenTo.class, "event");
                    event.ifPresent(eventType -> {
                        String listenerName = elements.getPackageOf(proxyElement).getQualifiedName().toString()
                                .replace(".presenters", ".listeners")
                                + "." + proxyElement.getSimpleName().toString()
                                + "_PresenterListenFor" + types.asElement(eventType).getSimpleName().toString();
                        listenersMethod.addStatement("listenersMap.put($T.class, new $L(this))", TypeName.get(eventType), ClassName.bestGuess(listenerName));
                    });
                });

        listenersMethod.addStatement("return listenersMap");

        proxyType.addMethod(listenersMethod.build());

    }


    private void generateFireActivationEvent(TypeSpec.Builder proxyType) {
        if (nonNull(proxyElement.getAnnotation(OnStateChanged.class))) {
            Optional<TypeMirror> eventType = processorUtil.getClassValueFromAnnotation(proxyElement, OnStateChanged.class, "value");

            proxyType.addMethod(MethodSpec.methodBuilder("fireActivationEvent")
                    .addModifiers(Modifier.PROTECTED)
                    .addAnnotation(Override.class)
                    .returns(TypeName.VOID)
                    .addParameter(TypeName.BOOLEAN, "state")
                    .addStatement("fireEvent($T.class, new $T(state))", TypeName.get(eventType.get()), TypeName.get(eventType.get()))
                    .build());
        }
    }

}
