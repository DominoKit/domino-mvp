package org.dominokit.domino.api.client.request;

import org.dominokit.domino.api.client.mvp.PresenterConfig;
import org.dominokit.domino.api.client.mvp.ViewablePresenterConfig;
import org.dominokit.domino.api.client.mvp.presenter.Presentable;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenterSupplier;
import org.dominokit.domino.api.shared.request.BaseRequest;
import org.dominokit.domino.api.shared.request.Request;
import org.dominokit.domino.api.shared.request.RequestContext;
import org.dominokit.domino.api.shared.request.RequestState;

import java.util.function.Supplier;

import static java.util.Objects.nonNull;

public abstract class PresenterCommand<P extends Presentable> extends BaseRequest {

    private PresenterHandler<P> handler = presenter -> {
    };

    private final RequestState<DefaultRequestStateContext> sent =
            context -> {
                if (nonNull(handler)) {
                    handler.onReady(getRequestPresenter());
                }
                state = completed;
            };

    private Supplier<P> presenterSupplier;

    @Override
    public void startRouting() {
        state = sent;
        RequestContext.make().getConfig().getClientRouter().routeRequest(this);
    }

    public final void send() {
        execute();
    }

    public PresenterCommand<P> onPresenterReady(PresenterHandler<P> presenterHandler) {
        this.handler = presenterHandler;
        return this;
    }
    protected P getRequestPresenter() {
        return presenterSupplier.get();
    }

    protected void configure(PresenterConfig<P> config){
        this.presenterSupplier = config.getPresenterSupplier();
        if(config instanceof ViewablePresenterConfig){
            ((ViewablePresenterSupplier)this.presenterSupplier).setViewSupplier(((ViewablePresenterConfig) config).getViewSupplier());
        }
    }

    public interface PresenterHandler<P> {
        void onReady(P presenter);
    }
}
