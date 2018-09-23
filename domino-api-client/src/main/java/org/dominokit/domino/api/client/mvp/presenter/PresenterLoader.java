package org.dominokit.domino.api.client.mvp.presenter;

public interface PresenterLoader {

    String getName();
    String getConcreteName();
    Presentable getPresenter();
}
