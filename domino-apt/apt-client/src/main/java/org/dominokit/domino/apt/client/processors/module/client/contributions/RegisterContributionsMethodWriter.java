package org.dominokit.domino.apt.client.processors.module.client.contributions;

import org.dominokit.domino.api.client.extension.ContributionsRegistry;
import org.dominokit.domino.apt.commons.AbstractRegisterMethodWriter;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

public class RegisterContributionsMethodWriter extends AbstractRegisterMethodWriter<ContributionEntry, String> {

    public RegisterContributionsMethodWriter(TypeSpec.Builder clientModuleTypeBuilder) {
        super(clientModuleTypeBuilder);
    }

    @Override
    protected String methodName() {
        return "registerContributions";
    }

    @Override
    protected Class<?> registryClass() {
        return ContributionsRegistry.class;
    }

    @Override
    protected void registerItem(ContributionEntry entry, MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement("registry.registerContribution($T.class, new $T())", ClassName.bestGuess(entry.extensionPoint), ClassName.bestGuess(entry.contribution));
    }

    @Override
    protected ContributionEntry parseEntry(String request) {
        String[] requestPair = request.split(":");
        return new ContributionEntry(requestPair[0], requestPair[1]);
    }
}
