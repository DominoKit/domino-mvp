package com.progressoft.brix.domino.logger.gwt;

import com.progressoft.brix.domino.logger.gwt.GWTLoggerAdapter;
import org.slf4j.ILoggerFactory;

public class Impl {
    public static final ILoggerFactory LOGGER_FACTORY = GWTLoggerAdapter::new;

    private Impl() {
    }
}
