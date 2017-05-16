package com.progressoft.brix.domino.logger.client;

import java.util.logging.Logger;

public class CoreLoggerFactory {

	private CoreLoggerFactory(){
	}

	public static CoreLogger getLogger(String className) {
		return new CoreLogger(Logger.getLogger(className));
	}

	public static CoreLogger getLogger(Class<?> type) {
		return new CoreLogger(Logger.getLogger(type.getName()));
	}

}
