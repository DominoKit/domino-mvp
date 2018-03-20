package org.dominokit.domino.api.shared.extension;

@FunctionalInterface
public interface ExtensionPoint<C extends Context> {
    C context();
}
