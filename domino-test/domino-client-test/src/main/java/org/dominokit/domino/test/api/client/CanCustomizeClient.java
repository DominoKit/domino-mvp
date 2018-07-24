package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.shared.extension.DominoEventListener;

public interface CanCustomizeClient extends CanStartClient {
    CanCustomizeClient replacePresenter(Class<? extends Presentable> original, Presentable replacement);

    CanCustomizeClient replaceView(Class<? extends Presentable> presenter, View view);

    CanCustomizeClient viewOf(Class<? extends Presentable> presenter, ViewHandler handler);

    <L extends DominoEventListener> CanCustomizeClient listenerOf(Class<L> listenerType,
                                                                  ListenerHandler<L> handler);

    CanCustomizeClient onBeforeStart(BeforeStarted beforeStarted);

    CanCustomizeClient onStartCompleted(StartCompleted startCompleted);
}
