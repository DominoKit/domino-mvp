package org.dominokit.domino.apt.client;

import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.client.mvp.view.View;

@UiView(presentable = DefaultAnnotatedClassWithPresenter.class)
public class AnnotatedClassWithUiView implements View {

}