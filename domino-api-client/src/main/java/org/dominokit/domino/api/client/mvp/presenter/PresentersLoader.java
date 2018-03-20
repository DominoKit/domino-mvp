package org.dominokit.domino.api.client.mvp.presenter;

@FunctionalInterface
public interface PresentersLoader {
    void load(PresentersRepository repository);
}
