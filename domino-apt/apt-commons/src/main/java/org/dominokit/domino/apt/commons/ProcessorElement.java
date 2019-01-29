package org.dominokit.domino.apt.commons;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcessorElement {

    private final Element element;
    private Elements elementUtils;
    private Types typeUtils;
    private Messager messager;

    public ProcessorElement(Element element, Elements elementUtils, Types typeUtils,
                            Messager messager) {
        this.element = element;
        this.elementUtils = elementUtils;
        this.typeUtils = typeUtils;
        this.messager = messager;
    }

    public ProcessorElement make(Element element) {
        return new ProcessorElement(element, elementUtils, typeUtils, messager);
    }

    public String elementPackage() {
        return elementUtils.getPackageOf(element).getQualifiedName().toString();
    }

    public TypeElement asTypeElement() {
        return (TypeElement) element;
    }


    public String simpleName() {
        return element.getSimpleName().toString();
    }

    public String fullQualifiedNoneGenericName() {
        return elementPackage() + "." + simpleName();
    }

    public <A extends Annotation> A getAnnotation(Class<A> annotation) {
        return element.getAnnotation(annotation);
    }

    public Stream<Element> fieldsStream() {
        return element.getEnclosedElements().stream().filter(e -> e.getKind() == ElementKind.FIELD)
                .map(e -> (Element) e);
    }

    public Stream<ExecutableElement> methodsStream() {
        return element.getEnclosedElements().stream().filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement) e);
    }

    public <A extends Annotation> Stream<Element> fieldsAnnotatedWithStream(Class<A> annotationClass) {
        return element.getEnclosedElements().stream().filter(e -> e.getKind() == ElementKind.FIELD)
                .filter(e -> fieldAnnotatedWith(e, annotationClass)).map(ele -> (Element) ele);
    }

    private <A extends Annotation> boolean fieldAnnotatedWith(Element element, Class<A> annotationClass) {
        return Objects.nonNull(element.getAnnotation(annotationClass));
    }

//    public boolean isImplementsInterface(Class<?> clazz) {
//        return typeUtils.isAssignable(element.asType(),
//                (TypeMirror) elementUtils.getTypeElement(clazz.getCanonicalName()).asType());
//    }

    /**
     * <p>isAssignableFrom.</p>
     *
     * @param targetClass a {@link java.lang.Class} object.
     * @return a boolean.
     */
    public boolean isAssignableFrom(Class<?> targetClass) {
        return typeUtils.isAssignable(element.asType(), typeUtils.getDeclaredType(elementUtils.getTypeElement(targetClass.getName())));
    }

    public boolean isImplementsGenericInterface(Class<?> targetInterface) {
        return asTypeElement().getInterfaces().stream().filter(i -> isSameInterface(i, targetInterface)).count() > 0;
    }

    private boolean isSameInterface(TypeMirror i, Class<?> targetInterface) {
        return targetInterface.getCanonicalName().equals(make(typeUtils.asElement(i)).fullQualifiedNoneGenericName());
    }

    public TypeMirror getInterfaceType(Class<?> targetInterface) {
        return asTypeElement().getInterfaces().stream().filter(i -> isSameInterface(i, targetInterface))
                .collect(Collectors.toSet()).stream().findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public String getInterfaceFullQualifiedGenericName(Class<?> targetInterface) {
        return typeUtils.capture(getInterfaceType(targetInterface)).toString();
    }


    public boolean validateElementKind(ElementKind kind) {
        if (element.getKind() != kind)
            throw new ProcessingException(element, "Only " + kind + " can be annotated with @%s");
        return true;
    }

    public Element getElement() {
        return element;
    }

    public Messager getMessager() {
        return messager;
    }

    public Elements getElementUtils() {
        return elementUtils;
    }

    public Types getTypeUtils() {
        return typeUtils;
    }
}
