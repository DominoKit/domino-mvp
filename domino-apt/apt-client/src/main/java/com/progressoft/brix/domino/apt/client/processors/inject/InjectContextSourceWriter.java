package com.progressoft.brix.domino.apt.client.processors.inject;

import com.progressoft.brix.domino.api.client.annotations.AutoRequest;
import com.progressoft.brix.domino.api.client.annotations.Contribute;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.apt.commons.*;

public class InjectContextSourceWriter extends JavaSourceWriter {

    private final String presenter;
    private final String extensionPoint;
    private final JavaSourceBuilder sourceBuilder;
    private final String targetPackage;
    private final FullClassName presenterFullClassName;
    private final FullClassName extensionPointFullClassName;


    public InjectContextSourceWriter(ProcessorElement processorElement, String presenter,
                                     String extensionPoint, String targetPackage, String className) {
        super(processorElement);
        this.presenter = presenter;
        this.extensionPoint = extensionPoint;
        this.sourceBuilder = new JavaSourceBuilder(className);
        this.targetPackage = targetPackage;

        this.presenterFullClassName = new FullClassName(presenter);
        this.extensionPointFullClassName = new FullClassName(extensionPoint);
    }

    @Override
    public String write() {
        this.sourceBuilder.onPackage(targetPackage)
                .imports(Contribute.class.getCanonicalName())
                .imports(AutoRequest.class.getCanonicalName())
                .imports(Contribution.class.getCanonicalName())
                .imports(extensionPoint)
                .imports(presenter)
                .imports(makeRequestClassName())
                .annotate("@Contribute")
                .annotate("@AutoRequest(presenters={" + presenterFullClassName.asSimpleName() + ".class}, method=\"" + processorElement.simpleName() + "\")")
                .withModifiers(new ModifierBuilder().asPublic())
                .implement(Contribution.class.getCanonicalName() + "<" + extensionPoint + ">");
        writeBody();
        return this.sourceBuilder.build();
    }

    private void writeBody() {
        MethodBuilder methodBuilder = this.sourceBuilder.method("contribute");
        methodBuilder.annotate("@Override")
                .withModifier(new ModifierBuilder().asPublic())
                .returnsVoid()
                .takes(extensionPoint, "extensionPoint")
                .block("\tnew " + new FullClassName(makeRequestClassName()).asSimpleName() + "(extensionPoint).send();")
                .end();
    }

    private String makeRequestClassName() {
        return targetPackage.replace("contributions", "requests") + "." +
                "Obtain" + extensionPointFullClassName.asSimpleName() +
                "For" + presenterFullClassName.asSimpleName() +
                "ClientRequest";
    }
}
