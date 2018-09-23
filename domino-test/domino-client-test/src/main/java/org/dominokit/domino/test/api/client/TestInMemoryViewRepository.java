package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.view.SingletonViewLoader;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.client.mvp.view.ViewLoader;
import org.dominokit.domino.client.commons.mvp.view.InMemoryViewRepository;

import java.util.HashMap;
import java.util.Objects;

public class TestInMemoryViewRepository extends InMemoryViewRepository {

    private final HashMap<String, ViewLoader> replacedViews = new HashMap<>();

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

    private class TestViewLoader extends SingletonViewLoader {

        private final ViewLoader singletonViewLoader;
        private final TestViewFactory viewFactory;
        private View view;

        public TestViewLoader(ViewLoader singletonViewLoader, TestViewFactory viewFactory) {
            super(singletonViewLoader.getPresenterName());
            this.singletonViewLoader = singletonViewLoader;
            this.viewFactory = viewFactory;
        }

        @Override
        public String getPresenterName() {
            return singletonViewLoader.getPresenterName();
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
            return getPresenterName().equals(((ViewLoader) other).getPresenterName());
        }

        @Override
        public int hashCode() {
            return getPresenterName().hashCode();
        }

    }

}
