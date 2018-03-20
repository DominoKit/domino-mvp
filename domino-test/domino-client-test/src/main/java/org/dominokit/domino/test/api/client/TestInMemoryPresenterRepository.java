package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.presenter.LazyPresenterLoader;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.client.commons.mvp.presenter.InMemoryPresentersRepository;

import java.util.HashMap;
import java.util.Objects;


public class TestInMemoryPresenterRepository extends InMemoryPresentersRepository {

    private final HashMap<String, LazyPresenterLoader> replacedPresenters = new HashMap<>();

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

    private class TestPresenterLoader extends LazyPresenterLoader {

        public static final int HASH_NUMBER = 31;
        private final LazyPresenterLoader lazyPresenterLoader;
        private final TestPresenterFactory presenterFactory;
        private Presentable presenter;

        public TestPresenterLoader(LazyPresenterLoader lazyPresenterLoader, TestPresenterFactory presenterFactory) {
            super(lazyPresenterLoader.getName(), lazyPresenterLoader.getConcreteName());
            this.lazyPresenterLoader = lazyPresenterLoader;
            this.presenterFactory = presenterFactory;
        }

        @Override
        public String getName() {
            return lazyPresenterLoader.getName();
        }

        @Override
        public String getConcreteName() {
            return lazyPresenterLoader.getConcreteName();
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
            result = HASH_NUMBER * result + (lazyPresenterLoader != null ? lazyPresenterLoader.hashCode() : 0);
            result = HASH_NUMBER * result + (presenterFactory != null ? presenterFactory.hashCode() : 0);
            result = HASH_NUMBER * result + (presenter != null ? presenter.hashCode() : 0);
            return result;
        }
    }
}
