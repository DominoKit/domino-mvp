package com.progressoft.brix.domino.api.client.mvp.presenter;

import java.util.Objects;

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
        if(Objects.isNull(presenter))
            presenter=make();
        return presenter;
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
        int result = name.hashCode();
        result = 31 * result + getConcreteName().hashCode();
        return result;
    }
}
