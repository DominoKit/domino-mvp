### Presenters

Presenters are the core of a domino-mvp application, they control the page life-cycle and holds most of our business logic, they control routing and navigation, fire events, make REST calls, do the validation and drive the view.

Domino-mvp provide tow main types of presenters, the passive or simple presenters and the viewable presenters.

#### Passive or Simple presenters

Those presenters are not linked with a view or any UI elements, they are just classes that can be controlled by routing but, they will work in the background to do some tasks, they do not control any view life-cycle but, they can listen to events, make calls to the server and manipulate the navigation tokens. such presenters are not common, and we can create such a presenter by extending `BaseClientPresenter` and annotate the class with `@Presenter`

```java
@Presenter
public class SimplePresenter extends BaseClientPresenter {
  @Override
  protected void onActivated() {
    super.onActivated();
  }

  @Override
  protected void onDeactivated() {

  }

  @Override
  public Optional<String> getName() {
    return Optional.of("simplePresenter");
  }

}
```

The only interesting super methods in such a presenter are the `postConstruct` and `onActivated` methods, those methods are coupled to the presenter life-cycle in domino-mvp, for such a presenter the life-cycle is the following :

1. Some routing happens that requires the presenter to be activated.
   > We will discuss more about how presenters can be activated later.

2. A new instance of the presenter will be created
   > Presenters can be marked as singletons, singleton presenters will utilize any already created instance.

3. Call `postConstruct` method. - For singleton presenter this will be called only once when we create the first instance -

4. Register presenter events listeners.
5. Fire presenter state event if such event is present. - we can make the presenter fire an event when ever its (de)activated -
6. If the presenter has a name we register that presenter name in an internal registry.
7. Call `onActivated` method.
8. Later presenter is deactivated.
9. Call `onDeactivated`

#### Viewable presenters

Viewable presenters are those that are linked with a UI view, both the presenter and the view share the life-cycle, when a presenter is activated the view will be revealed and if the view is removed the presenter will be deactivated,we define such presenter bye extending from the `ViewBaseClientPresenter` and specify the view in the generic type.

```java
@Presenter
public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {
  @Override
  protected void postConstruct() {
  }

  @Override
  protected void onActivated() {
  }

  @Override
  protected void onDeactivated() {

  }

  @Override
  protected void onBeforeReveal() {
  }

  @Override
  protected RevealedHandler getRevealHandler() {
    return () -> {
      //do something when view revealed
    };
  }

  @Override
  protected RemovedHandler getRemoveHandler() {
    return () -> {
      //do something when view is removed
    };
  }

  @Override
  public Optional<String> getName() {
    return Optional.of("simpleViewPresenter");
  }

}
```

In a viewable presenter the generic type is the type-of an interface that extends from `View` which represent the contract between the presenter and its view, notice that the presenter does not know about the implementation of view and the framework will inject the view implementation into the presenter at runtime.

     We will discuss views in details in later parts if the documentation.

In addition to the change in base class and the generic type we notice that we have few more method that we can override here and those methods are coupled to the life-cycle of the presenter and the view together, and the life-cycle is as the following :

1. A routing happens that requires the presenter to be activated.
2. A new instance of the presenter is created.
3. A new instance of the view is created and injected into the presenter.
4. Call `postConstruct`
5. Register presenter events listeners.
6. Fire presenter state event if such event is present. - we can make the presenter fire an event when ever its (de)activated -
7. If the presenter has a name we register that presenter name in an internal registry.
8. View is about to be revealed.
   > We will explain more about revealing views as part of this documentation.
9. Call `onBeforeReveal`.
10. View is revealed.
11. Call the `onRevealHandler`.
12. Later view is removed.
13. Call `onRemoveHandler`
14. Call `onDeactivated`

#### Presenter proxy

Meanwhile manually overriding the presenters methods works and flixable yet it is has boilerplate code and requires a lot of knowledge about the super classes and how they work, to overcome that Domino-mvp provide what is called a presenter proxy, which a class that extends from the base presenters but instead of annotating the class with `@Presenter` with annotate if with `@PressenterProxy`, once we have this annotation on the class we can use a declarative annotations to inject our code inside an auto generated presenter, this will reduce the code we need to write and also makes our presenters code more readable, but also add some interesting feature as a built-in part of the framework that we would have implement manually without the proxy.

Following is a simple persenter proxy :

```java

@PresenterProxy
public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

}

```

This looks very similar to a normal presenter, and if we look at the generated code we wont see lots of differences between the proxy and the presenter:

```java
/**
 * This is a generated class, please don't modify
 */
@Presenter(
    name = "",
    parent = ""
)
public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {

}

```

Notice that the generated presenter is extending from the proxy, but then if we modify the proxy by setting the `name` and `parent` values like this :

```java
@PresenterProxy(name = "simpleViewPresenter", parent = "shell")
public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

}
```
Then the generated presenter will look like this :

