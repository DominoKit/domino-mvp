package com.progressoft.brix.domino.api.shared.extension;

import com.progressoft.brix.domino.api.shared.history.DominoHistory;

@FunctionalInterface
public interface MainContext extends Context{
    DominoHistory history();
}