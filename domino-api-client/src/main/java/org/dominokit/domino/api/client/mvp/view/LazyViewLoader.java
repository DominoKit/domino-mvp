package org.dominokit.domino.api.client.mvp.view;

import static java.util.Objects.isNull;

public abstract class LazyViewLoader {

    private final String presenterName;
    private View view;

    public LazyViewLoader(String presenterName) {
        this.presenterName = presenterName;
    }

    public String getPresenterName() {
        return presenterName;
    }

    public View getView() {
        if(isNull(view))
            view=make();
        return view;
    }

    protected abstract View make();

}
