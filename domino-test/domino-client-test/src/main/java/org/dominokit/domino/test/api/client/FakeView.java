package org.dominokit.domino.test.api.client;

import org.dominokit.domino.api.client.mvp.view.BaseDominoView;

public abstract class FakeView extends BaseDominoView<FakeElement> {

    private boolean revealed = false;

    @Override
    protected void initRoot(FakeElement root) {

    }

    @Override
    public FakeElement createRoot() {
        return new FakeElement(attached -> {
            if(attached){
                revealHandler.onRevealed();
                revealed = true;
            }else{
                removeHandler.onRemoved();
                revealed = false;
            }
        });
    }

    public boolean isRevealed(){
        return revealed;
    }
}
