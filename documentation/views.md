# Views

Views in domino-mvp are the UI part of the module, a view defines what kind of content we need to render in the application, we have seen that `ViewBaseClientPresenter` sub-classesany will define a generic type that is of type `? extends View`, all views in Domino-mvp must extends from the interface `View`, presenters should never know about the view implementation details and for that views from the presenter perspective are just interfaces that play the role of a contract between the presenter and the view actual implementation, the presenter will call methods defined in the interface to manipulate the view without understanding what kind of UI components or how the view will handle those calls, the framework during the presenter life-cycle will create a view instance from its implementation and inject it into the presenter.

The `View` interface itself in Domino-mvp is just a marker interface and does not specify any methods, a more useful interface is is `ContentView` which from the name provide a content, most of the time this is the interface we will be using, the `Content` view proides a single element that represent the root element of the view which will be the element that we attach to the application UI when the view is revealed, example :

```java
import org.dominokit.domino.api.client.mvp.view.ContentView;

public interface BookView extends ContentView {
    void setBookTitle(String title);
}
```

an implementation of this interface should be annotated with `@UiView` and specifying to which presenter it belongs. example :

```java
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.samples.shell.client.presenters.BookProxy;
import org.dominokit.samples.shell.client.views.PersonView;

@UiView(presentable = BookProxy.class)
public class BookViewImpl implements BookView {

    private DominoElement<HTMLDivElement> root = DominoElement.div();

    @Override
    public void setBookTitle(String title) {
        root.setTextContent(title);
    }

    @Override
    public Content getContent() {
        return (Content<HTMLDivElement>) () -> root.element();
    }
}
```

Now a presenter can use such view like the following example :

```java
@PresenterProxy
@AutoRoute(token = "books/:title")
public class BookProxy extends ViewBaseClientPresenter<BookView> {

    @PathParameter
    String title;

    @OnReveal
    public void setBookTitle(){
        view.setBookTitle(title);
    }
}
```

#### UI handlers

The view interface is how presenter interact with view without having access to its implementation, Views interact with presenters in the same way, views does not know about presenters implementations and details, therefore we use what is called `UiHandlers` which an interface implemented by the presenter and used by the view.

the `UiHandlers` is a marker interface, to implement UI handlers create an interface that extends the `UiHandlers` interface and add what ever methods in it. then make your view interface extends from `HasUiHandlers` inreface passing your created UiHandlers interface as a generic type, this will add the method `setUihandlers` to your view, then the presenter implements the created UiHandlers interface.

When framework creates the presenter instance it will inject the presenter as a UiHandlers in the view using the `setUiHandlers` method.

Example :

If we have the following view :

```java
public interface BookView extends ContentView, HasUiHandlers<BookView.BookUiHandlers> {
    void setBookTitle(String title);

    interface BookUiHandlers extends UiHandlers {
        void onBookDelete(String title);
    }
}
```

Then we implement the UiHandlers in the presenter :

```java
@PresenterProxy
@AutoRoute(token = "books/:title")
public class BookProxy extends ViewBaseClientPresenter<BookView> implements BookView.BookUiHandlers {

    @PathParameter
    String title;

    @OnReveal
    public void setBookTitle(){
        view.setBookTitle(title);
    }

    @Override
    public void onBookDelete(String title) {
        //delete book
    }
}
```

The view implementation will implement HasUiHandlers :

```java

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.shared.extension.Content;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.samples.shell.client.presenters.BookProxy;
import org.dominokit.samples.shell.client.views.BookView;

@UiView(presentable = BookProxy.class)
public class BookViewImpl implements BookView {

    private DominoElement<HTMLDivElement> root = DominoElement.div();
    private BookUiHandlers uiHandlers;

    @Override
    public void setBookTitle(String title) {
        root
                .setTextContent(title)
                .appendChild(Button.create("Delete")
                        .addClickListener(evt -> {
                            uiHandlers.onBookDelete(title);
                        }));
    }

    @Override
    public Content getContent() {
        return (Content<HTMLDivElement>) () -> root.element();
    }

    @Override
    public void setUiHandlers(BookUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
}
```

> In `@UiView` the presentable argument can take one or more presenters, In case we have presenters that uses the same view but apply some different logic, this way we wont need to duplicate the view for each presenter.

> In `@UiView` the presentable argument can referece presenters or proxies or a mix.


#### BaseElementView

When we directly implement `ContentView` we are implementing the raw interface, which means there will be a lot of wiring that we need to do to make the view works as expected with its presenter, for example, by directly implementing `ContentView` we have to implement the wiring to tell the presenter when our view attached/detached from the DOM, also how does our view recreate the content when ever it needs re-render.

The `BaseElementView` is a generic abstract class that implements all this wiring for you, in most of the cases you will need to extend from this class, and implement the `init` method which should return the root element.


```java
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.view.BaseElementView;
import org.dominokit.samples.shell.client.presenters.BookProxy;
import org.dominokit.samples.shell.client.views.BookView;

@UiView(presentable = BookProxy.class)
public class BookViewImpl extends BaseElementView<HTMLDivElement> implements BookView {

    private DominoElement<HTMLDivElement> root = DominoElement.div();

    @Override
    protected HTMLDivElement init() {
        return root.element();
    }
}
```

This view will automatically inform the presenter when ever its root element is attached/detached from the DOM, and will automatically re-drawn when it is requested to be revealed again.

there is only few cases when you cant extends from `BaseElementView` such as when the view does not have a single root element, like layout that consist of several sections appended to the page body.

#### Slots

When we explained presenters we explained what are slots, how we register them, and what are the pre-defined slot types, meanwhile presenters responsible about registering slots, views are responsible about creating them, when we add `@RegisterSlots` to the presenter we will get an interface generated that can be extended by the view interface, this force the view implementation to implement the methods to create the slots, Example :

The proxy register slots

```java
@PresenterProxy(name = "shell")
@AutoRoute()
@AutoReveal
@Slot(PredefinedSlots.BODY_SLOT)
@RegisterSlots({"left-panel", "content"})
public class ShellProxy extends ViewBaseClientPresenter<ShellView> {

}
```

An interface is generated

```java
import org.dominokit.domino.api.client.mvp.slots.IsSlot;

public interface ShellProxySlots {
  IsSlot<?> getLeftPanelSlot();

  IsSlot<?> getContentSlot();
}
```

Which we extend in the view

```java
public interface ShellView extends ContentView, ShellProxySlots {

}
```

And implement in the view implementation

```java
@UiView(presentable = ShellProxy.class)
public class ShellViewImpl extends BaseElementView<HTMLDivElement> implements ShellView{

    private Layout layout= Layout.create("Book store");

    @Override
    public HTMLDivElement init() {
        return layout.element();
    }

    @Override
    public IsSlot<?> getLeftPanelSlot() {
        return SingleElementSlot.of(layout.getLeftPanel());
    }

    @Override
    public IsSlot<?> getContentSlot() {
        return SingleElementSlot.of(layout.getContentPanel());
    }
}
```

And this complete the cycle of slots registration

