## Entry point


- #### The application entry point
    Each Domino-mvp application will have at least one Entry point class inside the main frontend module, the entry point class is like the main class in any simple Java application, we use this entry point to bootstrap and configure the application.
    
    The default generated entry point should look like the following :
    
    ```java
    package org.dominokit.samples;
    
    import com.google.gwt.core.client.EntryPoint;
    import org.dominokit.domino.api.client.ClientApp;
    import org.dominokit.domino.gwt.client.app.DominoGWT;
    import org.dominokit.domino.view.GwtView;
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

- #### The module entry point
    
    In addition to the main entry point, each module we create in our application will have an entry point in its `frontend` module and `frontend-ui` module, the purpose of this entry point is to configure each module as part of the application, those entry points will mainly register a auto generated configuration like the following :
    
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
    By default, those entry points will just configure the module using the auto generated module configuration.