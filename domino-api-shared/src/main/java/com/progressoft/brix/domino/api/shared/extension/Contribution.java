package com.progressoft.brix.domino.api.shared.extension;

@FunctionalInterface
public interface Contribution<E extends ExtensionPoint> {
    void contribute(E extensionPoint);
}
