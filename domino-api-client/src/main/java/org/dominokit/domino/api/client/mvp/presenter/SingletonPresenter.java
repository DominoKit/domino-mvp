package org.dominokit.domino.api.client.mvp.presenter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;


public abstract class SingletonPresenter implements PresenterLoader{

    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonPresenter.class);

    private final String name;
    private Presentable presenter;

    private final String concreteName;

    public SingletonPresenter(String name, String concreteName) {
        this.name = name;
        this.concreteName=concreteName;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Presentable getPresenter() {
        if(isNull(presenter)) {
            LOGGER.info("SINGLETON INIT : "+ name);
            presenter = makeInitialized();
        }
        LOGGER.info("SINGLETON RETURN : "+ name);
        return presenter;
    }

    private Presentable makeInitialized(){
        return make().init();
    }

    protected abstract Presentable make();

    @Override
    public String getConcreteName(){
        return this.concreteName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (other == null || getClass() != other.getClass())
            return false;
        return name.equals(((SingletonPresenter) other).name) && getConcreteName().equals(((SingletonPresenter) other).getConcreteName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
