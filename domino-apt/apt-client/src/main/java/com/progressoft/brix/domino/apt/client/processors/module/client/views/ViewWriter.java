package com.progressoft.brix.domino.apt.client.processors.module.client.views;

import com.progressoft.brix.domino.api.client.mvp.ViewRegistry;
import com.progressoft.brix.domino.api.client.mvp.view.LazyViewLoader;
import com.progressoft.brix.domino.api.client.mvp.view.View;
import com.progressoft.brix.domino.apt.commons.FullClassName;
import com.progressoft.brix.domino.apt.commons.JavaSourceBuilder;
import com.progressoft.brix.domino.apt.commons.MethodBuilder;
import com.progressoft.brix.domino.apt.commons.ModifierBuilder;

import java.util.Set;

public class ViewWriter {

    private final JavaSourceBuilder sourceBuilder;

    public ViewWriter(JavaSourceBuilder sourceBuilder) {
        this.sourceBuilder = sourceBuilder;
    }

    public void write(Set<String> views){

        if(!views.isEmpty()){
            sourceBuilder.imports(ViewRegistry.class.getCanonicalName())
                    .imports(LazyViewLoader.class.getCanonicalName())
                    .imports(View.class.getCanonicalName());

            MethodBuilder methodBuilder = this.sourceBuilder.method("registerViews");
            methodBuilder.annotate("@Override")
                    .withModifier(new ModifierBuilder().asPublic())
                    .returnsVoid()
                    .takes("ViewRegistry", "registry");
            views.stream().map(p-> parseEntry(p))
                    .forEach(e-> registerPresenter(e, methodBuilder));
            methodBuilder.end();
        }
    }

    private void registerPresenter(ViewEntry e, MethodBuilder methodBuilder) {
        importView(e);
        FullClassName view=new FullClassName(e.view);
        FullClassName presenter=new FullClassName(e.presenter);

        methodBuilder.line("registry.registerView(new LazyViewLoader("+presenter.asSimpleName()+".class.getCanonicalName()) {\n" +
                "\t\t\t@Override\n" +
                "\t\t\tprotected View make() {\n" +
                "\t\t\t\treturn new "+view.asSimpleName()+"();\n" +
                "\t\t\t}\n" +
                "\t\t});");

    }

    private void importView(ViewEntry e) {
        sourceBuilder.imports(e.view).imports(e.presenter);
    }

    private ViewEntry parseEntry(String presenter) {
        String[] viewPair=presenter.split(":");
        return new ViewEntry(viewPair[0], viewPair[1]);
    }

    private class ViewEntry{
        private final String view;
        private final String presenter;

        public ViewEntry(String view, String presenter) {
            this.view = view;
            this.presenter = presenter;
        }
    }
}
