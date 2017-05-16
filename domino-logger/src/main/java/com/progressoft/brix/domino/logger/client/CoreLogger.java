package com.progressoft.brix.domino.logger.client;

import java.util.logging.Level;
import java.util.logging.Logger;


public class CoreLogger {

	private final Logger logger;

	public CoreLogger(Logger logger) {
		this.logger = logger;
	}

	public void log(String message) {
		logger.log(Level.ALL, message);
	}

	public void info(String message) {
		logger.log(Level.INFO, message);
	}

	public void warning(String message) {
		logger.log(Level.WARNING, message);
	}

	public void error(String message, Throwable throwable) {
		logger.log(Level.SEVERE, message, throwable);
	}

	public void debug(String message) {
		logger.log(Level.FINE, message);
	}

	public void debug(String message, Throwable throwable){
		logger.log(Level.FINEST, message, throwable);
	}

	public void finer(String message) {
		logger.log(Level.FINER, message);
	}

	public void trace(String message) {
		logger.log(Level.FINEST, message);
	}

	public void config(String message) {
		logger.log(Level.CONFIG, message);
	}

	public Logger logger() {
		return logger;
	}

}
