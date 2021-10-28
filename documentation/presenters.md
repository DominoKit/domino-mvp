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

In Domino-mvp we control the presenter routing through three different mechanisms, **URL Token based routing**, **Events dependency**, and **Parent/Child relation** or a combination of any of the three.


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

