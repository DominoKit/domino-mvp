package com.progressoft.brix.domino.configurations;

import static java.util.Objects.isNull;

class ConfigLocation {

    private static final String CONFIG_LOCATION = "CONFIG_PATH";
    private static final String DEFAULT_CONFIG_LOCATION = "./config";

    String getDefault(){
        if (isNull(System.getenv().get(CONFIG_LOCATION)))
            return DEFAULT_CONFIG_LOCATION;
        return System.getenv().get(CONFIG_LOCATION);
    }
}