```java

/**
 * This is a generated class, please don't modify
 */
@Presenter(
    name = "simpleViewPresenter",
    parent = "shell"
)
public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
  @Override
  public Optional<String> getName() {
    return Optional.of("simpleViewPresenter");
  }

  @Override
  public Optional<String> getParent() {
    return Optional.of("shell");
  }
}
```

Now you can see the `getName` and `getParent` methods are both auto generated, And next we will show how we can define method and bind them to the presenter life-cycle using proxy declarative style :


- #### **@PostConstruct**

  In a presenter proxy we can annotate as many methods with `@PostConstruct` and they will be called during the presenter post construct life cycle stage.

  Example :

     ```java
     import javax.annotation.PostConstruct;

     @PresenterProxy()
     public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

         @PostConstruct
         public void doSomething(){
             //Do something here
         }

         @PostConstruct
         public void doSomethingElse(){
             //Do another thing here
         }
     }

     ```

  Will generate :

     ```java
     /**
      * This is a generated class, please don't modify
      */
     @Presenter(
         name = "",
         parent = ""
     )
     public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
       @Override
       protected void postConstruct() {
         doSomething();
         doSomethingElse();
       }
     }
     ```

  Notice how the annotated methods are being called, and the order of the methods calls is the same as hiw they appear in the proxy, also in those methods the view instance will be already created and can be accessed even though it is not yet revealed, for example you can call a method in the view `view.doSomeUiStuff()`.



- #### **@OnInit**

  In a presenter proxy we can annotate as many methods with `@OnInit` and they will be called during the presenter activation life cycle stage.

  Example :

     ```java
     import org.dominokit.domino.api.client.annotations.presenter.OnInit;

     @PresenterProxy()
     public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

         @OnInit
         public void doSomething(){
             //Do something here
         }

         @OnInit
         public void doSomethingElse(){
             //Do another thing here
         }
     }
     ```

  Will generate :

     ```java
     /**
      * This is a generated class, please don't modify
      */
     @Presenter(
         name = "",
         parent = ""
     )
     public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
       @Override
       protected void onActivated() {
         doSomething();
         doSomethingElse();
       }
     }
     ```

  Notice how the annotated methods are being called, and the order of the methods calls is the same as hiw they appear in the proxy, also in those methods the view instance will be already created and can be accessed even though it is not yet revealed, for example you can call a method in the view `view.doSomeUiStuff()`.


- #### **OnBeforeReveal**

  In a presenter proxy we can annotate as many methods with `@OnBeforeReveal` and they will be called during the presenter before reveal life cycle stage.

  Example :

     ```java
     import org.dominokit.domino.api.client.annotations.presenter.OnBeforeReveal;

     @PresenterProxy()
     public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

         @OnBeforeReveal
         public void doSomething(){
             //Do something here
         }

         @OnBeforeReveal
         public void doSomethingElse(){
             //Do another thing here
         }
     }

     ```

  Will generate :

     ```java
     /**
      * This is a generated class, please don't modify
      */
     @Presenter(
         name = "",
         parent = ""
     )
     public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
       @Override
       protected void onBeforeReveal() {
         doSomething();
         doSomethingElse();
       }
     }
     ```

  Notice how the annotated methods are being called, and the order of the methods calls is the same as hiw they appear in the proxy, also in those methods the view instance will be already created and can be accessed even though it is not yet revealed, for example you can call a method in the view `view.doSomeUiStuff()`.


- #### **OnReveal**

  In a presenter proxy we can annotate as many methods with `@OnReveal` and they will be called during the presenter reveal life cycle stage.

  Example :

     ```java
     import org.dominokit.domino.api.client.annotations.presenter.OnReveal;

     @PresenterProxy()
     public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

         @OnReveal
         public void doSomething(){
             //Do something here
         }

         @OnReveal
         public void doSomethingElse(){
             //Do another thing here
         }
     }

     ```

  Will generate :

     ```java
     /**
      * This is a generated class, please don't modify
      */
     @Presenter(
         name = "",
         parent = ""
     )
     public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
       @Override
       public ViewBaseClientPresenter.RevealedHandler getRevealHandler() {
         return ()-> {
           doSomething();
           doSomethingElse();
         } ;
       }
     }
     ```

  Notice how the annotated methods are being called, and the order of the methods calls is the same as hiw they appear in the proxy, also in those methods the view instance will be already created and revealed and can be accessed, for example you can call a method in the view `view.doSomeUiStuff()`.


- #### **OnRemove**

  In a presenter proxy we can annotate as many methods with `@OnRemove` and they will be called during the presenter remove life cycle stage.

  Example :

     ```java
     import org.dominokit.domino.api.client.annotations.presenter.OnRemove;

     @PresenterProxy()
     public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

         @OnRemove
         public void doSomething(){
             //Do something here
         }

         @OnRemove
         public void doSomethingElse(){
             //Do another thing here
         }
     }

     ```

  Will generate :

     ```java
     /**
      * This is a generated class, please don't modify
      */
     @Presenter(
         name = "",
         parent = ""
     )
     public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
       @Override
       public ViewBaseClientPresenter.RemovedHandler getRemoveHandler() {
         return ()-> {
           doSomething();
           doSomethingElse();
         } ;
       }
     }
     ```

  Notice how the annotated methods are being called, and the order of the methods calls is the same as hiw they appear in the proxy, we use the remove methods to do any needed clean-up when the presenter get deactivated.


