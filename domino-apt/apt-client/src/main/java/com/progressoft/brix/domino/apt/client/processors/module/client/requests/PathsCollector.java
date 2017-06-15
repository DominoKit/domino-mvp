package com.progressoft.brix.domino.apt.client.processors.module.client.requests;

import com.google.auto.common.MoreElements;
import com.progressoft.brix.domino.api.client.annotations.Path;
import com.progressoft.brix.domino.api.client.annotations.PathParameter;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Types;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathsCollector {

    private final Types typeUtils;
    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> paths;

    public PathsCollector(Types typeUtils,
                          BaseProcessor.ElementFactory elementFactory, Set<String> paths) {
        this.typeUtils = typeUtils;
        this.elementFactory = elementFactory;
        this.paths = paths;
    }

    public void collectPaths(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Path.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .filter(this::isRequestClass)
                .collect(Collectors.toSet())
                .forEach(this::addPath);
    }

    private boolean isRequestClass(ProcessorElement element) {
        String superType = elementFactory.make(typeUtils.asElement(element.asTypeElement().getSuperclass()))
                .fullQualifiedNoneGenericName();
        return superType.equals(ClientServerRequest.class.getCanonicalName()) ||
                superType.equals(ClientRequest.class.getCanonicalName());
    }

    private void addPath(ProcessorElement request){
        Set<String> similarPaths= paths.stream().filter(p->p.startsWith(request.fullQualifiedNoneGenericName())).collect(Collectors.toSet());
        paths.removeAll(similarPaths);
        paths.add(request.fullQualifiedNoneGenericName() + ":" + getRequestPath(request)+":"+getPathMapper(request)+":"+getParameters(request));
    }



    private String getRequestPath(ProcessorElement element) {
        return element.getAnnotation(Path.class).path();
    }

    private String getPathMapper(ProcessorElement element) {
        AnnotationMirror annotationMirror = MoreElements.getAnnotationMirror(element.asTypeElement(), Path.class).get();
        return getMapperClass(annotationMirror);
    }

    private String getMapperClass(AnnotationMirror providerAnnotation) {
        Map<? extends ExecutableElement, ? extends AnnotationValue> valueIndex =
                providerAnnotation.getElementValues();
        String mapper="_";
        Iterator<? extends ExecutableElement> it=valueIndex.keySet().iterator();
        while(it.hasNext()){
            ExecutableElement key=it.next();
            if("mapper()".equals(key.toString()))
                return elementFactory.make(((DeclaredType)valueIndex.get(key).getValue()).asElement()).fullQualifiedNoneGenericName();
        }
        return mapper;
    }

    private String getParameters(ProcessorElement request) {
        StringBuilder parametersBuilder=new StringBuilder();
        constructors(request).forEach(e->parametersBuilder.append(parameters((ExecutableElement)e)));
        return "".equals(parametersBuilder.toString())?"_":parametersBuilder.toString();
    }

    private String parameters(ExecutableElement constructor) {
        return allParameters(constructor)
                .map(parameter -> {
                    if (isConverterPresent(parameter))
                        return parameterName(parameter)+";"+elementFactory.make(converter(parameter).asElement()).fullQualifiedNoneGenericName();

                    return parameterName(parameter)+";"+ PathParameter.DefaultParameterConverter.class.getCanonicalName();
                })
                .collect(Collectors.joining(","));
    }

    private String parameterName(VariableElement p) {
        if (p.getAnnotation(PathParameter.class).name().isEmpty())
            return p.getSimpleName().toString();
        return p.getAnnotation(PathParameter.class).name();
    }

    private boolean isConverterPresent(VariableElement parameter) {
        return pathAnnotationMirror(parameter).getElementValues().entrySet()
                .stream()
                .anyMatch(e -> "converter".equals(e.getKey().getSimpleName().toString()));
    }

    private AnnotationMirror pathAnnotationMirror(VariableElement parameter) {

        return parameter.getAnnotationMirrors().stream()
                .filter(a -> a.getAnnotationType().toString().equals(PathParameter.class.getCanonicalName()))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private DeclaredType converter(VariableElement parameter) {
        AnnotationValue converter = pathAnnotationMirror(parameter).getElementValues().entrySet().stream().filter(e -> "converter".equals(e.getKey().getSimpleName().toString()))
                .findAny().orElseThrow(IllegalArgumentException::new).getValue();
        return (DeclaredType) converter.getValue();
    }

    private Stream<? extends VariableElement> allParameters(ExecutableElement constructor) {
        return constructor.getParameters()
                .stream()
                .filter(this::isPathParameter);
    }

    private boolean isPathParameter(VariableElement p) {
        return p.getAnnotationMirrors()
                .stream()
                .anyMatch(a -> a.getAnnotationType().asElement().toString().equals(PathParameter.class.getCanonicalName()));
    }

    private Stream<? extends Element> constructors(ProcessorElement request) {
        return request.asTypeElement().getEnclosedElements()
                .stream().filter(this::isConstructor);
    }

    private boolean isConstructor(Element e) {
        return e.getKind().equals(ElementKind.CONSTRUCTOR);
    }

}