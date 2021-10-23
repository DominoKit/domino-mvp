#### Presenter

Presenters are the core of a domino-mvp application, they control the page life-cycle and holds most of our business logic, they control routing and navigation, fire events, make REST calls, do the validation and drive the view.

Domino-mvp provide tow main types of presenters, the passive or simple presenters and the viewable presenters.

- #### Passive or Simple presenters

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

- #### Viewable presenters
    
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
  > We will discuss views in details in later parts if the documentation.

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
  
#### Presenter command


Since manually creating an instance of a presenter using the `new` keyword will not initialize the presenter properly, and will not inject the view into the presenter we need a way to obtain an instance of a presenter to call any of its public API, presenter commands does this, each presenter comes associated with a presenter command, a presenter command will be auto generated and will have the name that is same as the presenter with the `Command` postfix, sending a command to a non-singleton presenter will always create a new instance of that presenter.

example

for presenter :

```java
@Presenter
public class SamplePresenter extends ViewBaseClientPresenter<ViewInterface> {
}
```

the generated command will be :

```java
@Command
public class SamplePresenterCommand extends PresenterCommand<SamplePresenter> {
}
```

Using a presenter command

```java
new SamplePresenterCommand()
   .onReady(presenter -> //call some presenter api with this instance)
   .send();
```

this will make sure the presenter is properly initialized before the `onReady` handler is called.

domino-mvp internally depends on presenter commands in many of its parts.

#### Presenter proxy

Even though we can always override the presenter super class method to work with presenters, there is a declarative way to deal with presenters that makes working with them easier and adds some interesting features, this is called a presenter proxy, presenter proxy works as a super class for the actual presenter which will be automatically generated from the presenter proxy.

the simplest presenter proxy looks like a normal presenter :

```java
@PresenterProxy
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
}
```
this looks like a normal presenter, which is true. the generated presenter will almost look the same, this will generate the following presenter :

```java
@Presenter
public class SampleProxy_Presenter extends SampleProxy {
}
```
lets spice things a little bit using some annotations :


##### @OnInit

lets add 2 methods in the proxy and annotate them with `@OnInit`

```java
@PresenterProxy
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {

    @OnInit
    public void initFoo(){
        //do some initialization here
    }

    @OnInit
    public void initBar(){
        //do some initialization here
    }
}
```

the generated presenter will look like this :

```java
@Presenter
public class SampleProxy_Presenter extends SampleProxy {
  @Override
  protected void onActivated() {
    initFoo();
    initBar();
  }
}
```

the `onActivated` now is implemented and calls both annotated methods in the order of deceleration on the class, you annotate as many as you wish init methods in the proxy.

> Annotated methods in super classes will be also included.

##### @OnReveal , @OnRemove

these tow annotations allows you to add any number of methods to be called by the `revealHandler`/`removeHandler`, sample :

```java
@PresenterProxy
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
    @OnReveal
    public void applyFoo(){
        //view is revealed, apply something
    }

    @OnReveal
    public void applyBar(){
        //view is revealed, apply something
    }

    @OnRemove
    public void cleanUpFoo(){
        //view is removed, cleanup something
    }

    @OnRemove
    public void cleanUpBar(){
        //view is removed, cleanup something
    }
}
```

the generated code will be :

```java
@Presenter
public class SampleProxy_Presenter extends SampleProxy {
  @Override
  public DominoView.RevealedHandler getRevealHandler() {
    return ()-> {
      applyFoo();
      applyBar();
    } ;
  }

  @Override
  public DominoView.RemovedHandler getRemoveHandler() {
    return ()-> {
      cleanUpFoo();
      cleanUpBar();
    } ;
  }
}
```

now the methods are called inside the correct handler in the order of deceleration.

> Annotated methods in super classes will be also included.

#### @Slot

This annotation on class level will define the reveal slot for the presenter

Sample

```java
@PresenterProxy
@Slot("content")
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
}
```

the generated code will be :

```java
@Presenter
@Slot("content")
public class SampleProxy_Presenter extends SampleProxy {
  @Override
  public String revealSlot() {
    return "content";
  }
}
```

Notice how the annotation is being copied to the generated class to be picked up by other processors.

### Routing presenters

By default in domino-mvp you can activate presenters by firing/listning on domino-events, using presenters commands, or registering a listener for the url changes in a startup task, but with the presenter proxy you dont need to write this routing code, instead you can use the declarative annotations :

