## Entry point

Each Domino-mvp application will have at least one Entry point class inside the main frontend module, the entry point class is like the main class in any simple Java application, we use this entry point to bootstrap and configure the application.
    
The default generated entry point should look like the following :

```java
package org.dominokit.samples;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.api.client.ClientApp;
import org.dominokit.domino.gwt.client.app.DominoGWT;
import org.dominokit.domino.view.DominoViewOptions;
import org.dominokit.rest.DominoRestConfig;

import java.util.logging.Logger;

public class AppClientModule implements EntryPoint {

    private static final Logger LOGGER = Logger.getLogger(AppClientModule.class.getName());

    public void onModuleLoad() {
        DominoRestConfig.initDefaults()
                .setDefaultServiceRoot("http://localhost:9090");
        DominoGWT.init();
        GwtView.init();
        ClientApp.make().run();
        LOGGER.info("bookstore Application frontend have been initialized.");
    }
}

```

The entry point extends from the `EntryPoint` class and implements the `onModuleLoad` method in which we add the application bootstrapping code.

In the code above we let the application knows where are our REST endpoints are deployed, then we initialize the application with some defaults before we call the `ClientApp.make().run()` to start it.
