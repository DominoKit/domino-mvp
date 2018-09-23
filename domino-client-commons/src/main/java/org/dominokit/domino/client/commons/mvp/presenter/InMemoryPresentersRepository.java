package org.dominokit.domino.client.commons.mvp.presenter;


import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.presenter.PresenterLoader;
import org.dominokit.domino.api.client.mvp.presenter.PresentersRepository;

import java.util.HashMap;

public class InMemoryPresentersRepository implements PresentersRepository {

    private final HashMap<String, PresenterLoader> presenters = new HashMap<>();
    private final HashMap<String, String> names = new HashMap<>();

    @Override
    public void clear() {
        presenters.clear();
    }

    @Override
    public void registerPresenter(PresenterLoader presenterLoader) {
        if (isRegisteredPresenter(presenterLoader.getName()))
            throw new PresenterCannotBeRegisteredMoreThanOnce(presenterLoader.getName());
        presenters.put(presenterLoader.getName(), presenterLoader);
        names.put(presenterLoader.getConcreteName(), presenterLoader.getName());
    }

    @Override
    public Presentable getPresenter(String presenterName) {
        if (isRegisteredPresenter(presenterName))
            return presenters.get(presenterName).getPresenter();
        throw new PresenterNotFoundException(presenterName);
    }

    protected PresenterLoader getPresenterLoader(String presenterName) {
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
