package org.dominokit.domino.api.client.mvp.presenter;

import static java.util.Objects.isNull;

public abstract class LazyPresenterLoader {

    private final String name;
    private Presentable presenter;

    private final String concreteName;

    public LazyPresenterLoader(String name, String concreteName) {
        this.name = name;
        this.concreteName=concreteName;
    }

    public String getName() {
        return name;
    }

    public Presentable getPresenter() {
        if(isNull(presenter))
            presenter=makeInitialized();
        return presenter;
    }

    private Presentable makeInitialized(){
        return make().init();

    }

    protected abstract Presentable make();

    public String getConcreteName(){
        return this.concreteName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return name.equals(((LazyPresenterLoader) other).name) && getConcreteName().equals(((LazyPresenterLoader) other).getConcreteName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
