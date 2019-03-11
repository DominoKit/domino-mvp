package org.dominokit.domino.api.server;

public interface DominoLoaderPlugin {
    DominoLoaderPlugin init(PluginContext context);
    void setNext(DominoLoaderPlugin nextPlugin);
    void apply();
    int order();
    default boolean isEnabled(){return true;}
}
