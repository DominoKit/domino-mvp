<p align="center"><font size="20">Welcome to <img alt="domino"src="https://avatars2.githubusercontent.com/u/28739752?v=3&s=200" width="50"/> Domino! </font></p>
<p align="center">
  <a title="Gitter" href=""><img src="https://badges.gitter.im/Join%20Chat.svg"></a>
</p>

Domino is a small, simple, and  light weighted framework for building applications using  [GWT](http://www.gwtproject.org/)  and [Vertx](http://vertx.io/).  Domino follows the approach of extension points and contributions allowing developers to write a modular application with shared component which can be any other domino application. With vertx as a back-end engine domino gives the ability and choice to build one main application and also provides tools in which a large application is built as a suite of modular services known as micro-services architecture, moreover allows building the application using TDD approach with practices offering an easy and fast way to debug for both client and service side.

**We are still not done yet!** Domino comes with ready to use archetypes, one is for creating a domino application, and the other two creates the modules within a domino application the difference between the last two archetypes is that one of these archetypes comes with GMD [GWT Material Design](https://github.com/GwtMaterialDesign) set-up and ready.

There is still a lot of things to learn about domino please follow the below step by step tutorial that explains and shows the simplicity of domino and how to use it.

#### **Table of Contents**
* [Task 1 : Create your first domino application](#Task1)
* [Task 2: Run the application](#Task2)
* [Task 3: Creating a new domino module](#Task3)

##### **Task 1 : Create your first domino application** <a name="Task1"></a>
*For this tutorial we are going to use [Intellij](https://www.jetbrains.com/idea/) IDE and make sure your JDK is 1.8*

- After cloning the Domino repository change the directory to the Domino project then execute mvn clean install goal before starting the steps below.
- Open intellij and press on Create New Project.

![image1](https://raw.githubusercontent.com/GwtDomino/domino/0d80397597737c14f08032856adc6cba36b188a7/documents/012.png)

- We are going to create a new Maven project using the domino application archetype, a window will pop up make sure of the below values:
    - step 1: maven.
    - step 2: make sure of the project JDK to be 1.8.
    - step 3: tick create from archetype.
    - step 4: click on add archetype.
    
    ![image2](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/013.png)
- After clicking on add archetype button, another window will pop up fill it with the following values: 
  - GroupId: com.progressoft.brix.domino.archetypes
  - ArtifactId: domino-gwt-app-archetype
  - Version: 1.0-rc.2-SNAPSHOT
  
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
![image10](https://raw.githubusercontent.com/GwtDomino/domino/master/documents/)
If you had noticed your project is a multi module maven project that contains two modules a back-end module and a front-end module both modules are described below,

##### Back-end module
Back-end module contains all what we need to build an application it produce deployable artifacts for example static resources, configuration files, assemblies and dependencies but not the **source code**, one of the great benefits of domino is that applications are modular applications and module dependencies can be added to the back-end module. The final output will be provided from the back-end, which means that when you build a domino application you will see the output in the **`target`** folder of the back-end module.

##### Frond-end module
Same as the back-end module we don't add any source code directly to this module, front-end module contains a single class that acts as a main class for the client side code, this single class is a normal GWT entry point with predefined logic that runs our client side application, we add the GWT client side modules to the front-end module as dependencies. When building the application front-end module will be compiled into JavaScript and the output will be automatically placed into the **`webroot`** folder of the back-end module.

> For GWT we use `Thomas Broyer` [maven plugin](https://github.com/tbroyer/gwt-maven-plugin)  which automatically detect GWT module within any maven dependency and adds the required inherits tags in the `.gwt.xml` files.

> The idea of separating client side **Front-end** code and server side **Back-end** code each in a separate module was from `Thomas Broyer` [GWT archetype](https://github.com/tbroyer/gwt-maven-archetypes)

##### **Task 2 : Run the application** <a name="Task2"></a>
Note that before starting we need to build the application first, open a terminal in Intellij and type `mvn clean install` goal, this goal will trigger the GWT compiler,  after building the application successfully you will notice a new folder created inside `webroot` folder in the back-end module this new folder is the result of GWT compilation, also you will notice new jars, as domino produces jars not wars, in the target folder of the back-end module focus on the fat jar as it represents the final output.

*File `how-to.txt` lists full instructions of how to run a domino application but we will go through them quickly below.*

- Go to your intellij menu and select Run -> Edit configuration.

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

##### **Task 3 : Creating a new domino module** <a name="Task3"></a>
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
