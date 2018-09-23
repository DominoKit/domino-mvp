package org.dominokit.domino.client.commons.mvp.view;

import org.dominokit.domino.api.client.mvp.view.View;
import org.dominokit.domino.api.client.mvp.view.ViewLoader;
import org.dominokit.domino.api.client.mvp.view.ViewsRepository;

import java.util.HashMap;

public class InMemoryViewRepository implements ViewsRepository {

    private final HashMap<String, ViewLoader> views=new HashMap<>();

    @Override
    public void registerView(ViewLoader viewLoader) {
        if(isRegisteredPresenterView(viewLoader.getPresenterName()))
            throw new ViewsRepository.ViewCannotBeRegisteredMoreThanOnce(viewLoader.getPresenterName());
        views.put(viewLoader.getPresenterName(), viewLoader);
    }

    @Override
    public View getView(String presenterName) {
        if(isRegisteredPresenterView(presenterName))
            return views.get(presenterName).getView();
        throw new ViewsRepository.ViewNotFoundException(presenterName);
    }

    protected ViewLoader getViewLoader(String presenterName){
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
