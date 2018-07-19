package org.dominokit.domino.api.client.mvp.presenter;

import org.dominokit.domino.api.client.mvp.view.CompositeView;
import org.dominokit.domino.api.client.mvp.view.View;

public class MultiViewBaseClientPresenter<V extends CompositeView<? extends View>> extends ViewBaseClientPresenter<V> {
}
