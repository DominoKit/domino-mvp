package com.progressoft.brix.domino.apt.client;

import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.request.ClientRequest;
import com.progressoft.brix.domino.api.shared.extension.Contribution;
import com.progressoft.brix.domino.apt.commons.*;

public class ContributionRequestSourceWriter extends JavaSourceWriter {

    private final JavaSourceBuilder sourceBuilder;
    private final String targetPackage;
    private final String generatedClassName;
    private final String presenter;
    private final String contextName;
    private final String presenterMethod;


    public ContributionRequestSourceWriter(ProcessorElement processorElement, String presenterName,
                                           String targetPackage, String generatedClassName, String presenterMethod) {
        super(processorElement);
        this.presenter = presenterName;
        this.targetPackage = targetPackage;
        this.generatedClassName = generatedClassName;
        this.sourceBuilder = new JavaSourceBuilder(generatedClassName);

        this.contextName = new FullClassName(processorElement.getInterfaceFullQualifiedGenericName(Contribution.class)).allImports().get(1);
        this.presenterMethod = presenterMethod;
    }

    @Override
    public String write() {
        this.sourceBuilder.onPackage(targetPackage)
                .imports(ClientRequest.class.getCanonicalName())
                .imports(Request.class.getCanonicalName())
                .annotate("@Request")
                .withModifiers(new ModifierBuilder().asPublic())
                .extend(ClientRequest.class.getCanonicalName() + "<" + presenter + ">");
        writeBody();
        return this.sourceBuilder.build();
    }

    private void writeBody() {
        FieldBuilder fieldBuilder = this.sourceBuilder.field("extensionPoint");
        fieldBuilder.withModifier(new ModifierBuilder().asPrivate())
                .ofType(contextName)
                .end();

        ConstructorBuilder constructorBuilder = this.sourceBuilder.constructor(generatedClassName);
        constructorBuilder.withModifier(new ModifierBuilder().asPublic())
                .takes(contextName, "extensionPoint")
                .line("this.extensionPoint=extensionPoint;")
                .end();

        MethodBuilder methodBuilder = this.sourceBuilder.method("process");
        methodBuilder.annotate("@Override")
                .withModifier(new ModifierBuilder().asProtected())
                .returnsVoid()
                .takes(presenter, "presenter")
                .line("\tpresenter." + presenterMethod + "(extensionPoint.context());")
                .end();

    }
}
