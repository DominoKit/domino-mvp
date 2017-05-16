package com.progressoft.brix.domino.apt.client.path;

import com.progressoft.brix.domino.api.client.annotations.PathParameter;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathParametersResolver {

    private Map.Entry<Element, String> entry;

    public PathParametersResolver(Map.Entry<Element, String> entry) {
        this.entry = entry;
    }

    public String parametersAsString() {
        StringBuilder parametersBuilder = new StringBuilder();
        constructorsAsStream()
                .forEach(e -> parametersBuilder.append(extractParameters((ExecutableElement) e)));

        return parametersBuilder.toString();
    }

    private Stream<? extends Element> constructorsAsStream() {
        return entry.getKey().getEnclosedElements()
                .stream().filter(this::isConstructor);
    }

    private boolean isConstructor(Element e) {
        return e.getKind().equals(ElementKind.CONSTRUCTOR);
    }

    private String extractParameters(ExecutableElement constructor) {
        return pathParametersAsStream(constructor)
                .map(parameter -> {
                    if (isConverterPresent(parameter))
                        return "new " + converter(parameter).asElement().getSimpleName() + "().convert(" + parameterString(parameter) + ")";

                    return parameterString(parameter);
                })
                .collect(Collectors.joining(","));
    }

    private boolean isConverterPresent(VariableElement parameter) {
        return pathAnnotationMirror(parameter).getElementValues().entrySet()
                .stream()
                .anyMatch(e -> "converter".equals(e.getKey().getSimpleName().toString()));
    }

    private DeclaredType converter(VariableElement parameter) {
        AnnotationValue converter = pathAnnotationMirror(parameter).getElementValues().entrySet().stream().filter(e -> "converter".equals(e.getKey().getSimpleName().toString()))
                .findAny().orElseThrow(IllegalArgumentException::new).getValue();
        return (DeclaredType) converter.getValue();
    }

    private AnnotationMirror pathAnnotationMirror(VariableElement parameter) {

        return parameter.getAnnotationMirrors().stream()
                .filter(a -> a.getAnnotationType().toString().equals(PathParameter.class.getCanonicalName()))
                .findAny().orElseThrow(IllegalArgumentException::new);
    }

    private String parameterString(VariableElement parameter) {
        return "path.getParameter(\"" + extractParameterName(parameter) + "\")";
    }

    private String extractParameterName(VariableElement p) {
        if (p.getAnnotation(PathParameter.class).name().isEmpty())
            return p.getSimpleName().toString();
        return p.getAnnotation(PathParameter.class).name();
    }

    private Stream<? extends VariableElement> pathParametersAsStream(ExecutableElement constructor) {
        return constructor.getParameters()
                .stream()
                .filter(this::isPathParameter);
    }

    private boolean isPathParameter(VariableElement p) {
        return p.getAnnotationMirrors()
                .stream()
                .anyMatch(a -> a.getAnnotationType().asElement().toString().equals(PathParameter.class.getCanonicalName()));
    }

    public Collection<String> imports() {
        return converters().stream().map(c -> "import " + c.asElement().asType().toString() + ";\n").collect(Collectors.toSet());
    }

    private Set<DeclaredType> converters() {
        Set<DeclaredType> converters = new HashSet<>();
        constructorsAsStream().forEach(e -> converters.addAll(
                getConverterAsSet((ExecutableElement) e)));
        return converters;
    }

    private Set<DeclaredType> getConverterAsSet(ExecutableElement e) {
        return pathParametersAsStream(e)
                .filter(this::isConverterPresent)
                .map(this::converter)
                .collect(Collectors.toSet());
    }
}
