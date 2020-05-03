package org.dominokit.domino.gwt.client.slots;

import org.dominokit.domino.api.client.mvp.slots.IsSlot;
import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.shared.extension.Content;

public interface ContentSlot extends IsSlot<Content> {
    void updateContent(HasContent view, HasContent.CreateHandler createHandler);
}
