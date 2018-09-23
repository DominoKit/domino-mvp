package org.dominokit.domino.api.client.mvp.view;

import static java.util.Objects.isNull;

public abstract class SingletonViewLoader implements ViewLoader{

    private final String presenterName;
    private View view;

    public SingletonViewLoader(String presenterName) {
        this.presenterName = presenterName;
    }

    @Override
    public String getPresenterName() {
        return presenterName;
    }

    @Override
    public View getView() {
        if(isNull(view))
            view=make();
        return view;
    }

    protected abstract View make();

}
