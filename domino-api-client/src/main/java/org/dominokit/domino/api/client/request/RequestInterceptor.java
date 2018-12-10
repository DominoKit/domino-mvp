package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface RequestInterceptor {

    void interceptRequest(ServerRequest request, InterceptorCallBack callBack);

    @FunctionalInterface
    interface InterceptorCallBack{
        void onComplete();
    }

}
