package org.dominokit.domino.api.client.mvp.slots;

import org.dominokit.domino.api.client.mvp.view.HasContent;
import org.dominokit.domino.api.shared.extension.Content;

public interface ContentSlot extends IsSlot<Content> {
    void updateContent(HasContent view, HasContent.CreateHandler createHandler);
}
