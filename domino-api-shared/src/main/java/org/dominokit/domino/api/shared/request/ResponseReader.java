package org.dominokit.domino.api.shared.request;

@FunctionalInterface
public interface ResponseReader<T> {
    T read(String response);
}
