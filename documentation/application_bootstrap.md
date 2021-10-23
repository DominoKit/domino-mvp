## Application bootstrap

The following is the order of things that happens during a Domino-mvp application startup :

1. Configure submodules. this means every submodule entry point will be executed to configure the module as part of the application.
2. The main frontend module entry point is executed in which we call `ClientApp.make().run()`.
3. The `run` can take an argument  to register a handler that will be called to do further configuration 
   ```java
        ClientApp.make().run(new ClientApp.DominoOptionsHandler() {
            @Override
            public void onBeforeRun(CanSetDominoOptions dominoOptions) {
                //do something here before going to next bootstrap step.
            }
        });
   ```
4. Call all simple startup tasks
5. Call all async startup tasks.
6. Call all routing tasks.
7. Fire the `MainDominoEvent`.
8. Call Application startup handler which can be set in the run method like this
   
   ```java
   ClientApp.make().run(new ClientApp.DominoOptionsHandler() {
               @Override
               public void onBeforeRun(CanSetDominoOptions dominoOptions) {
                   dominoOptions.setApplicationStartHandler(() -> {
   
                   });
               }
           });
   ``` 