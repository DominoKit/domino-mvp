package com.progressoft.brix.domino.logger.gwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class Log {
  private static final Logger LOGGER = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

  /**
   * Send a DEBUG log message.
   */
  public static void d(String message) {
    LOGGER.debug(message);
  }

  /**
   * Send a DEBUG log message and log the exception.
   */
  public static void d(String message, Throwable t) {
    LOGGER.debug(message, t);
  }

  /**
   * Send a formatted DEBUG log message.
   */
  public static void d(String format, Object... args) {
    LOGGER.debug(format, args);
  }

  /**
   * Send an ERROR log message.
   */
  public static void e(String message) {
    LOGGER.error(message);
  }

  /**
   * Send an ERROR log message and log the exception.
   */
  public static void e(String message, Throwable t) {
    LOGGER.error(message, t);
  }

  /**
   * Send a formatted ERROR log message.
   */
  public static void e(String format, Object... args) {
    LOGGER.error(format, args);
  }

  /**
   * Send an INFO log message.
   */
  public static void i(String message) {
    LOGGER.info(message);
  }

  /**
   * Send an INFO log message and log the exception.
   */
  public static void i(String message, Throwable t) {
    LOGGER.info(message, t);
  }

  /**
   * Send a formatted INFO log message.
   */
  public static void i(String format, Object... args) {
    LOGGER.info(format, args);
  }

  /**
   * Send a TRACE log message.
   */
  public static void t(String message) {
    LOGGER.trace(message);
  }

  /**
   * Send a TRACE log message and log the exception.
   */
  public static void t(String message, Throwable t) {
    LOGGER.trace(message, t);
  }

  /**
   * Send a formatted TRACE log message.
   */
  public static void t(String format, Object... args) {
    LOGGER.trace(format, args);
  }

  /**
   * Send a WARN log message.
   */
  public static void w(String message) {
    LOGGER.warn(message);
  }

  /**
   * Send a WARN log message and log the exception.
   */
  public static void w(String message, Throwable t) {
    LOGGER.warn(message, t);
  }

  /**
   * Send a formatted WARN log message.
   */
  public static void w(String format, Object... args) {
    LOGGER.warn(format, args);
  }

  private Log() {
  }
}
