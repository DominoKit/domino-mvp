package org.dominokit.domino.api.server.spi;

@FunctionalInterface
public interface RunnableWithException<T extends Throwable> {
	void run() throws T;
}
