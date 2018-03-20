package org.dominokit.domino.api.client.mvp.presenter;

public interface PresentersRepository {

    void registerPresenter(LazyPresenterLoader lazyPresenterLoader);

    Presentable getPresenter(String presenterName);
    String getNameFromConcreteName(String concreteName);

    void clear();

    class PresenterCannotBeRegisteredMoreThanOnce extends RuntimeException {
        private static final long serialVersionUID = 956087089156886416L;

        public PresenterCannotBeRegisteredMoreThanOnce(String message) {
            super(message);
        }
    }

    class PresenterNotFoundException extends RuntimeException {
        private static final long serialVersionUID = -6455103815754837305L;

        public PresenterNotFoundException(String message) {
            super(message);
        }
    }
}