#### Routing

Routing is the process of navigating from one presenter/view to another, but before we discuss routing in domino-mvp we need to understand how presenters are actually being activated.

First we need to know that simply creating a new instance of a presenter will not initialize it correctly, it will not make it go into the life-cycle stages, instead we need to create and initialize the presenter through the domino-mvp framework, which provide a simple mechanism to do so using what is called a **presenter command**, from the name we create and send a command to the framework asking it to activate a presenter for us, each presenter will have its own auto generated command, for example, for the following presenter called `SimplePresenter` there will an auto generated command named `SimplePresenterCommand` and we can use the command to obtain a fully initialized presenter instance like this :

```java
new SimplePresenterCommand().onPresenterReady(presenter -> {
    //Do something with the initialized presenter instance
}).send();

```

We send the command and receives an initialized presenter instance in the `onPresenterReady` method, but with this we will need handle the presenter life-cycle manually, so eventhough we can do this its mainly for the framework internal usage.

Routing in domino-mvp is all about sending the presenter command and handling the life-cycle of the presenter.


- #### **URL Token routing**

  In URL token routing we control the activation of a presenter based on the token presented in the URL bar in the browser -for web implementation-, when a the URL is changed we check if the new URL matchs a token assigned to our presenter and if so we activate it, but before we go into more details lets understand the URL Token:



     ###### URL Token
      
      
     The URL token is the string in the URL bar of the browser except the base URL, for example if the URL bar has the string 
      `http://localhost:8080/path1/path2?query1=value1&query2=value2#fargment1/fragnment2`
     then the URL token that we will be using is 
      `/path1/path2?query1=value1&query2=value2#fargment1/fragnment2`
      
     and this token devided into 3 different parts : 
       
     - The path : `/path1/path2`.
     - The query : `query1=value1&query2=value2`.
     - The fragments : `fargment1/fragnment2`.


     and we can do routing based on any or a combo of the three parts
    

    To do a URL token routing we will nill need to listen to the browser URL changes and check if the new URL is a match to what we need to activate the presenter, we assign a token to a presenter using the annotation `@AutoRoute` on a presenter proxy, like the following example : 
    
    for the proxy 
    
    ```java
    import org.dominokit.domino.api.client.annotations.presenter.AutoRoute;

    @PresenterProxy
    @AutoRoute
    public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {
    }
    ```
    
    The following presnter will be generated : 
    
    ```java
    /**
     * This is a generated class, please don't modify
     */
    @Presenter(
        name = "",
        parent = ""
    )
    @AutoRoute(
        token = "",
        routeOnce = false,
        reRouteActivated = false,
        generateTask = true
    )
    @RoutingTask(SimpleViewPresenter_PresenterHistoryListenerTask.class)
    public class SimpleViewPresenter_Presenter extends SimpleViewPresenter {
    }
    ```
    
    Notice the defaults in the auto generated proxy, the default token is an empty token, and notice that now we have something called `RotingTask`, the `AutoRoute` annotation will also generate a startup task that registers a listner for the URL changes to activated the presenter when it should be.
    
    and the generated task should like the following : 
    
    ```java
    /**
     * This is a generated class, please don't modify
     */
    @StartupTask
    public class SimpleViewPresenter_PresenterHistoryListenerTask extends BaseNoTokenRoutingStartupTask {
      public SimpleViewPresenter_PresenterHistoryListenerTask() {
        super(Arrays.asList(new DefaultEventAggregator()));
      }

      @Override
      protected void onStateReady(DominoHistory.State state) {
         new SimpleViewPresenter_PresenterCommand().onPresenterReady(presenter -> {
          bindPresenter(presenter);
        } ).send();
      }
    }
    ```
    
    The generated task in this case will register a listener that listen to any url change and will send the presenter command to activate it, this is because we left the token empty and empty token in domino-mvp means any token, so what ever the URL in the browser such a presenter will always get activated.
    
    But if we change the proxy like this : 
    
    ```java
    import org.dominokit.domino.api.client.annotations.presenter.AutoRoute;

    @PresenterProxy
    @AutoRoute(token = "path1/path2")
    public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {
    }
    ```
    
    The generated task will be like this : 
    
    ```java
    /**
     * This is a generated class, please don't modify
     */
    @StartupTask
    public class SimpleViewPresenter_PresenterHistoryListenerTask extends BaseRoutingStartupTask {
      public SimpleViewPresenter_PresenterHistoryListenerTask() {
        super(Arrays.asList(new DefaultEventAggregator()));
      }

      @Override
      protected TokenFilter getTokenFilter() {
        return TokenFilter.endsWithPathFilter("path1/path2");
      }

      @Override
      protected TokenFilter getStartupTokenFilter() {
        return TokenFilter.startsWithPathFilter("path1/path2");
      }

      @Override
      protected void onStateReady(DominoHistory.State state) {
         new SimpleViewPresenter_PresenterCommand().onPresenterReady(presenter -> {
          bindPresenter(presenter);
        } ).send();
      }
    }
    ```
    
    Now we have tow token filters being assigned in this task, one of them is for on application start-up (when first time open the applicaton or when we hit the refresh button) and the other is when we do an in application routing, in common cases what is generated is enough but all of this is controled as we will see later.
    
    To explain why we have 2 different filters for start-up and in application navigation lets take and example : 
    
    Assume we start with an empty token so the url is `http://localhost:8080` and we have 2 presenters **A** and **B**, presenter **A** will be activate by the token `pathA` ans so in our home page we have a link or a button that changes the URL to `http://localhost:8080/pathA` once it does so presenter **A** will be activated and its view should be revealed in the page, now inside **A** view will want to reveal presenter **B** view so we have a link or a button that adds the path `pathB` to the URL making it like this `http://localhost:8080/pathA/pathB` then presenter **B** will be activated and its view will be revealed, now when we added `pathB` we changes the URL and that will also trigger tehe listener for presenter **A** but we dont want to reactivte presnter **A** because it is already active, thats why we say `token ends with pathA`., But what if we hit the refresh button on the browser? `ends with pathA` will not be true and presenter **A** wont be activated, so instead on a refresh we say `token starts with pathA` instead. The idea here is to give you full control over when and how you activated the presenters in different scenarioes.
    
    lets focus on the `getTokeFilter` which return a fiter that says the `path ends with` which means the presenter can only be activated if the path part of the URL token ends with the specified token, we can always specify different token filters for our presenter in the proxy using both `@RoutingTokenFilter` and `@StartupTokenFilter` annoations, We add those annotation in the proxy to static methods that take a string token and return a `TokenFilter`
    
    For example the proxy below : 
    
    ```java
    import org.dominokit.domino.api.client.annotations.presenter.AutoRoute;
    import org.dominokit.domino.api.client.annotations.presenter.StartupTokenFilter;
    import org.dominokit.domino.api.client.annotations.presenter.RoutingTokenFilter;

    @PresenterProxy
    @AutoRoute(token = "path1/path2")
    public class SimpleViewPresenter extends ViewBaseClientPresenter<SimpleView> {

        @RoutingTokenFilter
        public static TokenFilter onRoutingFilter(String token){
            return TokenFilter.contains(token);
        }

        @StartupTokenFilter
        public static TokenFilter onStartupFilter(String token){
            return TokenFilter.exactPathFilter(token);
        }
    }
    ```
    
    Will generate the following task
    
    ```java
    /**
     * This is a generated class, please don't modify
     */
    @StartupTask
    public class SimpleViewPresenter_PresenterHistoryListenerTask extends BaseRoutingStartupTask {
      public SimpleViewPresenter_PresenterHistoryListenerTask() {
        super(Arrays.asList(new DefaultEventAggregator()));
      }

      @Override
      protected TokenFilter getTokenFilter() {
        return SimpleViewPresenter_Presenter.onRoutingFilter("path1/path2");
      }

      @Override
      protected TokenFilter getStartupTokenFilter() {
        return SimpleViewPresenter_Presenter.onStartupFilter("path1/path2");
      }

      @Override
      protected void onStateReady(DominoHistory.State state) {
         new SimpleViewPresenter_PresenterCommand().onPresenterReady(presenter -> {
          bindPresenter(presenter);
        } ).send();
      }
    }
    ```
    
    Notice how the task is now referencing the annotated methods in the proxy.
    
    > The methods must be public static as they will be referenced before an instance of the presenter is actually created.

    > To learn more about how we listen to URL changes and what kind of token filters we can use, please refer the [Domino-history](https://github.com/DominoKit/domino-history) project.

    The `@AutoRoute` also has some other parameters to find control the routing behavior : 
        
    - **generateTask** : Default is `true`, when it is set to `false` not startup task will be generated so that we can manually write our own customized task.
    - **routeOnce** : Default is `false`, when set to `true` the URL change listener registered by the routing task will be removed once a routing is completed preventing the routing from happening again, this is usefull for cases where the UI component does not change an remains on the screen all time in regards of how we navigate in the application, example is the application layout.
    - **reRouteActivated** : Default value is `false`, when set to true even if the presenter is currently active it will deactivated and activated agian.


     So far we learned how we can assign tokens and token filters to our presenters, next we will learn about how we change the token and how we can have variable tokens, and how we can read information from the token.
     
     - ##### Firing token

        In Domino-mvp the URL changes are like the navigation history for our application and therefor we call the navigation as `history` we can obtain an instance of our application navigation history by calling the method `history()` in any presenter/proxy, The history api will allow us to manipulate the URL and will also parse the URL for us.
        
        To change the URL we have two options `history().fireState(..)` and `history().pushState(...)` , the fireState will change the URL and publish the events to all listeners including the current active presenter history listener while the pushState will change the URL without firing an event.
        
        To change the URL anywhere in the presenter/proxy use `history().fireState(new URL token)` : 
        
        ```java
        public void onNavigationButton(){
            history().fireState("path1/path2");
        }
        ```
        
        If the URL had `http://localhost:8080` after that call it will become `http://localhost:8080/path1/paht2`, firing or pushing a new token will always update the whole token, if you want to change the current token you can always obtain the current token in the URL using `history().currentToken()` and do the modification then fire it again, for example if the current token is `http://localhost:8080/path1/paht2` and we want to add `path3` to it we can do it like this : 
        
        ```java
        history().fireState(history().currentToken().appendPath("path3"));
        ```
        
        > To learn the full history api please refere the [Domino-history](https://github.com/DominoKit/domino-history) project.

        
     - ##### Tokens variables

        Token in general are not just a way to activate the presenters but they also can contains valuable information needed by our presenter to do its job, they are the best way to keep track of the application state between refreshes or restarts, for example assume we have page that show a book details, a URL that might represent this specific book might look like this `http://localhost:8080/books/1234` where `1234` is the ID or a unique identifire, then we take this URL and paste in the browser tab or window URL bar we should endup viewing the same book details.
        
        But, it does not make sense to fix the ID in the presenter token as we dont write a presenter for each specific book, instead we need the ID to be a variable and we need to fetch the ID value from the URL so our presnter can tell which book it needs to dispplay.
        
        In Domino-mvp we can always fetch all kind of information from the token parts - Path, query or fragments - and use it in the presenter, and we can do routing based on token with wildcards or variables, for example a book details proxy can define the token like the following : 
        
        ```java
        @PresenterProxy
        @AutoRoute(token = "books/:bookId")
        public class BookDetailsProxy extends ViewBaseClientPresenter<BookDetailsView> implements     BookDetailsView.BookDetailsUiHandlers {
        }
        ```
        
        Now as `bookId` is a variable any token that can make up for the variable will activate the presenter, for example : `books/1234`, `books/4356`, `books/someId`, `books/blahblah` are all valid tokens that ill activate that presenter.
        
        In a proxy we can obtain the actual value of token variable using annotations for each part of the token :
        
        - `@PathParameter` : We use this method to annotate a field in the proxy to hold the actual value of the a path variable, exmple : 

        ```java
        import org.dominokit.domino.api.client.annotations.presenter.PathParameter;

        @PresenterProxy
        @AutoRoute(token = "books/:bookId")
        public class BookProxy extends ViewBaseClientPresenter<BookView> {

            @PathParameter
            protected String bookId;
        }
        ```
        
        When the presenter is activated it will assign the actual value of the `:bookId` from the URL token to the annotated field, the name of the field should match the variable name or if we want to use a different name in the field we specify the variable name in the annotation, example : 
        
        ```java
        import org.dominokit.domino.api.client.annotations.presenter.PathParameter;

        @PresenterProxy
        @AutoRoute(token = "books/:bookId")
        public class BookProxy extends ViewBaseClientPresenter<BookView> {

            @PathParameter("bookId")
            protected String id;
        }
        ```
        
        We can have as many path variables in the token as long as the names of thoses variable does not collide, and we can obtain each path variable in the same way, for example if our book has a composite ID : 
 
        ```java
        import org.dominokit.domino.api.client.annotations.presenter.PathParameter;

        @PresenterProxy
        @AutoRoute(token = "books/:bookId/:year")
        public class BookProxy extends ViewBaseClientPresenter<BookView> {

            @PathParameter
            protected String bookId;
            
            @PathParameter
            protected String year;
        }
        ```
        
        
        - `@QueryParameter` : We use this method to annotate a field in the proxy to hold the actual value of the a query parameters.

        Unlike the path, the query part of the URL may or may not exist in presenter token, for example a presenter with the token `book/:bookId` can be activated by tokens like this `book/:bookId` or like this `book/:bookId?year=2021?author=dominokit`,  In this case the query part of the URL can also have some information the presenter can make use of it, for example for a search, but also unlike the path parameters query paramters does not need to be specified with a variable in the token as they already a pair of key and value by design, in a proxy we can obtain the value of a query parameter like the following :  

        ```java
        import org.dominokit.domino.api.client.annotations.presenter.PathParameter;

        @PresenterProxy
        @AutoRoute(token = "books/:bookId")
        public class BookProxy extends ViewBaseClientPresenter<BookView> {

            @QueryParameter
            protected List<String> search;
        }
        ```
        
        Notice that we dont have `search`  in the token but we still can get the value of it if it is present in the URL, also notice that we are using a list here insted of a single string, that is because a query parameter can have multiple values, example `books/123?author=dominokit&author=vegegoku` and therefor we have a list.
        
        And same as the path variables, we can have as many query parameters and we can specify the name in the annotation.
        

         
        - `@FragmentParameter` : We use this method to annotate a field in the proxy to hold the actual value of the a fragment variable, exmple : 

        ```java
        import org.dominokit.domino.api.client.annotations.presenter.PathParameter;

        @PresenterProxy
        @AutoRoute(token = "books/:bookId#:activeTab")
        public class BookProxy extends ViewBaseClientPresenter<BookView> {

            @FragmentParameter
            protected String activeTab;
        }
        ```
        
        Fragment parameters works exactly like the path parameters, but it asign values from the part after the `#` in the url, fragments can be a good choice to implement navigation to a specific section in the page.
       
       
     > In Domino-mvp routing can be achived all parts of the token, but in general we assume that we will use the path for routing from presenter to another, and we use query to hold more information for our presenter that can even be passed to the server, while we expect the fragment to be used for inside same page navigation, but we dont restric using them in anyway you desire.

        
    In some cases we might need to access the token and the state that was responsible for the presenter activation and read whatever information it has manually, in such we can use `@RoutingState`, example
    
    ```java
    import org.dominokit.domino.api.client.annotations.presenter.RoutingState;

    @PresenterProxy
    @AutoRoute(token = "books/:bookId")
    public class BookProxy extends ViewBaseClientPresenter<BookView> {

        @RoutingState
        protected DominoHistory.State state;
    }
    ```
    
    We can also use `@OnRouting` to do some logic right after a sucess routinig and before the vie is revealed, example : 
    
    ```java
    import org.dominokit.domino.api.client.annotations.presenter.OnRouting;

    @PresenterProxy
    @AutoRoute(token = "books/:bookId")
    public class BookProxy extends ViewBaseClientPresenter<BookView> {

        @RoutingState
        protected DominoHistory.State state;

        @OnRouting
        public void doSomething(){
            //do something after success routing and before view reveal
        }
    }
    ```


#### Revealing presenters

Revealing is the process of showing up the view in the application UI, it is normally when the view is attached to the application UI, the revealing is not affected by view visibility as we can reveal views in hidden state, but as long as the view become part of the applicaion UI it is considered revealed - In browser this means the view elements are attached to the DOM -.

The revealing is only for preseneters that inherits from the `ViewBaseClientPresenter`, and revealing a view is part the presenter life cycle as we discussed before, In Domino-mvp we have two options to reveal a presenter view, Auto and Manual, Auto revealing means the view will be revealed as soon as the presenter is activated, while manual means its our job to decide when to reveal the view after the presenter is activated, for example the presenter is activated but then we wait for an event to reveal the view, or we might be waiting for response from the server.

For automatic revealing we use the annotation `@AutoReveal` on the proxy, like the following :

```java
import org.dominokit.domino.api.client.annotations.presenter.AutoReveal;

@PresenterProxy
@AutoRoute(token = "books/:bookId")
@AutoReveal
public class BookProxy extends ViewBaseClientPresenter<BookView> {

}
```

- ##### Reveal condition :

  We can use the `@RevealCondition` to conditionally control auto revealing of a presenter, we annotate a method that returns a boolean and it will be evaluated before revealing the view if it returns true the auto reveal happens, otherwise the view will not, example :

    ```java
    import org.dominokit.domino.api.client.annotations.presenter.RevealCondition;
    import org.dominokit.domino.api.client.annotations.presenter.AutoReveal;

    @PresenterProxy
    @AutoRoute(token = "books/:bookId")
    @AutoReveal
    public class BookProxy extends ViewBaseClientPresenter<BookView> {

        @RevealCondition
        public boolean shouldReveal() {
            // true  : will reveal the view.
            // false : view will not be automatically revealed
            return true;
        }
    }

    ```
- ##### Manual revealing :

  To manually revealing a presenter we will just need to call its public method `reveal()`.


#### Slots

While reveal is about when to reveal the presenter view, slots are about where to reveal it, a slot in Domino-mvp is a named part of the current view of the application, which can hold one or more other elements, presenters can do two things with slots, they can register slots or they can be revealed in a slot, first we will see how we can register slots using the `@RegisterSlots` annotation :


- ##### Registering slots


```java
@PresenterProxy
@AutoRoute
@AutoReveal
@RegisterSlots({"leftPanel", "mainPanel"})
public class LayoutProxy extends ViewBaseClientPresenter<BookView> {

}
```

In the above example the layoutProxy is registering two slots `leftPanel` and `mainPanel` and now it is the job of the presenter view to implement the methods that will create the slots and the presenter will assign those names to the created slots, you can see how we do this if we check on the generated code from such a presenter :

```java

/**
 * This is a generated class, please don't modify
 */
@Presenter(
    name = "",
    parent = ""
)
@AutoRoute(
    token = "",
    routeOnce = false,
    reRouteActivated = false,
    generateTask = true
)
@RoutingTask(LayoutProxy_PresenterHistoryListenerTask.class)
@AutoReveal
public class LayoutProxy_Presenter extends LayoutProxy {
  @Override
  protected SlotsEntries getSlots() {
    SlotsEntries slotsEntries = SlotsEntries.create();
    slotsEntries.add("leftPanel", view.getLeftPanelSlot());
    slotsEntries.add("mainPanel", view.getMainPanelSlot());
    return slotsEntries;
  }
}
```

Notice the calls to the view that each should return a slot, and in order to avoid any type errors in case we change our registered slots name we also generate an interface that can be extended by the presenter view interface, so will get a compile error if such change happens without changing the implementation :

```java
import org.dominokit.domino.api.client.mvp.slots.IsSlot;

public interface LayoutProxySlots {
  IsSlot<?> getLeftPanelSlot();

  IsSlot<?> getMainPanelSlot();
}

```

What kind of slot we are registering and element is assigned to that slot is up to the view to decide, since the presenter dont need to know about UI details, but we need to knoRegistering slotsRegistering slotsw that slots can have different types andRRegRegistering slotsistering slotsegistering slots behaviors.

Slots registered by a presenter can be overrided by another presenter, but this does not mean the original one is actually removed from the slot registery, slots with the same name will be registered in stack and the next presenter that should be revealed on that slot name will pop the the slot from the top of the stack, for example, presebter A registered a slot named `content` then preseneter B got activated and should be revealed in slot `content` so it will use the slot to reveal the its view, now presenter C which should be revealed in `content` got activated and also registered two new slots , `leftPanel` and `content` again, now presenter B got activated again wihtout deactivating presenter C, this time presenter B will be revealed in the `content` slot defined by C instead of `content` slot registered by A. this give us a lot of flexibility with layouts.


- ##### Slot type

A slot evantually will be registered with a specific name, but slots are actual classes that can have different behaviors, all slots are implementation os the `IsSlot` interface, which has one mandatory method to implement `updateContent` and an optional method `cleanUp`, the first will be called when the view is being revealed to update the slot content, while the other is called when the presnter is deactivated.

Some of the pre-defined slots in Domino-mvp that are all related to web browser environment are :


- ###### SingleElementSlot :
  Extends from the base class `ElementSlot` and allow only one root element in it, updating the content of this slot with a new element will automatically remove the old content.
- ###### AppendElementSlot :
  Extends from base class `ElementSlot` and allows maulitple root elements to added to it, updating the content of such slot will keep the old content and will append the new content after the old content, the clear the slot or to remove and element from it we need to explicitily remove them manually.

In addition to those types there is also another slot types that will have an entry registered for them by default :

- ###### BodyElementSlot :
  Implements the `ContentSlot` interface and is assigned to the document body element, and cant be assinged to other elements, this slot will allow only one root element in it just like the SingleElementSlot, a slot of this type will be registerd by default on application startup under the name `body-slot` the name of the slot can be accessed as constant through `org.dominokit.domino.api.shared.extension.PredefinedSlots#BODY_SLOT`.


- ###### ModalSlot :
  Implements the `ContentSlot` interface and is not assigned to any element nor it can be assigned to a specific element, the same slot can be used to show as many pop-ups or modals as we want, a slot of this type will be registerd by default on application startup under the name `modal-slot` the name of the slot can be accessed as constant through `org.dominokit.domino.api.shared.extension.PredefinedSlots#MODAL_SLOT`.


- ###### FakeSlot :
  Implements the `ContentSlot` interface and is only used in testing, we will discuss this slot more when we talk about testing.


    Despite the fact that those slots type are sufficient to implement most of the use cases, the users of Domino-mvp can implement any kind of slot with thier custom logic, for example, a slot that allows N elements to be appended before it starts recycle from the first appended element.
    
    
    So far we talked about registering slots and slot types, next is how we tell a presenter in which slot it will be revealed.


- ##### Revealing in slots :

  To reveal a presenter in a specific slot we use the `@Slot` annotation, this annotation take one mandatory argument which is the slot name :

    ```java
    import org.dominokit.domino.api.client.annotations.presenter.Slot;

    @PresenterProxy
    @AutoRoute(token = "home")
    @Slot("content")
    @AutoReveal
    public class HomeProxy extends ViewBaseClientPresenter<HomeView> implements HomeView.HomeUiHandlers {

    }
    ```

  When this presenter is activated it will try to reveal its view in a slot name `content`, if such slot is not found we will get a runtime error.


- #### Presenter state events

We can make a presenter to fire an event when ever it is activated/deactivated to track it is state from other parts of the application, we do this using the `@OnStateChange` annotation which takes a single argument that represent the event class that extends from `ActivationEvent`, if we add such annoation to the presenter, the even will be auto fired when the presenter is activated/deactivated.

```java

import org.dominokit.domino.api.client.annotations.presenter.OnStateChanged;

@PresenterProxy
@AutoRoute(token = "home")
@Slot("content")
@AutoReveal
@OnStateChanged(HomeActivationEvent.class)
public class HomeProxy extends ViewBaseClientPresenter<HomeView> implements HomeView.HomeUiHandlers {

}
```

Later when we discuss Events we will see how we can listen to and use such events.


- #### Presenter dependency

  In an actual application we will have more than just a single presenter, and those presenters sometime needs to depend on each other, for example items list presenter will need the layout to be already active and revealed so it can reveal its view in the layout content panel, or a presenter needs to wait for another to be activated and update some context, when we have such case we use different approaches to make such dependency work :

  - ##### Parent/child dependency
    When we define a presenter/proxy we can give it a name, then we can use that name to define another presenter/proxy parent, when a presenter has a parent it will not be activated unless its parent is activated, and same as slots presenters that has the same name will be registered in a stack style, meaning a child presenter does not care which presenter is actually activated as long as it has the same name.


    Example : 

    ```java
    @PresenterProxy(name = "shell")
    @AutoRoute()
    @Slot(PredefinedSlots.BODY_SLOT)
    @AutoReveal
    @RegisterSlots({Slots.LEFT_PANEL, Slots.CONTENT})
    public class ShellProxy extends ViewBaseClientPresenter<ShellView> {

    }
    ```
        
        
    ```java
    @PresenterProxy(parent = "shell")
    @AutoRoute(token = "home")
    @Slot(Slots.CONTENT)
    @AutoReveal
    @OnStateChanged(HomeActivationEvent.class)
    public class HomeProxy extends ViewBaseClientPresenter<HomeView> implements HomeView.HomeUiHandlers {

    }
    ```
        
        In this example the home proxy will not be activated until the shell proxy is activated.


- ##### Event dependency
  The activation of a presenter can be made so it depends on events being fired, all such events needs to extend from `ActivationEvent` and the presenter can depend on one or more events to fired, we define such dependency using the `@DependsOn` and `@EventsGroup` annotations :

      ```java
      import org.dominokit.domino.api.client.annotations.presenter.DependsOn;
      import org.dominokit.domino.api.client.annotations.presenter.EventsGroup;

      @PresenterProxy(parent = "shell")
      @AutoRoute
      @Slot(Slots.CONTENT)
      @AutoReveal
      @DependsOn({
              @EventsGroup({UserLoggedInEvent.class, AuthenticationEvent.class}),
              @EventsGroup(UserLoggedOutEvent.class)
      })
      public class NotificationProxy extends ViewBaseClientPresenter<HomeView> implements HomeView.HomeUiHandlers {

      }
      ```

  In this example the presenter will not be activated unless either both of `UserLoggedInEvent` and `AuthenticationEvent` are fired or `UserLoggedOutEvent` is fired, the events in the same group will must all be fired but at least only one events group needs to be fired to activate the presenter, - events in same group uses `AND` while between groups it is `OR` -
  And as we discussed in presenter state events, presenter can auto fire presenter state events, and we make other presenters depends on those events we actually make them depend on those presenters.

- ##### Manual dependency
  Knowing the life-cycle of presenters and knowing how we can actuall do routing for presenters , we can always make presenters depends on other presenters by manually firing events, change the URL token or even manually trigger other presenters commands.


- #### Singleton presenters

  In some cases we might need to cache the presenter/view instances, because we dont want re-render the view every time the presenter got activated or there is a state in our presenter that we need to preserve, in such cases we can mark the presenter as a singleton presenter using `@Singleton(true)`, for a singleton presenter the same instance of the presenter and the view will be used and wont create a new instance with every activation.


- #### Presenters inheritance

  One of the things that we might have noticed that we are using different annotations for different settings on a proxy instead of using a single annotation with more arguments, this is because those settings can be inherited from base proxy classes, Domino-mvp will look for the annotations in the whole class tree, for example you can make a base proxy class and annotate it with `@AutoReveal` then for all proxy classes that inherits from that class will be AutoReveal even if you dont specify the annotation directly on them, same for all other annotations, except the `@PresenterProxy` since it is what actually make the class a proxy.

  And this is not only for the class level annotations, but also for all annotations that goes into the class methods, so `@PostConstruct`, `@OnInit`, `@OnBeforeReveal`, `@OnReveal`, `@OnRemove`, `@RevealCondition` ...etc and all annotations that we will study as we go with this documentation works on base classes unless we specify that they dont, this will give you a lot of power when you want to implement common behaviors in your application.

  For example, what if I want to register some audit log when ever a user navigated to a view that should be logged, it would be a too much to implement this behavior in every view, so instead we can do something like this :

  The base class

    ```java
    @Slot("content")
    @AutoReveal
    public abstract class AuditLogProxy<V extends View> extends ViewBaseClientPresenter<V> {

        @PathParameter
        String viewName;

        @OnReveal
        public void auditLog(){
            //send audit log to the server
        }
    }

    ```

  Then child classes could be something like this :

    ```java
    @PresenterProxy
    @AutoRoute(token = "app/foo/:viewName")
    public class ScreenFooProxy extends AuditLogProxy<ScreenFooView> {

    }

    ```


    ```java
    @PresenterProxy
    @AutoRoute(token = "app/bar/:viewName")
    public class ScreenBarProxy extends AuditLogProxy<ScreenBarView> {

    }

    ```

and they will inherit what ever settings from the parent class annotations.