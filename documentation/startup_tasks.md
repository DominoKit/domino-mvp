## Startup tasks

In a Domino-mvp application we can create and register as many startup tasks as we want, after we call `ClientApp.mak().run()` in the main entry point, and after we configure all application module only, all startup tasks will be executed before any presenter can be activated.

Startup tasks are useful for doing application specific configuration or loading lookups for example.

There is rwo types of startup tasks, `ClientStartupTask` -simple startup task- and `AsyncClientStartupTask` :

- #### Simple startup task
    We can add a startup task to our application by implementing `ClientStartupTask` and annotate the class with `@StartupTask`, then we add our task logic in the execute method, there is nothing special here, such task will be executed during our application bootstrap :

    ```java
    @StartupTask
    public class SampleTask implements ClientStartupTask {
      @Override
      public void execute() {
        //do some initialization here
      }
    }
    ```

- #### Async startup tasks
  Sometimes we might need to do some async work inside a startup task, like doing a REST request to the server to fetch some data, in this case we might be interested in blocking the bootstrapping of the application until the data request is succeeded and data is processed, in this case we create an Async startup task by extending from the `AsyncClientStartupTask` and annotate the class with `@StartupTask`, then in the execute method we add our logic, but for Async tasks we must call the `complete` method to mark the task as completed, and until we do so the task will block the application bootstrapping.

  Async Tasks are ordered and grouped, you can change the order and group of an async task by overriding the `order` method, all Async task with a specific order should be completed be fore any startup task with the next order can be executed. 

  Async task look like the following 

    ```java 
    @StartupTask
    public class SampleAsyncStartUpTask extends AsyncClientStartupTask<SomeObject> {
    
        @Override
        public void execute() {
            asyncCallToTheServer
                    .onSuccess(someObjectResponse -> {
                        complete(someObjectResponse);
                    })
                    .onFailed(err -> {
                        LOGGER.error("server call failed : ", err);
                        complete(defaultObjectInstance);
                    })
        }
    
        @Override
        public int order() {
            return 1;
        }
    }
    ```
  
    > For Async tasks you need to make sure to call the `complete` method or otherwise the application will be blocked and will not start.