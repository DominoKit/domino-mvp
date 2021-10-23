## Module configuration

In addition to the main entry point, each module we create in our application using `dominokit gen module` command will have an entry point in its `frontend` module and `frontend-ui` module, the purpose of the entry points is to configure each module as part of the application, those entry points will mainly register an auto generated configuration like the following :

```java
package org.dominokit.samples.shell.client;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.api.client.ModuleConfigurator;
import org.dominokit.domino.api.client.annotations.ClientModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientModule(name="Shell")
public class ShellClientModule implements EntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShellClientModule.class);

    public void onModuleLoad() {
        LOGGER.info("Initializing Shell frontend module ...");
        new ModuleConfigurator().configureModule(new ShellModuleConfiguration());
    }
}

```

> You will hardly need to learn about the generated code but, you can take a look to understand more, mostly it is some calls to internal apis to register and setup things.
 