#### @AutoRoute

this annotation will generate the required code to listen on the url changes and and activate the presenter based on a token and a token filter, Sample :

```java
@PresenterProxy
@AutoRoute(token="watch-list/movies")
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
}
```

this code will generate the following startup task

```java
@StartupTask
public class SampleProxy_PresenterHistoryListenerTask extends BaseRoutingStartupTask {
  public SampleProxy_PresenterHistoryListenerTask() {
    super(Arrays.asList(new DefaultEventAggregator()));
  }

  @Override
  protected TokenFilter getTokenFilter() {
    return TokenFilter.exactMatch("watch-list/movies");
  }

  @Override
  protected void onStateReady(DominoHistory.State state) {
     new SampleProxy_PresenterCommand().onPresenterReady(presenter -> {
      if(!presenter.isActivated()) {
        presenter.reveal();
      }
    } ).send();
  }
}
```

this code will listen for the browser url, and only when it is exactly matches the token specified it will activate the presenter by sending a command.

The annotation also has a Boolean parameter that indicate if we need to route to this presenter only once, this means after the routing happens it will never happen again, this useful for cases like the layout where you only want to route for any token at application start then no more routing is needed.

#### @OnRouting

If you wish to do some work when ever a routing to the presenter happens you can add any number of methods annotated with this annotation, Sample :

```java
@PresenterProxy
@AutoRoute(token="watch-list/movies")
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
    @OnRouting
    public void doFoo(){
        //do something here
    }

    @OnRouting
    public void doBar(){
        //do something here
    }
}
```

the generated code will be :

```java
@StartupTask
public class SampleProxy_PresenterHistoryListenerTask extends BaseRoutingStartupTask {
  public SampleProxy_PresenterHistoryListenerTask() {
    super(Arrays.asList(new DefaultEventAggregator()));
  }

  @Override
  protected TokenFilter getTokenFilter() {
    return TokenFilter.exactMatch("watch-list/movies");
  }

  @Override
  protected void onStateReady(DominoHistory.State state) {
     new SampleProxy_PresenterCommand().onPresenterReady(presenter -> {
      if(!presenter.isActivated()) {
        presenter.doFoo();
        presenter.doBar();
        presenter.reveal();
      }
    } ).send();
  }
}
```

the methods will be called when ever the routing happens in the order of deceleration.

> Annotated methods in super classes will be also included.

#### @RoutingTokenFilter

by default all routing happens with exact match of token, but this can be customized using this annotation on a `static` method that returns a different TokenFilter, this needs to be static since it will be called before the presenter is actually activated or created. Sample :

```java
@PresenterProxy
@AutoRoute(token="watch-list/movies")
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
    @RoutingTokenFilter
    public static TokenFilter filter(String token){
        return TokenFilter.startsWith(token);
    }
}
```

the generated startup task will have the following method :

```java
  @Override
  protected TokenFilter getTokenFilter() {
    return TokenFilter.startsWith("watch-list/movies");
  }
```

notice how the `exactMatch` now is changed to a call to the annotated method that returns a `startWith`

> the processor will recursively look for a static method annotated as `@FilterToken` starting from the class annotated with `@PresenterProxy` down into the super classes until it find the first match.

#### @StartupTokenFilter
This is just like the RoutingTokenFilter but will be only applied when we use a direct link to open the application or refresh the page.

##### Passing path parameters

The token can accept variable parameters values for paths part, query parameters part, and fragments part

example token `/path1/:pathParameter/path3?param1=value1&param2=:value2Param&param3=value3#fargment1/:fragmentParam/fragment3`

in the above token all parts starts with `:` means that the token can accept any value as a substitute for these parameters

example :

`@AutoRoute(token = "watch-list/movies/:movieName")`

an exact match will accept any value for the `movieName`
so all the following will actually route to the presenter :

`watch-list/movies/alians`

`watch-list/movies/titanic`

`watch-list/movies/galdiator`

`watch-list/movies/xyz`

> Most of the token filters will accept and will work with variable parameters in the token, but some will not, like `TokenFilter.containsPaths` which cant determine which part of the token to be normalized with parameter as it takes a set of paths as an argument.

#### Obtaining path parameters and fragment parameters
#### @RoutingState

