package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface RequestWriter<T> {
    String write(T request);
}
