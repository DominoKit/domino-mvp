package org.dominokit.domino.api.client.mvp.view;

public interface HasUiHandlers<T extends UiHandlers> {
    void setUiHandlers(T uiHandlers);
}
