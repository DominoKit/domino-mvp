package org.dominokit.domino.api.client.mvp.view;

public abstract class PrototypeViewLoader implements ViewLoader {

    private final String presenterName;

    public PrototypeViewLoader(String presenterName) {
        this.presenterName = presenterName;
    }

    @Override
    public String getPresenterName() {
        return presenterName;
    }

    @Override
    public View getView() {
        return make();
    }

    protected abstract View make();

}
