package org.dominokit.domino.api.client.mvp.view;


public interface ViewsRepository {

    void registerView(ViewLoader singletonViewLoader);

    View getView(String presenterName);

    void clear();

    class ViewCannotBeRegisteredMoreThanOnce extends RuntimeException {
        public ViewCannotBeRegisteredMoreThanOnce(String message) {
            super(message);
        }
    }

    class ViewNotFoundException extends RuntimeException {
        public ViewNotFoundException(String message) {
            super(message);
        }
    }

}
