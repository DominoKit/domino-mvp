package org.dominokit.domino.apt.commons;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class ProcessorUtil {

    protected final Messager messager;
    protected final Filer filer;
    protected final Types types;
    protected final Elements elements;
    protected final ProcessingEnvironment processingEnv;

    protected ProcessorUtil(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.types = processingEnv.getTypeUtils();
        this.elements = processingEnv.getElementUtils();
        this.processingEnv = processingEnv;
    }

    public List<Element> getAnnotatedMethods(TypeMirror beanType, Class<? extends Annotation> annotation) {
        return getAnnotatedElements(beanType, annotation, element -> ElementKind.METHOD.equals(element.getKind()));
    }

    public List<Element> getAnnotatedFields(TypeMirror beanType, Class<? extends Annotation> annotation) {
        return getAnnotatedElements(beanType, annotation, element -> ElementKind.FIELD.equals(element.getKind()));
    }

    public List<Element> getAnnotatedElements(TypeMirror beanType, Class<? extends Annotation> annotation, Function<Element, Boolean> filter) {
        TypeElement typeElement = (TypeElement) types.asElement(beanType);

        final List<Element> methods = new ArrayList<>();

        List<Element> annotatedMethods = getAnnotatedElements(typeElement, annotation, filter);
        methods.addAll(annotatedMethods);

        return methods;
    }

    public List<Element> getAnnotatedElements(TypeElement typeElement, Class<? extends Annotation> annotation, Function<Element, Boolean> filter) {
        TypeMirror superclass = typeElement.getSuperclass();
        if (superclass.getKind().equals(TypeKind.NONE)) {
            return new ArrayList<>();
        }

        List<Element> methods = typeElement.getEnclosedElements()
                .stream()
                .filter(filter::apply)
                .filter(element -> nonNull(element.getAnnotation(annotation)))
                .collect(Collectors.toList());

        methods.addAll(getAnnotatedElements((TypeElement) types.asElement(superclass), annotation, filter));
        return methods;
    }

    public Optional<TypeMirror> findTypeArgument(TypeMirror element, Class<?> targetClass) {
        if (element.getKind().equals(TypeKind.NONE)) {
            return Optional.empty();
        }

        DeclaredType elementType = (DeclaredType) element;
        List<? extends TypeMirror> typeArguments = elementType.getTypeArguments();
        for (TypeMirror type : typeArguments) {
            if (isAssignableFrom(type, targetClass)) {
                return Optional.of(type);
            }
        }


        TypeElement typeElement = (TypeElement) types.asElement(element);

        List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
        for (TypeMirror interfaceType : interfaces) {
            List<? extends TypeMirror> interfaceTypeArguments = ((DeclaredType) interfaceType).getTypeArguments();
            for (TypeMirror type : interfaceTypeArguments) {
                if (isAssignableFrom(type, targetClass)) {
                    return Optional.of(type);
                }
            }
        }
        return findTypeArgument(typeElement.getSuperclass(), targetClass);
    }

    public String capitalizeFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public String smallFirstLetter(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public String lowerFirstLetter(String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    public boolean isAssignableFrom(Element element, Class<?> targetClass) {
        return types.isAssignable(element.asType(), types.getDeclaredType(elements.getTypeElement(targetClass.getCanonicalName())));
    }

    public boolean isAssignableFrom(TypeMirror element, Class<?> targetClass) {
        return types.isAssignable(element, types.getDeclaredType(elements.getTypeElement(targetClass.getCanonicalName())));
    }

    public Optional<TypeMirror> getClassValueFromAnnotation(Element element, Class<? extends Annotation> annotation, String paramName) {
        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            if (types.isSameType(am.getAnnotationType(), elements.getTypeElement(annotation.getCanonicalName()).asType())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if (paramName.equals(entry.getKey().getSimpleName().toString())) {
                        AnnotationValue annotationValue = entry.getValue();
                        return Optional.of((DeclaredType) annotationValue.getValue());
                    }
                }
            }
        }
        return Optional.empty();
    }


    public List<TypeMirror> getClassArrayValueFromAnnotation(Element element, Class<? extends Annotation> annotation, String paramName) {

        List<TypeMirror> values = new ArrayList<>();

        for (AnnotationMirror am : element.getAnnotationMirrors()) {
            if (types.isSameType(am.getAnnotationType(), elements.getTypeElement(annotation.getCanonicalName()).asType())) {
                for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : am.getElementValues().entrySet()) {
                    if (paramName.equals(entry.getKey().getSimpleName().toString())) {
                        List<AnnotationValue> classesTypes = (List<AnnotationValue>) entry.getValue().getValue();
                        Iterator<? extends AnnotationValue> iterator = classesTypes.iterator();

                        while (iterator.hasNext()) {
                            AnnotationValue next = iterator.next();
                            values.add((TypeMirror) next.getValue());
                        }
                    }
                }
            }
        }
        return values;
    }


    public List<ExecutableElement> getElementMethods(Element element) {
        return element.getEnclosedElements()
                .stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement) e)
                .collect(Collectors.toList());
    }

    public boolean isStringType(TypeMirror typeMirror) {
        TypeMirror stringType = elements.getTypeElement("java.lang.String").asType();
        return types.isAssignable(stringType, typeMirror);
    }
}
