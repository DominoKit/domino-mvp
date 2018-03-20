package org.dominokit.domino.client.commons.mvp.presenter;


import org.dominokit.domino.api.client.mvp.presenter.LazyPresenterLoader;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.presenter.PresentersRepository;

import java.util.HashMap;

public class InMemoryPresentersRepository implements PresentersRepository {

    private final HashMap<String, LazyPresenterLoader> presenters = new HashMap<>();
    private final HashMap<String, String> names = new HashMap<>();

    @Override
    public void clear() {
        presenters.clear();
    }

    @Override
    public void registerPresenter(LazyPresenterLoader lazyPresenterLoader) {
        if (isRegisteredPresenter(lazyPresenterLoader.getName()))
            throw new PresenterCannotBeRegisteredMoreThanOnce(lazyPresenterLoader.getName());
        presenters.put(lazyPresenterLoader.getName(), lazyPresenterLoader);
        names.put(lazyPresenterLoader.getConcreteName(), lazyPresenterLoader.getName());
    }

    @Override
    public Presentable getPresenter(String presenterName) {
        if (isRegisteredPresenter(presenterName))
            return presenters.get(presenterName).getPresenter();
        throw new PresenterNotFoundException(presenterName);
    }

    protected LazyPresenterLoader getPresenterLoader(String presenterName) {
        if (isRegisteredPresenter(presenterName))
            return presenters.get(presenterName);
        throw new PresenterNotFoundException(presenterName);
    }

    @Override
    public String getNameFromConcreteName(String concreteName) {
        if (isRegisteredName(concreteName))
            return names.get(concreteName);
        throw new PresenterNotFoundException(concreteName);
    }

    private boolean isRegisteredPresenter(String presenterName) {
        return presenters.containsKey(presenterName);
    }

    private boolean isRegisteredName(String concreteName) {
        return names.containsKey(concreteName);
    }

}
