package com.progressoft.brix.domino.logger.gwt;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.util.HashMap;


public class GWTLoggerFactory implements ILoggerFactory {
  private final HashMap<String, Logger> loggers = new HashMap<>();

  @Override
  public Logger getLogger(String name) {
    if (name == null) {
      throw new NullPointerException();
    }

    if (Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(name)) {
      name = "";
    }

    Logger logger = loggers.get(name);
    if (logger == null) {
      logger = new GWTLoggerAdapter(name);
      loggers.put(name, logger);
    }
    return logger;
  }
}