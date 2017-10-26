package com.progressoft.brix.domino.apt.client.processors.contributions;

import com.google.auto.common.MoreElements;
import com.google.auto.service.AutoService;
import com.progressoft.brix.domino.api.client.annotations.AutoRequest;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.apt.commons.BaseProcessor;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@AutoService(Processor.class)
public class ContributionClientRequestProcessor extends BaseProcessor {

    private static final Logger LOGGER = Logger.getLogger(ContributionClientRequestProcessor.class.getName());

    private final Set<String> supportedAnnotations = new HashSet<>();

    public ContributionClientRequestProcessor() {
        supportedAnnotations.add(AutoRequest.class.getCanonicalName());
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(AutoRequest.class).stream().filter(this::hasPresenters)
                .forEach(this::generateContributionClientRequest);
        return false;
    }

    private void generateContributionClientRequest(Element e) {//NOSONAR
        Set<String> presenters = getContributionPresenters(e);
        String presenterMethod = getMethodName(e);
        presenters.forEach(p -> generateRequest(newProcessorElement(e), p, presenterMethod));

    }

    private void generateRequest(ProcessorElement processorElement, String presenterName, String presenterMethod) {
        String contributionName = processorElement.getInterfaceFullQualifiedGenericName(Contribution.class);
        FullClassName fullClassname = new FullClassName(contributionName);
        String contextName = fullClassname.allImports().get(1);
        FullClassName contextFullName = new FullClassName(contextName);
        String contextSimpleName = contextFullName.asSimpleName();
        String presenterSimpleName = new FullClassName(presenterName).asSimpleName();
        String targetPackage = processorElement.elementPackage().replace("contributions", "requests");
        String generatedClassName = "Obtain" + contextSimpleName + "For" + presenterSimpleName + "PresenterCommand";
        try (Writer sourceWriter = obtainSourceWriter(
                targetPackage, generatedClassName)) {
            sourceWriter
                    .write(new ContributionRequestSourceWriter(processorElement, presenterName, targetPackage, generatedClassName).write());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not generate classes : ", e);
            messager.printMessage(Diagnostic.Kind.ERROR, "could not generate class");
        }
    }

    private boolean hasPresenters(Element e) {//NOSONAR
        Set<String> presenters = getContributionPresenters(e);
        return nonNull(presenters) && !presenters.isEmpty();
    }

    private Set<String> getContributionPresenters(Element e) {
        AnnotationMirror
                providerAnnotation = MoreElements.getAnnotationMirror(e, AutoRequest.class).get();
        List<DeclaredType> providerInterfaces = this.getProviderInterface(providerAnnotation);
        Set<TypeElement> elements = providerInterfaces.stream().map(this::asTypeElement).collect(Collectors.toSet());
        return elements.stream().map(t -> t.getQualifiedName().toString()).collect(Collectors.toSet());
    }

    private String getMethodName(Element e) {
        return this.getMethod(MoreElements.getAnnotationMirror(e, AutoRequest.class).get());
    }

    private TypeElement asTypeElement(DeclaredType p) {
        return (TypeElement) p.asElement();
    }

    private List<DeclaredType> getProviderInterface(AnnotationMirror providerAnnotation) {
        return getPresentersList(getPresentersAnnotationValueObject(providerAnnotation.getElementValues()));
    }

    private List<DeclaredType> getPresentersList(Object annotationValueObject) {
        if (nonNull(annotationValueObject))
            return ((List<AnnotationValue>) ((AnnotationValue) annotationValueObject).getValue()).stream().map(v -> (DeclaredType) v.getValue()).collect(Collectors.toList());
        return new ArrayList<>();
    }

    private Object getPresentersAnnotationValueObject(Map valueIndex) {
        return valueIndex.get(valueIndex.keySet().stream().filter(k -> "presenters()".equals(k.toString())).findFirst().orElse(null));
    }

    private String getMethod(AnnotationMirror providerAnnotation) {//NOSONAR
        Map valueIndex = providerAnnotation.getElementValues();
        Object annotationValueObject = valueIndex.get(valueIndex.keySet().stream().filter(k -> "method()".equals(k.toString())).findFirst().orElse(null));

        if (nonNull(annotationValueObject))
            return (String) ((AnnotationValue) annotationValueObject).getValue();
        return "";
    }
}
