package org.dominokit.domino.view.slots;

import org.dominokit.domino.api.client.mvp.view.ContentView;

public interface ModalView extends ContentView {
    void open();
    void close();
}
