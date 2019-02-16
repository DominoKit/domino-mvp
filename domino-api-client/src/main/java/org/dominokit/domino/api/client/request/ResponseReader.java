package org.dominokit.domino.api.client.request;

@FunctionalInterface
public interface ResponseReader<T> {
    T read(String response);
}
