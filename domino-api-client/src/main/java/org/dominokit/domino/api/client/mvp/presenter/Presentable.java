package org.dominokit.domino.api.client.mvp.presenter;

public interface Presentable {
    default Presentable init(){
        return this;
    }
}
