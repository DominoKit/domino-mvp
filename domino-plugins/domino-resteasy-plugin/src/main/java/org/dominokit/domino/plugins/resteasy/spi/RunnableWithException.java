package org.dominokit.domino.plugins.resteasy.spi;

@FunctionalInterface
public interface RunnableWithException<T extends Throwable> {
	void run() throws T;
}
