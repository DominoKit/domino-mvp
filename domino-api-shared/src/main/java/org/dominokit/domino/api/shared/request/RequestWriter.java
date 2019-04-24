package org.dominokit.domino.api.shared.request;

@FunctionalInterface
public interface RequestWriter<T> {
    String write(T request);
}
