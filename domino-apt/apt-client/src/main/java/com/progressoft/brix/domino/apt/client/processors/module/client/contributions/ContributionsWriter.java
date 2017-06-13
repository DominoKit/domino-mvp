package com.progressoft.brix.domino.apt.client.processors.module.client.contributions;

import com.progressoft.brix.domino.api.client.extension.ContributionsRegistry;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;

import java.util.Set;

public class ContributionsWriter {

    private final JavaSourceBuilder sourceBuilder;

    public ContributionsWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> contributions){
        if(!contributions.isEmpty()){
            sourceBuilder.imports(ContributionsRegistry.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerContributions");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("ContributionsRegistry", "registry");
            contributions.stream().map(this::parseEntry)
                    .forEach(e-> registerContribution(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerContribution(ContributionEntry e, MethodBuilder methodBuilder) {
        importRequest(e);
        FullClassName contribution=new FullClassName(e.contribution);
        FullClassName extensionPoint=new FullClassName(e.extensionPoint);

        methodBuilder.line("registry.registerContribution("+extensionPoint.asSimpleName()+".class, new "+contribution.asSimpleName()+"());");

    }

    private void importRequest(ContributionEntry e) {
        sourceBuilder.imports(e.contribution).imports(e.extensionPoint);
    }

    private ContributionEntry parseEntry(String request) {
        String[] requestPair=request.split(":");
        return new ContributionEntry(requestPair[0], requestPair[1]);
    }

    private class ContributionEntry {
        private final String contribution;
        private final String extensionPoint;

        public ContributionEntry(String contribution, String extensionPoint) {
            this.contribution = contribution;
            this.extensionPoint = extensionPoint;
        }
    }
}
