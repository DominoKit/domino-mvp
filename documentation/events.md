# Events

Events are a core part of Domino-mvp and they play an important role in both navigation and communication between presenters, yet they are simple, so lets define an event and see how we can use it, how we can fire and listen to events, and understand the different types of events :


### Define events :

In Domino-mvp an event is a class that implements the interface `DominoEvent` which is a generic interface that take one generic argument that is for a class that implement `EventContex` each even has a context, the context is the class that holds the data that the even is supposed to hold, example :

```java
import org.dominokit.domino.api.shared.extension.DominoEvent;
import org.dominokit.domino.api.shared.extension.EventContext;

public class MessageReceivedEvent implements DominoEvent<MessageReceivedEvent.MessageContext> {

    private final MessageContext context;

    public MessageReceivedEvent(String message) {
        this.context = new MessageContext(message);
    }

    @Override
    public MessageContext context() {
        return context;
    }

    public static class MessageContext implements EventContext {
        private final String message;

        public MessageContext(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
```

In the above code we defined an event context `MessageContext` and then we used that to create an event class `MessageReceivedEvent`.

### Firing events :

To fire an event in Domino-mvp we usethe `DominoEvents` class static method `fire` passing the even class type and a new instance of the event or a subtype of the event, for example in a proxy we can do this :

```java
import org.dominokit.domino.api.client.extension.DominoEvents;

@PresenterProxy(parent = "shell")
@AutoRoute
@Slot("notifications")
@AutoReveal
public class NotificationProxy extends ViewBaseClientPresenter<HomeView> implements HomeView.HomeUiHandlers {

    public void publishMessage(String message){
        DominoEvents.fire(MessageReceivedEvent.class, new MessageReceivedEvent(message));
    }
}
```

We can fire events from any class in our application not just presenters, the only codition here is that the Domino-mvp application should be initialized and running  - ClientApp.make().run() is already called -

Presenter has special methods to fire events without directly referencing the `DominoEvents` class, we can directly call either `fireEvent` of `publishEvent` methods, the `fireEvent` method will fire the event to all active listeners including the presenter firing the event, and `publishEvent` will fire the event for all active listeners except the presenter firing the event.

### Listening to events :

We can manually register and remove event listeners from anywhere in the application using the `ClientApp` `registerEventListener` and `removeEventListener` , example :

Registering event lsitener

```java
ClientApp.make().registerEventListener(MessageReceivedEvent.class, (DominoEventListener<MessageReceivedEvent>) dominoEvent -> {
            var eventMessage =dominoEvent.context().getMessage();
            //do something with the message
        });
```

Removing an event will require that we keep an instance of the registered event, once we have the instance we can manually remove it like the following example :

Removing event lsitener

```java
DominoEventListener<MessageReceivedEvent> eventReference = dominoEvent -> {
            var eventMessage = dominoEvent.context().getMessage();
            //do something with the message
        };
ClientApp.make().removeEventListener(MessageReceivedEvent.class, eventReference);
```

But when we are working with presenter there is an declarative way to register events in a proxy using the annotation `@ListenTo`, the event listeners registered in a presenter will be coupled to the presenter life-cycle, so when the presener is activated they will be automatically registered and when the presenter is deactivated then they will be removed, **this means only active presenters can listen to events**, to automatically register/remove events, you create a method annotated with `@ListenTo` passing the event type as an argument to the annotation, the method will take one argument which the event context, example :

```java
import org.dominokit.domino.api.client.annotations.presenter.ListenTo;

@PresenterProxy(parent = "shell")
@AutoRoute
@Slot("notifications")
@AutoReveal
public class NotificationProxy extends ViewBaseClientPresenter<HomeView> implements HomeView.HomeUiHandlers {

    @ListenTo(event = MessageReceivedEvent.class)
    public void onMessageReceived(MessageReceivedEvent.MessageContext context){
        var message = context.getMessage();
        //do something with the message
    }
}
```

### Global events :

Global events are events that can be fired and received through different Domino-mvp applications, the context of those events should be something that we can convert from/to strings, they are very useful when for example we include 2 or more Domino-mvp applications in the same web page and we wanted to share some events between them, example :

```java
import org.dominokit.domino.api.shared.extension.EventContext;
import org.dominokit.domino.api.shared.extension.GlobalEvent;

public class ApplicationReadyEvent extends GlobalEvent<ApplicationReadyEvent.ApplicationReadyContext> {

    private final ApplicationReadyContext context;

    public ApplicationReadyEvent(String name) {
        this.context = new ApplicationReadyContext(name);
    }

    @Override
    public String serialize() {
        return context.getApplicationName();
    }

    @Override
    public ApplicationReadyContext context() {
        return null;
    }

    public static class ApplicationReadyContext implements EventContext {
        private final String applicationName;

        public ApplicationReadyContext(String applicationName) {
            this.applicationName = applicationName;
        }

        public String getApplicationName() {
            return applicationName;
        }
    }
}
```

### Activation events

Activation events are special type of global events that holds a boolean value, they can be used in presenter with `onStateChanged` and `DependsOn` to apply presenters dependency as we discussed in presenters documentation, example :

```java
import org.dominokit.domino.api.shared.extension.ActivationEvent;

public class AuthenticationEvent extends ActivationEvent {
    public AuthenticationEvent(boolean active) {
        super(active);
    }

    public AuthenticationEvent(String serializedEvent) {
        super(serializedEvent);
    }
}
```

