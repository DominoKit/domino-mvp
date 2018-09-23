package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.presenter.PresenterLoader;
import org.dominokit.domino.api.client.mvp.presenter.SingletonPresenter;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.client.commons.mvp.presenter.InMemoryPresentersRepository;

import java.util.HashMap;
import java.util.Objects;


public class TestInMemoryPresenterRepository extends InMemoryPresentersRepository {

    private final HashMap<String, PresenterLoader> replacedPresenters = new HashMap<>();

    @Override
    public void clear() {
        super.clear();
        replacedPresenters.clear();
    }

    @Override
    public Presentable getPresenter(String presenterName) {
        if (replacedPresenters.containsKey(presenterName))
            return replacedPresenters.get(presenterName).getPresenter();
        return super.getPresenter(presenterName);
    }

    public void replacePresenter(String presenterName, TestPresenterFactory presenterFactory) {
        replacedPresenters.put(presenterName,
                new TestPresenterLoader(super.getPresenterLoader(presenterName), presenterFactory));
    }

    private class TestPresenterLoader extends SingletonPresenter {

        public static final int HASH_NUMBER = 31;
        private final PresenterLoader presenterLoader;
        private final TestPresenterFactory presenterFactory;
        private Presentable presenter;

        public TestPresenterLoader(PresenterLoader presenterLoader, TestPresenterFactory presenterFactory) {
            super(presenterLoader.getName(), presenterLoader.getConcreteName());
            this.presenterLoader = presenterLoader;
            this.presenterFactory = presenterFactory;
        }

        @Override
        public String getName() {
            return presenterLoader.getName();
        }

        @Override
        public String getConcreteName() {
            return presenterLoader.getConcreteName();
        }

        @Override
        public Presentable getPresenter() {
            if (Objects.isNull(presenter))
                presenter = presenterFactory.make();
            return presenter.init();
        }

        @Override
        protected Presentable make() {
            return getPresenter();
        }

        @Override
        public boolean equals(Object o) {
            return this == o;
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = HASH_NUMBER * result + (presenterLoader != null ? presenterLoader.hashCode() : 0);
            result = HASH_NUMBER * result + (presenterFactory != null ? presenterFactory.hashCode() : 0);
            result = HASH_NUMBER * result + (presenter != null ? presenter.hashCode() : 0);
            return result;
        }
    }
}
