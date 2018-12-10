package org.dominokit.domino.api.client.mvp.view;

import org.dominokit.domino.api.shared.extension.Content;

public interface HasContent {

    Content getContent();

    default Content getContent(CreateHandler createHandler){
        return getContent();
    }

    @FunctionalInterface
    interface CreateHandler{
        void onCreated();
    }
}
