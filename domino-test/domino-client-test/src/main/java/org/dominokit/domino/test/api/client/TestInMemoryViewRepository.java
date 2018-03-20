package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.view.LazyViewLoader;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.client.commons.mvp.view.InMemoryViewRepository;

import java.util.HashMap;
import java.util.Objects;

public class TestInMemoryViewRepository extends InMemoryViewRepository {

    private final HashMap<String, LazyViewLoader> replacedViews = new HashMap<>();

    @Override
    public View getView(String presenterName) {
        if (replacedViews.containsKey(presenterName))
            return replacedViews.get(presenterName).getView();
        return super.getView(presenterName);
    }

    public void replaceView(String presenterName, TestViewFactory viewFactory) {
        replacedViews.put(presenterName, new TestViewLoader(getViewLoader(presenterName), viewFactory));
    }

    @Override
    public void clear() {
        super.clear();
        replacedViews.clear();
    }

    private class TestViewLoader extends LazyViewLoader {

        private final LazyViewLoader lazyViewLoader;
        private final TestViewFactory viewFactory;
        private View view;

        public TestViewLoader(LazyViewLoader lazyViewLoader, TestViewFactory viewFactory) {
            super(lazyViewLoader.getPresenterName());
            this.lazyViewLoader = lazyViewLoader;
            this.viewFactory = viewFactory;
        }

        @Override
        public String getPresenterName() {
            return lazyViewLoader.getPresenterName();
        }

        @Override
        public View getView() {
            if (Objects.isNull(view))
                view = viewFactory.make();
            return view;
        }

        @Override
        protected View make() {
            return getView();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other)
                return true;
            if (other == null || getClass() != other.getClass())
                return false;
            return getPresenterName().equals(((LazyViewLoader) other).getPresenterName());
        }

        @Override
        public int hashCode() {
            return getPresenterName().hashCode();
        }

    }

}
