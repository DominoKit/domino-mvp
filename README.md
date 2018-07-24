# This documentation is outdated and does not reflect latest domino api, but we promise it will updated very very soon. Sorry for that.


## Welcome to ![logoimage](https://raw.githubusercontent.com/DominoKit/domino/master/documents/projectavatar.png) Domino

<a title="Gitter" href="https://gitter.im/domino-gwt/Domino"><img src="https://badges.gitter.im/Join%20Chat.svg"></a>
[![Build Status](https://travis-ci.org/DominoKit/domino.svg?branch=master)](https://travis-ci.org/DominoKit/domino)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dominokit.domino/domino/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.dominokit.domino/domino)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/bfdb8283919a4adab6cbfeeb3a22e53a)](https://www.codacy.com/app/akabme/domino?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=DominoKit/domino&amp;utm_campaign=Badge_Grade)

Our Domino is a small, simple and a very light framework for building applications using [GWT](http://www.gwtproject.org/) and [Vertx](http://vertx.io/), it introduce the concept of extension points and listeners allowing developers to write a modular application and shared components with any other domino application. The main benefit of using Vertx as a back-end, it gives the ability of choosing either to build one monolithic but yet a modular application, or a large application built as micro-services, moreover allows building the application using TDD approach with practices offering an easy and fast way to debug for both client and service side.

**We are still not done yet!** Domino comes with ready to use archetypes, one is for creating a domino application, and the other two creates the modules within a domino application.

*The difference between the last two archetypes is that one of these archetypes comes with GMD [GWT Material Design](https://github.com/GwtMaterialDesign) set-up and ready.*

## **Table of Contents**
* [Domino Archetypes](#Task0)
* [Task 1 : Create your first domino application](#Task1)
* [Task 2: Run the application](#Task2)
* [Task 3: Creating a new domino module](#Task3)
* [Task 4: Implementing UI layout](#Task4) 
* [Task 5: Introducing new extension points](#Task5)

#### **Domino Archetypes**<a name="Task0"></a>:		

### Domino Application:

 Gruop Id : `org.dominokit.domino.archetypes`  

 Artifact Id  : `domino-gwt-app-archetype`  

 Version      : `1.0-rc.1`

### Domino Module:

 Gruop Id : `org.dominokit.domino.archetypes`  

 Artifact Id  : `domino-gwt-module-archetype`  

 Version      : `1.0-rc.1`

### Domino module with GMD:

 Gruop Id : `org.dominokit.domino.archetypes`  

 Artifact Id  : `domino-material-module-archetype`  

 Version      : `1.0-rc.1`
	
	
There is a lot of things to learn about domino please follow the below step by step tutorial that explains and show the simplicity of domino and how to use it.

#### **Task 1 : Create your first domino application** <a name="Task1"></a>
For this tutorial we are going to implement a very simple domino application that will allow looking up emails from a server, as shown below image,

![enter image description here](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/050_contact_ui.gif)

*We will be using [Intellij](https://www.jetbrains.com/idea/) IDE and make sure your JDK is 1.8 or higher*
			
Let's start by creating a new project.

- Open intellij and press on **Create New Project**.

 ![image1](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/012.png)

- We are going to create a new Maven project using the domino application archetype, a window will pop up make sure to follow the below values as shown below image,

  - Step 1: maven.
  - Step 2: make sure of the project JDK to be 1.8.
  - Step 3: check the create from archetype checkbox.
  - Step 4: click on add archetype.

 ![image2](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/013.png)

- After clicking on add archetype button, another window will pop up fill it with the below values,

  - GroupId: `org.dominokit.domino.archetypes`
  - ArtifactId: `domino-gwt-app-archetype`
  - Version: `1.0-rc.1`

![image3](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/014.png)

*After filling up the popped window click on the **OK** button.*
- Choose the created archetype and click **Next**.

![image5](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/015.png)

- Fill the dialog as shown below image and click **Next**.			

![image6](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/016.png)

- Continue by clicking **Next**.		

- Fill the dialog as shown below image and click **Finish**.	

![image8](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/018.png)

- Now the project is starting up, click on **enable auto import** at the bottom right corner of the screen *if appeared* as shown below image, and wait until Maven tasks are completed.

![image9](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/019.png)

When Maven completes the tasks a new project will be created, we now have a domino application ready to run and can add new modules to it, but before adding any new module let's have a look at the project structure as shown below image,

![image10](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/004_domino_app_structure.png)

If you noticed that your project is a multi module maven project that contains two modules a back-end module and a front-end module both modules are described below,

##### Back-end module	
The final output from any domino application will be provided from the back-end module, which means when you build a domino application you will only be interested in the output found under the **`target`** folder of the back-end module, the back-end module contains all what we need to build our application and produce deploy-able artifacts, like static resources, configuration files, assemblies and dependencies, **but not the source code**  we are not supposed to create classes or packages in the back-end module, because we are building a modular application and our code should be within the domino modules,these modules are added to the back-end module as dependencies. 

##### Front-end module
Same concept as the back-end module we don't add any source code directly to this module, front-end module contains a single class that acts as a main class for the client side code, this class is a normal GWT entry point with predefined logic that runs the client side application, we add the GWT client side modules to the front-end module as dependencies and when building the application front-end module will be compiled into JavaScript the output will be automatically placed into the **`webroot`** folder of the back-end module.

> For GWT we use `Thomas Broyer` [maven plugin](https://github.com/tbroyer/gwt-maven-plugin)  which automatically detect GWT module within any maven dependency and adds the required tags in the `.gwt.xml` files.

> The idea of separating client side Front-end code and the server side Back-end code, was actually inspired from `Thomas Broyer` [GWT archetype](https://github.com/tbroyer/gwt-maven-archetypes).

#### **Task 2 : Run the application** <a name="Task2"></a>
Before running the application we need to build it, open a terminal in Intellij and execute `mvn clean install` goal, this goal will trigger the GWT compiler,  after building the application successfully you will notice a new folder created inside `webroot` found in the back-end module, this new folder is the result of GWT compilation, also you will notice new jars, as domino produces jars not wars, in the target folder of the back-end module, focus on the fat jar as it represents the final output.

*The file `how-to.txt` lists the full instructions of how to run a domino application but we will go through them quickly below.*

- From Intellij menu select Run -> Edit configuration.

![image11](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/020.png)

- A window will pop up click on the green add button and select `Application` from the list.

![image12](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/021.png)

- Another window will pop up fill it as below,

    - Fill a name for the configuration.
    - Fill the Main class with : `org.dominokit.domino.api.server.DominoLauncher`
    - Fill the program arguments with : `run org.dominokit.domino.api.server.StartupVerticle -conf target/classes/config.json`
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

> Sun May 21 01:23:58 GMT+300 2017 Core
INFO: Initialize domino module...
Sun May 21 01:23:58 GMT+300 2017 domino.tutorial.AppClientModule
INFO: Application frontend have been initialized.

### **Task 3 : Creating a new domino module** <a name="Task3"></a>
*As mentioned before we don't add any code to the back-end or front-end modules, in order for us to add a simple layout to the home page we need to add a new domino module.*

Below steps will help you to add a new domino module with a simple layout,	

- Right click on the **`domino-demo`** that is the main project the root level of the application, and select **New -> Module**.

![image15](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/024.png)

![image16](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/025.png)

- We are adding a new archetype as explained above, click on add a archetype.
*Note: make sure you have picked maven module and the create from archetype checkbox is checked as described above task.*
- A window will pop up fill it with the following values: 

  - GroupId: org.dominokit.domino.archetypes
  - ArtifactId: domino-material-module-archetype
  - Version: 1.0-rc.1

- Choose the created archetype and click **Next**.
- In the Artifact Id set the value to `layout` and click **Next**.

![image17](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/026.png)

- Click on the green add button ![enter image description here](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/007_add_button.png) and add the below values : 

    - Name : module, Value : Layout **notice the uppercase L**.
    - Name : subpackage, Value : layout  **notice the lowercase l**.
  
![image18](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/027.png)
![image19](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/028.png)

- Keep on clicking **Next** until you finish the wizard.
- Wait until the maven task is completed then the new module should be created.
- Take a look at the project structure after the creation of the new module as shown below image:

![image20](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/029.png)

The layout module is actually a multi module maven project, below describes the structure of the module :

-  **layout-backend** : this is where the server code goes, open the module you will notice that there is some classes already created for example the `package-info` class this class defines the server module used by **APT** -Annotation Processing Tool- to generate a server module configuration, you will also find the `handlers` package that contains a handler class, this class is for when sending a request from the client to the server you will receive the request in such handler.

layout-backend module is not needed for this sample, can be deleted easily as listed below,

* Right click on the `layout-backend` module and select **Remove module**
* Right click on the `layout-backend` module and select **Delete**. 
* Open the `layout` module `pom.xml` file and remove the `layout-backend` module from the modules list.

		<modules>
		     <module>layout-frontend</module>
		     <module>layout-frontend-ui</module>
		     <module>layout-shared</module>
		     <module>layout-backend</module> <!-- remove this line -->
		</modules> 
		 
- **layout-frontend** : this is where all the client side flow goes in, in this module we use APT to generate a client module configuration, the interaction with other modules, listeners to extension points, sending requests, all client side logic starts from here. Never add any UI rendering code, we don't force you but in this module you should never have buttons, text fields, check boxes etc ... 

*We encourage that your flow should be independent from any UI presentation.*

Open the module you will notice a few already created classes, in some cases this might be all what we need but in our current demonstration this is more than what we need, lets clean it up a bit, follow the points below,

* Open the `layout-frontend` module and navigate through the source until you find the **requests** package as shown below image,
	
	![image21](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/030.png)
	
* We have deleted the backend module which means we don't need to send any requests to the server, right click on the request package and delete it.
* Deleting the request package will produce compilation error, navigate to the test source folder and locate the `LayoutClientModuleTest` class, from this test class delete the second test case as shown below image as we don't need it anymore,
	
	![image22](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/031.png)
	
* Also remove the import *org.dominokit..domino.sample.layout.client.requests.LayoutServerRequest;*.

* Open the layout-frontend `pom.xml` file and remove the dependency on the layout-backend with the test scope.

```
<!-- remove this -->
 <dependency>
            <groupId>domino.tutorial</groupId>
            <artifactId>layout-backend</artifactId>
            <version>1.0-SNAPSHOT</version>
            <scope>test</scope>
        </dependency> 
``` 
Notice other compilation errors, this might be because of the exclusion of the target folder, generated sources are normally excluded from the source path we need to add them, follow the below points,
           
* Rebuild the project.    
* Exclude the target folder.    
* Include the generated-sources/annotations and test-generated-sources/annotations folders as sources directories.

- **layout-frontend-ui** : this is where all the UI rendering goes in, Views, UIBinders and CSS all goes into this module, the views in this module should implement interfaces defined within the frontend module and in this module we are going to implement our actual layout we will be using GMD.
- **layout-shared** : this is where the classes that are shared between the frontend modules and backend module are placed, interfaces and data structures are in the shared module, the classes that are in this module should follow the rules of a shared GWT package, the actual role of this module is to decouple the frontend from the backend and what's more important is to decouple the front-end and the back-end of this module from other modules. In our current demonstration this is more than what we need, lets clean it up a bit, follow the points below,
	- Open the `layout-shared` module and navigate through the source until you find the **request and response** packages as shown below image, 
	
	![image23](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/032.png)
	
	- Delete both packages.
	
We had created a new module but we didn't add it to our application, in order to add it we need to add the `layout-frontend`, `layout-frontend-ui` and `layout-shared` to the `demo-frontend` module dependencies, and since we are dealing with GWT modules we will need to include the source dependencies too.

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
We are done, now we can test our application and see if the module is working, let's build and run the application again, follow the list below,

 - Open an a  Intellij terminal and and execute `mvn clean install` goal.
 - When the maven task is completed, Click on the **Run** button.
 - Open the browser and point it at `http://localhost:8080`
 
Again the browser page will be a blank page which is normal since we had only add an empty module we didn't add any UI, hit **`F12`**  and look at the console the below logs will appear :

> Sun May 21 12:57:15 GMT+300 2017 Core
INFO: Initialize domino module...
Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutClientModule
INFO: Initializing Layout frontend module ...
Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutUIClientModule
INFO: Initializing Layout frontend UI module ...
Sun May 21 12:57:16 GMT+300 2017 domino.tutorial.AppClientModule
INFO: Application frontend have been initialized.
Sun May 21 12:57:16 GMT+300 2017 domino.tutorial.layout.client.presenters.DefaultLayoutPresenter
INFO: Main context received at presenter Underrepresented

Take a close look at the below lines,
> Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutClientModule 
INFO: Initializing Layout frontend module … 
Sun May 21 12:57:15 GMT+300 2017 domino.tutorial.layout.client.LayoutUIClientModule 
INFO: Initializing Layout frontend UI module … 
> Sun May 21 12:57:16 GMT+300 2017 domino.tutorial.layout.client.presenters.DefaultLayoutPresenter 
INFO: Main context received at presenter DefaultLayoutPresenter

Above lines tells us two things, the layout-frontend and layout-frontend-ui modules were initialized, and once the application completed the initialization a context is received at a presenter.

#### A blank page?
When running an empty domino application and the result would be a blank page, it doesn't mean that the application is completely empty, any domino application starts with one and only one extension point, which is the **Main Extension Point** and this extension point provides a context, this context is surprisingly empty, it does nothing , it's just an empty interface but an important one, you start building your application by adding modules that contributes to this extension point and these modules might also provide additional extension points allowing modules to make more listeners, receiving the main context when contributing to the main extension point also means that all other modules in the application have already been initialized and configured.

#### Extension point and Extension point context?
The extension point is nothing but a simple interface and it's only job is to provide a context to the contributors, it's simply a type that represent a point at the application that can be extended. The important part is the extension point context, both the extension point and the extension point context interfaces lives inside the **-shared module** in our case **layout-shared**, the implementation of these interfaces goes into the  **-frontend module** in our case **layout-frontend** and it's the frontend module responsibility to deliver the context implementation to the contributing modules within an API in the right time, this API is specifically made for this action , will see these details later on in this tutorial.

#### Contribution?
Contribution is the process of obtaining an extension point context, as simple as it seems, when a module wants to obtain a context of an extension point it will only register itself as a contributor then it will get a context instance, let's see how we obtained the MainContext of the MainExtensionPoint in our layout-frontend module,

- Open the interface **LayoutPresenter**, you are going to find it as shown below,

```java
    @InjectContext(dominoEvent=MainExtensionPoint.class)
    void contributeToMainModule(MainContext context);
```
The above method only needs a MainContext from the MainExtensionPoint in order to inject it, this method should be implemented within our presenter and we will receive the context at the write time, in our case the context will be provided when all modules finish initialization, this is defined by the domino core.

- Open the **DefaultLayoutPresenter** class and check the implementation of the above method, it should be as shown below,

```java
@Override
    public void contributeToMainModule(MainContext context){
        LOGGER.info("Main context received at presenter "+DefaultLayoutPresenter.class.getSimpleName());
    }
```

Well! by default we do nothing, we only log that we had received the context, but this is where we start to add our logic.

By now you might be wondering if we have any test cases to verify that our code is tested and working in the right way, yes we do we have a test for this listener, as long as this test passes you should know that this code is right and will work on the browser too, have a look at the below test,

- Open the **LayoutClientModuleTest** class, below is the test case,

```java
@Test
public void givenLayoutModule_whenContributingToMainExtensionPoint_thenShouldReceiveMainContext() {
     assertNotNull(presenterSpy.getMainContext());
}
```
The above test case verify that if we contribute with the MainExtensionPoint we are going to receive a MainContext, but the interesting part is not the test case, it's the setup method, notice that the test class extends from the **ModuleTestCase** class and the setup method prepares a complete client application with all the required classes, also fakes and introduce hook methods to be implemented by the extended test class, which concludes that our setup method is nothing but a hook to configure the module under testing, we also replace the configured presenter and view with spy's to verify that our calls are happening in the right order, please run the test case and see what happens, you should get a green bar indicating the success of the test case, what's more interesting is that this test class is a normal test class, not a GWT specific type of test classes, and there is no special runners to run it, the test cases runs very fast, we write our client side and test it as if it's any normal java code, which means we can also debug the code as if it is any normal java code from IDE instead of the browser.  

*Browser debugging is still an available option with GWT code server.*

In our next task we are going to start implementing an actual UI layout, we will start to see a real UI rather than the usual blank page.

### **Task 4 : Implementing UI layout** <a name="Task4"></a>

In this task we are going to implement the actual UI, we will learn how to start and use the GWT super dev mode in order to reduce the time required for the GWT compilation when changing some code in the **frontend** and **frontend-ui** modules.

What we are going to do now is to call our view by show() method directly after receiving a main context from the main extension point, we encourage to use the TDD approach that's why we will start with a test case to ensure that the show method is being called when receiving the main context, but remember we already have a test case that ensures we had received a context, we are not going to test that again.

> Domino does not reference any GWT UI framework, actually you are free to use any, it's up to you to decide how you want to build your UI, it even encourage and allow the use of any and many UI frameworks, what we had came up to is an archetype with a default GMD setup that's easy to use.

Follow the below steps that will guide you in implementing an UI,

- Open the `LayoutClientModuleTest` class found under the **front-end** module and add the below test case,

```java
@Test
public void givenMainContextReceived_thenLayoutViewShouldBecomeVisible() {
   assertTrue(viewSpy.isVisible());
}
```
When adding the above code you will get a compilation error, you can resolve the assertTrue error by importing the required class, but notice that the isVisible method of the viewSpy is not implemented yet, let us implement it as a getter for the visible field in the viewSpy class called `LayoutViewSpy` found under the **front-end** module as shown below,

```java
@UiView(presentable=LayoutPresenter.class)
public class LayoutViewSpy implements LayoutView {

    private boolean visible;

    @Override
    public IsWidget get() {
        return null;
    }

    public boolean isVisible() {
        return visible;
    }
}
```
Our test case will definitely fail, run it and notice the red bar indicating the test failure, what we can do in order to pass this test case is to call a method in our view and spy on it, if the method is called we change the visible flag in the viewspy to true as shown below,

- Open the `DefaultLayoutPresenter` class found under the **front-end** module and change the `onMainContextReceived` method implementation to the below implementation,

```java
@Override
public void contributeToMainModule(MainContext context){
	view.show();
}
```

the show() method is not in the view interface, we have to add it in the `LayoutView` interface found under the **front-end** module as shown below,

```java
public interface LayoutView extends View<IsWidget>{
    void show();
}
```
After we add the show() method to the interface we need to implement it in all our view implementations as explained below,

- Open the `DefaultLayoutView` class found under the **front-end-ui** module and implement the show method, you don't need to add any body. 
- Open the `LayoutViewSpy` class found under the **front-end** module and implement the show method, set the visible flag to true in the method body as shown below,

```java
@UiView(presentable=LayoutPresenter.class)
public class LayoutViewSpy implements LayoutView {

    private boolean visible;

    @Override
    public IsWidget get() {
        return null;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void show() {
        this.visible=true;
    }
}
```

Let's go back and run the test again, this time we should see a green bar indicating the success of our test case, this means that our flow is working as intended, but notice that we only test if the show method is being called we don't test if it is actually attached to the DOM or how it is being added to the page, this part is up to the actual view implementation.

Next we are going to add a simple layout to our page with a header and a body as explained below,

- Open the `DefaultLayoutView.ui.xml` found under the **front-end-ui**, remove the `div` tag and replace it with the following,

```xml
<m:MaterialHeader depth="996" layoutPosition="FIXED" width="100%">
    <m:MaterialNavBar ui:field="appNav" backgroundColor="BLUE_LIGHTEN_1">
	    <m:MaterialNavBrand marginLeft="20" text="Demo"/>
	</m:MaterialNavBar>
</m:MaterialHeader>
<m:MaterialPanel ui:field="mainPanel">
</m:MaterialPanel>
```

- Open the `DefaultLayoutView` class found under **front-end-ui** module, and remove the `mainDiv` field.
- In the show method body, set add the view class to the RootPanel, the final result should be as below,
 
```java
@UiView(presentable = LayoutPresenter.class)
public class DefaultLayoutView extends Composite implements LayoutView{

    interface DefaultLayoutViewUiBinder extends UiBinder<HTMLPanel, DefaultLayoutView> {
    }

    private static DefaultLayoutViewUiBinder ourUiBinder = GWT.create(DefaultLayoutViewUiBinder.class);

    public DefaultLayoutView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public IsWidget get() {
        return this;
    }

    @Override
    public void show() {
        RootPanel.get().add(this);
    }
}
```
##### **What about testing here?**
We already have a test class ready to start testing, it uses GWTMokito to run tests, we don't want to go through faking and testing the UI we don't force it.

Now we're done, the layout is added to our blank page which means that our page is not blank anymore. Execute **mvn clean install** goal then run the application, open any browser and visit **http://localhost:8080**

> In case the page is still blank press `CTRL+F5`.

The page would look like the below image,

![enter image description here](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/010_demo_layout_first_run.png)


Things are going extremely well, but do we need to build the application each and every time we change something in the UI?! **No** we use GWT super dev mode!

follow the below steps in order to configure GWT super dev mode in your application,

- Open the how-to.txt file and copy the below command from the second step.

```shell
mvn gwt:codeserver -pl *-frontend -am
```
- Paste the command in any Intellij terminal and execute it.
- Open the folder `.vertx` found under the domino-demo-backend module and delete it.
- Wait until the code server is ready, then go back to the browser and hit **CTRL+F5**.
- A compilation will be triggered in the browser, wait until the compilation is done, then we are ready to go.

Now let's change something and see what happens

- Open **DefaultLaoutView.ui.xml** and change the Material navigation bar `MaterialNavBar` background color from `BLUE_LIGHTEN_1` to `RED_LIGHTEN_1`, and hit CTRL+F5 on the browser.
- A quick compilation will be triggered, the header should be red instead of blue.

### **Task 5 : Introducing new extension points** <a name="Task5"></a>

The layout is ready, but we need to make a use of it by showing more components at the main panel of the layout, we do this in a clean way, which means we never depend on a concrete implementation of the layout and we don't add any on the **layout-frontend** module or **layout-frontend-ui** module, we want to add these components without changing the implementation of the layout module.   

When we don't depend on the actual implementation of the **layout-frontend** module, it allows us to change our layout implementation anytime, without worrying about breaking other modules that uses the layout, this happens by introducing new extension points, the module that use the layout would depend on the **layout-shared** module that contains interfaces and data-structures only, the **layout-shared** module contains an extension point interface and a context interface ready to use, keep in mind that the context is still empty other modules will fill in the context in order to show content at the main panel of the layout.

We will start by adding the API in the `LayoutContext` , but at first we will do a test case for it as described below steps,

- Open the `LayoutClientModuleTest` test class found under the **layout-frontend** module and add the below test case,

```java
@Test
public void givenLayoutModule_whenContributingToLayoutExtensionPoint_shouldObtainLayoutContext(){
    assertNotNull(fakeContribution.getLayoutContext());
}
```
The fakeContribution is to confirm that there is a listener from a module to the layout extension point in which it will receive a layout context, notice that the fakeContribution does not compile because we didn't create a fake listener yet, below steps will guide you through creating a fake contributor, 

- Create a new class in the test source folder that implements `Contribution` interface under the **layout-frontend** module as shown below,
```java
@Contribute
public class FakeLayoutContribution implements Contribution<LayoutExtensionPoint> {

    private LayoutContext layoutContext;

    @Override
    public void contribute(LayoutExtensionPoint dominoEvent) {
        this.layoutContext=dominoEvent.context();
    }

    public LayoutContext getLayoutContext() {
        return layoutContext;
    }
}
```
*In this way we obtain a context from an extension point, if you are wondering about the `InjectContext` annotation we had used before, we used it to generate a class the same as the above class, and then we deliver the context to the presenter, check the generated classes and look at the `target/generated-sources/annotation` folder of the `layout-frontend` module.*

**let's continue!** The test case is still not compiling, we still need to create the fakeContribution instance, or to be more specific we need to obtain that instance since we annotated the class with `@Contribute` and the instance will be automatically created and configured as part of our test module, follow the below steps in order to compile our code,

- In the `LayoutClientModuleTest` class found under the **layout-frontend** module, create a new field as shown below,

```java
private FakeLayoutContribution fakeContribution;
```
- In the setup method add the below line at the end of the method,

```java
fakeContribution=testModule.getContribution(FakeLayoutContribution.class);
```

**Now the code compiles but yet a failing test case!**

> Run the test case if you end up with an exception of a view not found, it means that you need to rebuild your project in order for the APT to generate the required code, from Intellij menu select Build -> rebuild 
project, then run the test case again.

We need to pass our test case, we need to deliver a LayoutContext implementation to the contributors, but before that we need to decide when to do it before how to do it.

**When to do it?** 

The right time is after we add the layout, when it's ready to hold the content and to display them on the page, and that is exactly after we call the show method in our presenter.

**How to do it?**

- Open the `DefaultLayoutPresenter` class found under the  **layout-frontend** module, and add the below code right after the **view.show()** line,

```java
applyContributions(LayoutExtensionPoint.class, (LayoutExtensionPoint) () -> new LayoutContext() {});
```

Run the test case again, you should get a green bar indicating the success of the test case, this means that we made sure if any module contributes to our extension point will receive a context.

Now we need to show the content in the main panel, follow the below steps,

- Open the `LayoutClientModuleTest` class found under the **layout-frontend** module and add the below test case,

```java
@Test
    public void givenContributionToLayoutExtensionPoint_whenContextShowContentIsCalled_theContentShouldShowTheContent(){
        LayoutContext.Content content= () -> null;
        fakeContribution.getLayoutContext().showContent(content);
        assertEquals(viewSpy.getContent(), content);
    }
```
*Again the test case won't compile, we have to add the code that compiles it*

- Open the `LayoutContext` interface found under the **layout-shared** module and change it as shown below,

```java
public interface LayoutContext extends Context {
    interface Content extends IsWidget {}
    void showContent(Content content);
}
```
- Open the `LayoutViewSpy` class found under the **layout-frontend** module, add  the `content` field with  a getter method.

```java
@UiView(presentable=LayoutPresenter.class)
public class LayoutViewSpy implements LayoutView {

    private boolean visible;
    private LayoutContext.Content content;

    @Override
    public IsWidget get() {
        return null;
    }

    public boolean isVisible() {
        return visible;
    }

    @Override
    public void show() {
        this.visible=true;
    }

    public LayoutContext.Content getContent() {
        return content;
    }
}
```
- Open the `DefaultLayoutPresenter` class found under the **layout-frontend** module, and fill the **contributeToMainModule** method with the below code in order to reflect the changes in the LayoutContext interface,

```java
applyContributions(LayoutExtensionPoint.class, (LayoutExtensionPoint) () -> (LayoutContext) content -> {});
```
**The test case compiles but fails with a null pointer exception since we didn't set the content in the spy.**

- Open the default presenter class called `DefaultLayoutPresenter` found under the **layout-frontend** module and update it as shown below,

```java
@Presenter
public class DefaultLayoutPresenter extends BaseClientPresenter<LayoutView> implements LayoutPresenter {

    @Override
    public void contributeToMainModule(MainContext context){
        view.show();
        applyContributions(LayoutExtensionPoint.class, (LayoutExtensionPoint) () ->(LayoutContext) content -> {
            view.showContent(content);
        });
    }
}
```
The code won't compile, add the **showContent** method to the view interface called `LayoutView` found under the **layout-frontend** module as shown below,

```java
public interface LayoutView extends View<IsWidget>{
    void show();
    void showContent(LayoutContext.Content content);
}
```
- Open the `LayoutViewSpy` class found under the **layout-frontend** module and implement the method as shown below,

```java
@Override
public void showContent(LayoutContext.Content content) {
    this.content=content;
}
```
- Open the `DefaultLayoutView` class found under the **layout-frontend-ui** module, add an empty implementation for the showContent method.

**Rebuild** the project and run the test case again, see a green bar indicating the success of the test case.

>Good Practice: run all the test cases in the test class when changing the code to make sure that we didn't break any tests.

One thing left, is to implement the actual **showContent** in the actual layout view, as shown below steps,

- Open the `DefaultLayoutView` class found under the **layout-frontend-ui** module, add the below field,

```java
@UiField
MaterialPanel mainPanel;
```

- Fill the **showContent** method body with the below code,

```java
mainPanel.clear();
mainPanel.add(content);
```

We are done, if you still have the code server and the application running just hit the browser, there is no changes on the UI, but to make sure that the application is working correctly.

