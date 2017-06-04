<p align="center"><font size="20">Welcome to <img alt="domino"src="https://avatars2.githubusercontent.com/u/28739752?v=3&s=200" width="50"/> Domino! </font></p>
<p align="center">
<a title="Gitter" href=""><img src="https://badges.gitter.im/Join%20Chat.svg"></a>
</p>
Domino is a small, simple, and  light weighted framework for building applications using  [GWT](http://www.gwtproject.org/)  and [Vertx](http://vertx.io/).  Domino introduces the concept of extension points and contributions allowing developers to write a modular application with shared component with any other domino application. With vertx as a back-end engine domino gives the ability and choice to build one monolithic but yet modular application, but also provides extensions in which a large application is built as a suite of micro-services, moreover allows building the application using TDD approach with practices offering an easy and fast way to debug for both client and service side.

**We are still not done yet!** Domino comes with ready to use archetypes, one is for creating a domino application, and the other two creates the modules within a domino application the difference between the last two archetypes is that one of these archetypes comes with GMD [GWT Material Design](https://github.com/GwtMaterialDesign) set-up and ready.

## Domino Archetypes:		
			
	#### Domino application		
	> Gruop Id : `com.progressoft.brix.domino.archetypes`		
	> 		
	> Artifact Id  : `domino-gwt-app-archetype`		
	> 		
	> Version      : `1.0-rc.1`		
			
	#### Domino module		
	> Gruop Id : `com.progressoft.brix.domino.archetypes`		
	> 		
	> Artifact Id  : `domino-gwt-module-archetype`		
	> 		
	> Version      : `1.0-rc.1`		
			
	#### Domino module with GMD		
	> Gruop Id : `com.progressoft.brix.domino.archetypes`		
	> 		
	> Artifact Id  : `domino-material-module-archetype`		
	> 		
	> Version      : `1.0-rc.1`		
	

There is still a lot of things to learn about domino please follow the below step by step tutorial that explains and shows the simplicity of domino and how to use it.

# **Table of Contents**
* [Task 1 : Create your first domino application](#Task1)
* [Task 2: Run the application](#Task2)
* [Task 3: Creating a new domino module](#Task3)


## **Task 1 : Create your first domino application** <a name="Task1"></a>
*For this tutorial we are going to implement a very simple domino application in which will end up with a simple layout and a simple form that lookup someones email  from the server, just like the gif below :

![enter image description here](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/050_contact_ui.gif)

we will use [Intellij](https://www.jetbrains.com/idea/) IDE and make sure your JDK is 1.8 or higher*		
			
We are going to start by creating a new project.

- Open intellij and press on Create New Project.

![image1](https://raw.githubusercontent.com/GwtDomino/domino/0d80397597737c14f08032856adc6cba36b188a7/documents/012.png)

- We are going to create a new Maven project using the domino application archetype, a window will pop up make sure of the below values:
  - step 1: maven.
  - step 2: make sure of the project JDK to be 1.8.
  - step 3: tick create from archetype.
  - step 4: click on add archetype.

 ![image2](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/013.png)

- After clicking on add archetype button, another window will pop up fill it with the following values: 
 - GroupId: `com.progressoft.brix.domino.archetypes`
 - ArtifactId: `domino-gwt-app-archetype`
 - Version: `1.0-rc.1`

![image3](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/014.png)

*After filling up the pop up click on the **OK** button.*
- Choose the created archetype and click **Next**.

![image5](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/015.png)

- Fill the dialog as shown below image and click **Next**.			

![image6](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/016.png)

- Continue by clicking **Next**.		

![image7](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/017.png)

- Fill the dialog as shown below image and click **Finish**.	

![image8](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/018.png)

- Now the project is starting up, click on the **enable auto import** if a pop up appeared  at the bottom right corner of the screen and wait until Maven tasks are completed.

![image9](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/019.png)

When Maven completes the tasks a new project will be created and thats it we now have a domino application ready to run and can add new modules to it.

but before adding any new module lets take a look at the project structure as shown below image:

![image10](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/004_domino_app_structure.png)

If you had noticed your project is a multi module maven project that contains two modules a back-end module and a front-end module both modules are described below,

#### Back-end module	
The final output from any domino application will be provided from the back-end, which means that when you build a domino application you will be only interested in the output in the **`target`** folder of the back-end module, the back-end module contains all that we need to build our application and produce a deploy-able artifacts, like static resources, configuration files, assemblies and dependencies, **but not source code**,  we are not supposed to create classes or packages in the back-end module, because we are building modular application our code should be within domino modules and these modules are then added to the back-end module as dependencies. 

#### Frond-end module
Same as the back-end module we don't add any source code directly to this module, front-end module contains a single class that acts as a main class for the client side code, this single class is a normal GWT entry point with predefined logic that runs our client side application, we add the GWT client side modules to the front-end module as dependencies. When building the application front-end module will be compiled into JavaScript and the output will be automatically placed into the **`webroot`** folder of the back-end module.

> For GWT we use `Thomas Broyer` [maven plugin](https://github.com/tbroyer/gwt-maven-plugin)  which automatically detect GWT module within any maven dependency and adds the required inherits tags in the `.gwt.xml` files.

> The idea of separating client side -Front-end- code and the server side -Back-end- code each on its own module was actually inspired from `Thomas Broyer` [GWT archetype](https://github.com/tbroyer/gwt-maven-archetypes)
> I would really like to thank him for the great archetype.

## **Task 2 : Run the application** <a name="Task2"></a>
Note that before starting we need to build the application first, open a terminal in Intellij and type `mvn clean install` goal, this goal will trigger the GWT compiler,  after building the application successfully you will notice a new folder created inside `webroot` folder in the back-end module this new folder is the result of GWT compilation, also you will notice new jars, as domino produces jars not wars, in the target folder of the back-end module focus on the fat jar as it represents the final output.

*The file `how-to.txt` lists full instructions of how to run a domino application but we will go through them quickly below.*

- From intellij menu select Run -> Edit configuration.

![image11](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/020.png)

- A window will pop up click on the green add button  and select `Application` from the list.

![image12](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/021.png)

- Another window will pop up fill it as below;
    - Fill a name for the configuration.
    - Fill the Main class with : `com.progressoft.brix.domino.api.server.DominoLauncher`
    - Fill the program arguments with : `run com.progressoft.brix.domino.api.server.StartupVerticle -conf target/classes/config.json`
    - Select the Working directory as `domino-demo-backend` module folder.
    - Select the Use class path of module as `domino-demo-backend`
    - Make sure the JRE is 1.8.
    - Click **Apply and OK **.

![image13](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/022.png)

We are all set and ready to run the application, click the green run button on the top left of the tools bar.
![image14](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/023.png)
When the application is ready you should see the below message on the run console
**`INFO: Succeeded in deploying verticle`**
this means that the application is now up and running,  open any browser and visit **`http://localhost:8080`**
The browser page will be a blank page which is normal since we had only created an empty application we didn't add any module yet, hit **`F12`**  and look at the console the below logs will appear :

> Sun May 21 01:23:58 GMT+300 2017 com.progressoft.brix.domino.gwt.client.Core
INFO: Initialize domino module...
Sun May 21 01:23:58 GMT+300 2017 domino.tutorial.AppClientModule
INFO: Application frontend have been initialized.

## **Task 3 : Creating a new domino module** <a name="Task3"></a>
*As mentioned before we don't add any code to the back-end or front-end modules, in order for us to add a simple layout to the home page we need to add a new domino module.*

Below steps will help you to add a new domino module with a simple layout,	

- Right click on the **`domino-demo`** that is the main project the root level of the application, and select **New -> Module**.

![image15](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/024.png)

![image16](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/025.png)

- We are adding a new archetype as explained above, click on add a archetype.
*Note: make sure you have picked maven module and the create from archetype checkbox is checked as described above task.*
- A window will pop up fill it with the following values: 
  - GroupId: com.progressoft.brix.domino.archetypes
  - ArtifactId: domino-material-module-archetype
  - Version: 1.0-SNAPSHOT
- Choose the created archetype and click **Next**.
- In the Artifact Id set the value to `layout` and click **Next**.
![image17](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/026.png)
- Click on the green add button ![enter image description here](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/007_add_button.png) and add the following values : 
      -  Name : module, Value : Layout - notice he uppercase L -.
      - Name : subpackage, Value : layout  - notice the lowercase l-
      
![image18](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/027.png)
![image19](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/028.png)

- Click **Next** until you finish the wizard.
- Wait until the maven task is completed then the new module should be created.
- Take a look at the project structure after the creation of the new module as shown below image:

![image20](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/029.png)

Layout module is actually a multi module maven project, below describes the structure of the module :

-  **layout-backend** : this is where the server code goes, open the module you will notice that there is some classes already created like the `package-info` class this class defines the server module used by **APT**-Annotation Processing Tool- to generate a server module configuration, you will also find the `handlers` package that contains a handler class, this class is for when sending a request to the server from the client you will receive the request in such handler.
layout-backend module is not needed for this module, can be deleted easily as listed below,
	- Right click on the `layout-backend` module and select **Remove module**.
	- Right click on the `layout-backend` module and select **Delete**.
	- Open the `layout` module `pom.xml` file and remove the `layout-backend` module from the modules list.
		 ``` 
			<modules>
		        <module>layout-frontend</module>
		        <module>layout-frontend-ui</module>
		        <module>layout-shared</module>
		        <module>layout-backend</module> <!-- remove this line -->
		    </modules>
    ```
- **layout-frontend** : this is where all the client side flow goes in, in this module we use APT to generate a client module configuration, also the interaction with other modules, contributions to extension points, sending requests, and add all client side logic starts from here. Never ass any UI rendering code, we don't force you but in this module you should never have buttons, text fields, check boxes etc ... 
We encourage that your flow should be independent from any UI presentation and terms we are going to demonstrate it as we go further with this task.
Open the module you will notice few classes already created, in some cases this might be all what we need but in our current demonstration this is more than what we need, lets clean it up a bit, follow the points below,
	- Open the `layout-frontend` module and navigate through the source until you find the **requests** package as shown below image,
	
	![image21](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/030.png)
	
	- We have deleted the backend module we don't need to make any requests to the server, right click on the request package and delete it.
	- Deleting the request package will produce compilation error, navigate to the test source folder and locate the file `LayoutClientModuleTest`, from this test class delete the second test case as shown below image as we don't need it anymore,
	
	![image22](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/031.png)
	
    - Also remove the import com.progressoft.domino.sample.layout.client.requests.LayoutServerRequest;

	- Open the layout-frontend `pom.xml` file and remove the dependency on the layout-backend with the test scope.
> ```<!-- remove this -->
 <dependency>
            <groupId>domino.tutorial</groupId>
            <artifactId>layout-backend</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>test</scope>
        </dependency>
        ``` 
        
    - Notice other compilation errors this might be because of the exclusion of the target folder, generated sources are normally excluded from the source path we need to add them as list below,
     -  Rebuild the project.
     - Exclude the target folder.
     - Include the generated-sources/annotations and test-generated-sources/annotations folders as sources directories.
     
- **layout-frontend-ui** : this is where all the UI rendering stuff goes in, Views, UIBinders and CSS all goes into this module, the views in this module should implement interfaces defined within the frontend module and in this module we are going to implement our actual layout we will be using GMD for that.
- **layout-shared** : this is where the classes that are shared between the frontend modules and backend module goes in, interfaces and data structures are in the shared module, the classes that are in this module should follow the rules of a shared GWT package, the actual role of this module is to decouple the frontend from the backend and what's more important is to decouple the front-end and the back-end of this module from other modules. In our current demonstration this is more than what we need, lets clean it up a bit, follow the points below,
	- Open the `layout-shared` module and navigate through the source until you find the **request and response** packages as shown below image, 
	
	![image23](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/032.png)
	
	- Delete both packages.
	
We created a new module but we didn't add it to our application, in order to add it we need to add the `layout-frontend`, `layout-frontend-ui` and `layout-shared` to the `demo-frontend` module dependencies, and since we are dealing with GWT modules we will need to include the source dependencies too.
- Open the `demo-frontend` pom.xml file and paste the following dependencies into the dependencies section :
	```
		    <!-- layout-frontend -->
            <dependency>
                <groupId>domino.tutorial</groupId>
                <artifactId>layout-frontend</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>
            <dependency>
                <groupId>domino.tutorial</groupId>
                <artifactId>layout-frontend</artifactId>
                <version>1.0-SNAPSHOT</version>
                <classifier>sources</classifier>
            </dependency>
            <!-- layout-frontend-ui -->
            <dependency>
                <groupId>domino.tutorial</groupId>
                <artifactId>layout-frontend-ui</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>
            <dependency>
                <groupId>domino.tutorial</groupId>
                <artifactId>layout-frontend-ui</artifactId>
                <version>1.0-SNAPSHOT</version>
                <classifier>sources</classifier>
            </dependency>
            <!-- layout-shared -->
            <dependency>
                <groupId>domino.tutorial</groupId>
                <artifactId>layout-shared</artifactId>
                <version>1.0-SNAPSHOT</version>
            </dependency>
            <dependency>
                <groupId>domino.tutorial</groupId>
                <artifactId>layout-shared</artifactId>
                <version>1.0-SNAPSHOT</version>
                <classifier>sources</classifier>
            </dependency>
```

we are done, now we can test our application and see if the module is working so lets build and run the application again, follow the list below,

 - Open an a  intellij terminal and and execute `mvn clean install`.
 - When the maven task is completed, Click on the **Run** button.
 - Open the browser and point it at `http://localhost:8080`
 
Again the browser page will be a blank page which is normal since we only add an empty module we didn't add any real UI, hit **`F12`**  and look at the console the below logs will appear :

> Sun May 21 12:57:15 GMT+300 2017 com.progressoft.brix.domino.gwt.client.Core
INFO: Initialize domino module...
Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutClientModule
INFO: Initializing Layout frontend module ...
Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutUIClientModule
INFO: Initializing Layout frontend UI module ...
Sun May 21 12:57:16 GMT+300 2017 domino.tutorial.AppClientModule
INFO: Application frontend have been initialized.
Sun May 21 12:57:16 GMT+300 2017 domino.tutorial.layout.client.presenters.DefaultLayoutPresenter
INFO: Main context received at presenter DefaultLayoutPresenter


Take a close look at  below lines,
> Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutClientModule 
INFO: Initializing Layout frontend module … 
Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutUIClientModule 
INFO: Initializing Layout frontend UI module … 
> Sun May 21 12:57:16 GMT+300 2017 domino.tutorial.layout.client.presenters.DefaultLayoutPresenter 
INFO: Main context received at presenter DefaultLayoutPresenter

Above lines tells us two things, the first thing is that the layout-frontend and layout-frontend-ui modules were initialized, and once the application completed the initialization a  context is received at a presenter.


