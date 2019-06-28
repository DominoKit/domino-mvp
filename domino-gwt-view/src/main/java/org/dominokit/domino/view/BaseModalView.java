package org.dominokit.domino.view;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.ui.modals.BaseModal;
import org.dominokit.domino.api.client.mvp.view.ModalView;

public abstract class BaseModalView<T extends BaseModal<T>> extends BaseElementView<HTMLDivElement> implements ModalView {

    public abstract T getModal();

    @Override
    public void open() {
        getModal().open();
    }

    @Override
    public void close() {
        getModal().close();
    }
}
