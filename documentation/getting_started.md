## Getting started

The easiest way to create a domino-mvp application is to use the [domino-cli](https://github.com/DominoKit/domino-cli), Visit the CLI tool [releases page](https://github.com/DominoKit/domino-cli/releases) and download the package for your operating system and environment.

The prerequisites for working with Domino-mvp are `Maven` and `Java 8 or 11` once you have them installed in your system you can creat Domino-mvp project following the steps below :

- Open a terminal in any folder and execute the following domino-cli command : 

    ```bash
    dominokit gen app -t mvp -n [app name] -g [groupId]
    ```
    
    > Replace the `[app name]` with your desired application name, this will be also used as the artifactId in the generated project, and replace `[groupId]` with your desired groupId.
    
    The command will generate a maven domino-mvp project that with sub-modules inside :
    
    - **{appname}-frondend** : In this sub-module we collect all other frontend models in the application and we compile them into JavaScript that can be served by the server-side/back-end module or any other external server if we want.
    
    - **{appname}-backend** : This is the default server-side of the application that is responsible for serving static resources including the JavaScript generated from the front-end module. 
    
    - **{appname}-api** : This is another optional server-side module that is responsible about handling REST calls and other server-side business logic, Having such module can useful in cases we want to separate our UI from the API so we can deploy them to different servers and scale them separately, you skip the generation of such module by passing the flag `--api false` in the above command.

    > To learn more about the options provided in the command line, checkout domino-cli [readme file](https://github.com/DominoKit/domino-cli/blob/master/README.md)

  The generated application so far is just a skeleton and does not include any pages, we will need to create a domino-mvp module to building a page in our new application.


- Inside the project root folder execute the following command to create a new module :
  ```bash
  dominokit gen module -n [module name] -p [prefix] -sp [sub-package]
  ```
  > Replace the `[module name]` with your desired module name, this will be also used as the artifactId in the generated module, and replace `[sub-package]` with your desired package, this package will be appended to the project groupId.

  The command above will generate a maven module inside the project and generated module will have 3 sub-modules inside :

  - **{module-name}-forntend**: In this module we will be adding our presenters, handle the routing logic, make REST calls, add startup tasks, and respond to view events, but we will not add nay UI code here.

  - **{module-name}-frontend-ui**: In this module we implement the views and UI components, we will also send UI events to the presenters, we will avoid having business logic in this part. 

  - **{module-name}-shared**: In this module will add out POJOs/DTOs and there JSON mappers, Jax-rs interfaces, events classes.
    
    > If you are not familiar with maven multi-modules we also have another option to merge and combine all above modules in a single one, we will cover this in another part of the documentation, but multi-modules is the recommended way to work with domino-mvp.
   
    > The domino-cli module command have another option to generate a 4th module `[app-name]-backend` that we can use to write module specific backend code/configurations. 

----------------------------

# How to run

- #### run `mvn clean install` to build

- #### Run for development :

    - ##### For super dev mode

        - In one terminal run `mvn gwt:codeserver -pl *-frontend -am`

        - In another terminal `cd {appname}-backend`
        - execute `mvn exec:java`
        - the server port will be printed in the logs access the application on `http://localhost:[port]`

    - ##### For gwt compiled mode

        - `cd {appname}-backend`
        - execute `mvn exec:java -Dmode=compiled`
        - the server port will be printed in the logs access the application on `http://localhost:[port]`

    - ##### For production mode

        - `cd {appname}-backend`
        - execute `java -jar target/{appname}-backend-HEAD-SNAPSHOT-fat.jar`
        - the server port will be printed in the logs access the application on `http://localhost:[port]`


- ##### Running the API server
  **In case an api module was generated you can run it by following below instructions** :

    - In a separate terminal `cd {appname}-api` and from there follow the readme file inside `{appname}-api` module

    > IDEA Intellij users once import the project into the IDE will find a pre-generated run-configurations and can just select the `Development` run-configuration and hit the run button.