package org.dominokit.domino.client.commons.mvp.view;

import org.dominokit.domino.api.client.mvp.view.LazyViewLoader;
import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.client.mvp.view.ViewsRepository;

import java.util.HashMap;

public class InMemoryViewRepository implements ViewsRepository {

    private final HashMap<String, LazyViewLoader> views=new HashMap<>();

    @Override
    public void registerView(LazyViewLoader lazyViewLoader) {
        if(isRegisteredPresenterView(lazyViewLoader.getPresenterName()))
            throw new ViewsRepository.ViewCannotBeRegisteredMoreThanOnce(lazyViewLoader.getPresenterName());
        views.put(lazyViewLoader.getPresenterName(), lazyViewLoader);
    }

    @Override
    public View getView(String presenterName) {
        if(isRegisteredPresenterView(presenterName))
            return views.get(presenterName).getView();
        throw new ViewsRepository.ViewNotFoundException(presenterName);
    }

    protected LazyViewLoader getViewLoader(String presenterName){
        if(isRegisteredPresenterView(presenterName))
            return views.get(presenterName);
        throw new ViewsRepository.ViewNotFoundException(presenterName);
    }

    private boolean isRegisteredPresenterView(String presenterName) {
        return views.containsKey(presenterName);
    }

    @Override
    public void clear() {
        views.clear();
    }
}
