package com.progressoft.brix.domino.apt.client.processors.module.client.presenters;

import com.progressoft.brix.domino.api.client.mvp.PresenterRegistry;
import com.progressoft.brix.domino.api.client.mvp.presenter.LazyPresenterLoader;
import com.progressoft.brix.domino.api.client.mvp.presenter.Presentable;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;

import java.util.Set;

public class PresentersWriter {

    private final JavaSourceBuilder sourceBuilder;

    public PresentersWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> presenters){
        if(!presenters.isEmpty()){
            sourceBuilder.imports(PresenterRegistry.class.getCanonicalName())
                    .imports(LazyPresenterLoader.class.getCanonicalName())
                    .imports(Presentable.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerPresenters");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("PresenterRegistry", "registry");
            presenters.stream().map(p-> parseEntry(p))
                    .forEach(e-> registerPresenter(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerPresenter(PresenterEntry e, MethodBuilder methodBuilder) {
        importPresenter(e);
        FullClassName presenter=new FullClassName(e.name);
        FullClassName presenterImpl=new FullClassName(e.impl);

        methodBuilder.line("registry.registerPresenter(new LazyPresenterLoader("+presenter.asSimpleName()+".class.getCanonicalName(), "+presenterImpl.asSimpleName()+".class.getCanonicalName()) {\n" +
                "\t\t\t@Override\n" +
                "\t\t\tprotected Presentable make() {\n" +
                "\t\t\t\treturn new "+presenterImpl.asSimpleName()+"();\n" +
                "\t\t\t}\n" +
                "\t\t});");

    }

    private void importPresenter(PresenterEntry e) {
        sourceBuilder.imports(e.name).imports(e.impl);
    }

    private PresenterEntry parseEntry(String presenter) {
        String[] presenterPair=presenter.split(":");
        return new PresenterEntry(presenterPair[0], presenterPair[1]);
    }

    private class PresenterEntry{
        private final String impl;
        private final String name;

        PresenterEntry(String impl, String name) {
            this.impl = impl;
            this.name = name;
        }
    }
}
