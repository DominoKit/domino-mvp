package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.shared.extension.Content;

@UiView(presentable = DefaultAnnotatedClassWithPresenter.class)
public class AnnotatedClassWithUiView implements View {
    @Override
    public Content getContent() {
        return null;
    }
}