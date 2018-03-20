package org.dominokit.domino.apt.client.processors.module.client.contributions;

import org.dominokit.domino.api.client.annotations.Contribute;
import org.dominokit.domino.api.shared.extension.Contribution;
import org.dominokit.domino.apt.commons.BaseProcessor;
import org.dominokit.domino.apt.commons.FullClassName;
import org.dominokit.domino.apt.commons.ProcessorElement;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class ContributionsCollector {

    private final Messager messager;
    private final BaseProcessor.ElementFactory elementFactory;
    private final Set<String> contributions;

    public ContributionsCollector(Messager messager,
                                  BaseProcessor.ElementFactory elementFactory, Set<String> contributions) {
        this.messager = messager;
        this.elementFactory = elementFactory;
        this.contributions = contributions;
    }

    public void collectContributions(RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Contribute.class).stream()
                .map(elementFactory::make)
                .filter(e -> e.validateElementKind(ElementKind.CLASS))
                .collect(Collectors.toSet())
                .forEach(this::addContribution);
    }

    private boolean addContribution(ProcessorElement c) {
        if(!c.isImplementsGenericInterface(Contribution.class)) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Not implementing contribution interface", c.getElement());
            throw new NotImplementingContributionInterfaceException();
        }
        return contributions
                .add(c.fullQualifiedNoneGenericName() + ":" +
                        getContributionExtensionPoint(c));
    }

    private String getContributionExtensionPoint(ProcessorElement element) {
        String contribution = element.getInterfaceFullQualifiedGenericName(Contribution.class);
        return new FullClassName(contribution).allImports().get(1);
    }

    public class NotImplementingContributionInterfaceException extends RuntimeException {
    }
}