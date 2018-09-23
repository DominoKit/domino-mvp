package org.dominokit.domino.api.client.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PrototypePresenter implements PresenterLoader {

    private final static Logger LOGGER = LoggerFactory.getLogger(PrototypePresenter.class);

    private final String name;

    private final String concreteName;

    public PrototypePresenter(String name, String concreteName) {
        this.name = name;
        this.concreteName = concreteName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Presentable getPresenter() {
        LOGGER.info("PROTOTYPE INIT RETURN : " + name);
        return makeInitialized();
    }

    private Presentable makeInitialized() {
        return make().init();
    }

    protected abstract Presentable make();

    @Override
    public String getConcreteName() {
        return this.concreteName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return name.equals(((PrototypePresenter) other).name) && getConcreteName().equals(((PrototypePresenter) other).getConcreteName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
