package com.progressoft.brix.domino.api.shared.extension;

@FunctionalInterface
public interface ExtensionPoint<C extends Context> {
    C context();
}
