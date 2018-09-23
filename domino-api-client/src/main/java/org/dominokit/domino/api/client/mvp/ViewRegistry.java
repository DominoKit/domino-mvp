package org.dominokit.domino.api.client.mvp;

import org.dominokit.domino.api.client.mvp.view.ViewLoader;

@FunctionalInterface
public interface ViewRegistry{
    void registerView(ViewLoader viewLoader);
}