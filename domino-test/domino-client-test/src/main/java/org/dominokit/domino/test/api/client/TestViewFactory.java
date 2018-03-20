package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.view.View;

@FunctionalInterface
public interface TestViewFactory {
    View make();
}
