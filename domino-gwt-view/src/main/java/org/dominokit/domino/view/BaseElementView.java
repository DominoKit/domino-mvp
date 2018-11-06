package org.dominokit.domino.view;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLElement;
import org.dominokit.domino.api.client.mvp.view.BaseDominoView;
import org.dominokit.domino.ui.utils.ElementUtil;

import static java.util.Objects.nonNull;

public abstract class BaseElementView<T extends HTMLElement> extends BaseDominoView<T> {
    @Override
    protected void initRoot(T root) {
        if(nonNull(root)){
            if(nonNull(revealHandler)){
                DomGlobal.console.info("Register attach handler for view : "+this.getClass().getName());
                ElementUtil.onAttach(root, mutationRecord -> revealHandler.onRevealed());
            }

            if(nonNull(removeHandler)){
                ElementUtil.onDetach(root, mutationRecord -> {
                    DomGlobal.console.info("Register dettach handler for view : "+this.getClass().getName());
                    removeHandler.onRemoved();
                    clear();
                });
            }
        }
    }

}