when a routing happens you can always obtain an instance of the state token that was responsible of the routing, by obtaining this state you can read the passed variable parameters and query parameters from the token, use the `@RoutingState` annotation on a protected field of type `DominoHistory.State`. Sample

```java
@PresenterProxy
@AutoRoute(token="watch-list/movies/:movieName")
public class SamplePresenter extends ViewBaseClientPresenter<ViewInterface> {
    
    @RoutingState
    protected DominoHistory.State state;

    @OnRouting
    public void doSomethingWithTheState(){
        state.normalizedToken().getPathParameter("movieName");
    }
}
```

with this you will obtain an instance of the state, the `State` has lots of useful methods to deal with history, we will talk more about this when we talk about dealing with history api in domino-mvp, but now we are interested in the `NormalizedToken` from the state, this contains normalized token and all path and fragment parameters obtained from the URL token, see how we were able to get the movie name from the normalize token.

to get a fragment parameter use `state.normalizedToken().getFragmentParameter("paramName");`

Query parameters are accessible from the actual token so just use `state.token().getQueryParameter("paramName")`

#### Shortcut annotations :

In addition to obtaining routing state, you can use shortcut method to obtain path, fragments or query parameters from the routing state.

`@PathParameter` : use on a protected `String` field to obtain the value of a path parameter.
`@FragmentParameter` : use on a protected `String` field to obtain the value of a fragment parameter.
`@QueryParameter` : use on a protected `String` field to obtain the value of a query parameter.

for all three annotation the name of the parameter is the name of field by default, a custom name can be provided in the annotation value if want to use a name different than the field name.

#### @Singleton

adding this annotation on a presenter proxy will make the presenter singleton, which means for the first routing or command to the presenter a new instance will be created, and any later consequent routing or command will use the same instance.

#### Revealing the view

The routing we did before did not attempt to reveal the presenter view content in the page, this because we didnt direct the presenter to do so, we can manually reveal the content using the `reveal()` or `revealInSlot` methods.

the `reveal` method will reveal the view in the slot defined by the `revealSlot` method, while `revealInSlot` will allow you to dynamically pick the slot in which the view will be revealed.

we can enable auto revealing of a view in a declarative way.

#### @AutoRveal

using this annotation will require the `@Slot` annotation to be added on the class or the method `revealSlot` to be implemented, when we add this annotation on the class then when the routing happens the view will be automatically revealed in the designated slot. Sample

```java
@AutoRoute(token="watch-list/movies/:movieName")
@AutoReveal
@Slot("content")
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {
}
```

in the generate startup task we will have this code :

```java
@Override
protected void onStateReady(DominoHistory.State state) {
      new SampleProxy_PresenterCommand().onPresenterReady(presenter -> {
        presenter.reveal();
      } ).send();
}

```

notice the call to the reveal method of the presenter.

#### @RevealCondition

Sometimes we need some control over the auto reveal, this is what the `@RevealCondition` annotation is used for, when this annotation is added on a method that returns a `boolean` it will be evaluated before revealing the view if it returns `true` the reveal will happens, otherwise the view will not be revealed. Sample

```java
@PresenterProxy
@AutoRoute(token="watch-list/movies/:movieName")
@AutoReveal
@Slot("content")
public class SampleProxy extends ViewBaseClientPresenter<ViewInterface> {

    @RevealCondition
    public boolean validateOnReveal(){
        // true  : will reveal the view.
        // false : view will not be automatically revealed
        return true;
    }
}
```

#### OnBeforeReveal
Use this annotation on a method to be called right before the view is revealed.

#### @PostConstruct
Use this annotation on any method to be called right after the creation of the presenter and the view but before any reveal or activation events.

#### @OnStateChange

This annotation allows us to define an event class that extends `ActivationEvent`, when the presenter is activated or deactivated this event will be fired, this is an important feature that allows other parts of the application and the framework to track the presenter state, this feature is used by the framework to satisfy the presenters dependencies on each other. the event will fired automatically.

@DependsOn and @EventsGroup

This annotation allows us to define a dependency for a presenter activation when a routing happen, the `DependsOn` annotation take an array of `EventsGroup` annotations, when ever a routing happens and only when all events in a group are satisfied the presenter will be activated.

Sample :

Assume we have PresenterB that requires PresenterA to run first, then PresenterA will define an `@OnStateChange` event, while PresenterB will depends on that event, then even of the routing to PresenterB happens before presenterA is activated, activation of presenterB will wait until presenterA is activated.