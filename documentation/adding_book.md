# Adding new book

To add a new book, we add a button in the book list, when the button is clicked we show up a dialog to fill in the book information, we start by adding the button to the book list view

- In `BooksViewImple` edit the view to add a button that makes a call to the `BookUiHandlers` indicating that we want to add a book, the view should look like this after the change :

    ```java
    package org.dominokit.samples.library.client.views.ui;
    
    import elemental2.dom.HTMLDivElement;
    import org.dominokit.domino.api.client.annotations.UiView;
    import org.dominokit.domino.ui.Typography.Paragraph;
    import org.dominokit.domino.ui.cards.Card;
    import org.dominokit.domino.ui.cards.HeaderAction;
    import org.dominokit.domino.ui.datatable.ColumnConfig;
    import org.dominokit.domino.ui.datatable.DataTable;
    import org.dominokit.domino.ui.datatable.TableConfig;
    import org.dominokit.domino.ui.datatable.plugins.EmptyStatePlugin;
    import org.dominokit.domino.ui.datatable.plugins.RowClickPlugin;
    import org.dominokit.domino.ui.datatable.store.LocalListDataStore;
    import org.dominokit.domino.ui.dialogs.ConfirmationDialog;
    import org.dominokit.domino.ui.grid.Column;
    import org.dominokit.domino.ui.grid.Row;
    import org.dominokit.domino.ui.icons.Icons;
    import org.dominokit.domino.ui.modals.BaseModal;
    import org.dominokit.domino.ui.notifications.Notification;
    import org.dominokit.domino.ui.utils.DominoElement;
    import org.dominokit.domino.ui.utils.TextNode;
    import org.dominokit.domino.view.BaseElementView;
    import org.dominokit.samples.library.client.presenters.BooksProxy;
    import org.dominokit.samples.library.client.views.BooksView;
    import org.dominokit.samples.library.shared.model.Book;
    
    import java.util.List;
    
    import static org.jboss.elemento.Elements.b;
    
    @UiView(presentable = BooksProxy.class)
    public class BooksViewImpl extends BaseElementView<HTMLDivElement> implements BooksView {
    
        private BooksUiHandlers uiHandlers;
        private LocalListDataStore<Book> dataStore;
        private DominoElement<HTMLDivElement> root = DominoElement.div();
    
        @Override
        public HTMLDivElement init() {
    
            TableConfig<Book> tableConfig = new TableConfig<Book>()
                    .addColumn(ColumnConfig.<Book>create("title", "Title")
                            .setCellRenderer(cellInfo -> TextNode.of(cellInfo.getRecord().getTitle()))
                    )
                    .addColumn(ColumnConfig.<Book>create("author", "Author")
                            .setCellRenderer(cellInfo -> TextNode.of(cellInfo.getRecord().getAuthor()))
                    )
                    .addColumn(ColumnConfig.<Book>create("year", "Year")
                            .setCellRenderer(cellInfo -> TextNode.of(cellInfo.getRecord().getYear() + ""))
                    )
                    .addColumn(ColumnConfig.<Book>create("publisher", "Publisher")
                            .setCellRenderer(cellInfo -> TextNode.of(cellInfo.getRecord().getPublisher()))
                    )
                    .addColumn(ColumnConfig.<Book>create("price", "Price")
                            .setCellRenderer(cellInfo -> TextNode.of(cellInfo.getRecord().getCost() + ""))
                    )
                    .addColumn(ColumnConfig.<Book>create("action", "Action")
                            .setCellRenderer(cellInfo -> Icons.ALL.trash_can_mdi()
                                    .clickable()
                                    .addClickListener(evt -> {
                                        evt.stopPropagation();
                                        confirmDelete(cellInfo.getRecord());
                                    })
                                    .element())
                    )
                    .addPlugin(new EmptyStatePlugin<>(Icons.ALL.format_line_weight_mdi(), "No books found"))
                    .addPlugin(new RowClickPlugin<>(tableRow -> uiHandlers.onBookSelected(tableRow.getRecord())));
    
            dataStore = new LocalListDataStore<>();
            DataTable<Book> dataTable = new DataTable<>(tableConfig, dataStore);
    
            root
                    .appendChild(Row.create()
                            .appendChild(Column.span12()
                                    .appendChild(Card.create("Books")
                                            //Add action to trigger create new book
                                            .addHeaderAction(HeaderAction.create(Icons.ALL.plus_mdi())
                                                    .addClickListener(evt -> uiHandlers.onCreate())
                                            )
                                            .appendChild(dataTable)
                                    )
                            )
                    );
    
            return root.element();
        }
    
        @Override
        public void setUiHandlers(BooksUiHandlers uiHandlers) {
            this.uiHandlers = uiHandlers;
        }
    
        @Override
        public void setBooks(List<Book> books) {
            dataStore.setData(books);
        }
    
        @Override
        public void showError(String errorMessage) {
            Notification.createDanger(errorMessage).show();
        }
    
        public void confirmDelete(Book book) {
            ConfirmationDialog.create("Delete Book")
                    .appendChild(Paragraph.create("Are you sure you want to delete book : ")
                            .appendChild(b(book.getTitle()))
                    )
                    .onConfirm(dialog -> {
                        uiHandlers.deleteBook(book);
                        dialog.close();
                    })
                    .onReject(BaseModal::close)
                    .open();
        }
    }
    ```

- Add the method `onCreate` to `BooksUiHandlers`
- Implement `onCreate` in the `BooksProxy` to update the URL token to point to `new-book`

    ```java
        @Override
        public void onCreate() {
            history().fireState("new-book");
        }
    ```
- Use Domino-cli to add a new proxy named `NewBookProxy` using the following command
`dominokit gen module -n library -p newBook -sp library`
- In `library-shared` module delete the `NewBookService` interface, we will use the `BookService` instead.
- In the `NewBookProxy` remove the import for `NewBookService`.
- Set the proxy parent to `books`.
- Set the token to `new-book`
- Set the Slot to `PredefinedSlots.MODAL_SLOT`
- Remove `onNewBookInit` method
- Rename `onNewBookRevealed` to `initBook` and change the body to the following, to initialize the view with a new book instance
  
  ```java
      @OnReveal
      public void initBook(){
          view.edit(new Book());
      }
  ```
- Make the `NewBookView` interface extends from `ModalView` instead of `ContentView`
- Add the method `void edit(Book book)` to the `NewBookView` interface
- Add the method `Book save()` to the `NewBookView` interface
- Add the method `boolean isValid(Book book)` to the `NewBookView` interface
- Add the method `void showError(String message)` to the `NewBookView` interface
- Remove the `welcomeMessage` method from `NewBookView` interface
- In the `NewBookViewImpl` class remove the `welcomeMessage`.
- Implement the view to use the `BookComponent`, we will create an instance of `BookComponent` and delegate the calls for `edit`, `save`, and `isValid` to it, we will also layout the view to add the `save` and `cancel` buttons, `save` will add a new book, while cancel will close the dialog.
- Since we are implementing a Dialog view we will extend from `BaseModalView` instead of `BaseElementView` this time, and we have to implement a method that returns a dialog element, the view should look like this.

```java
package org.dominokit.samples.library.client.views.ui;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.ui.button.Button;
import org.dominokit.domino.ui.cards.Card;
import org.dominokit.domino.ui.grid.Column;
import org.dominokit.domino.ui.grid.Row;
import org.dominokit.domino.ui.grid.flex.FlexItem;
import org.dominokit.domino.ui.grid.flex.FlexLayout;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.modals.ModalDialog;
import org.dominokit.domino.ui.notifications.Notification;
import org.dominokit.domino.ui.utils.DominoElement;
import org.dominokit.domino.view.BaseElementView;
import org.dominokit.domino.view.BaseModalView;
import org.dominokit.samples.library.client.presenters.NewBookProxy;
import org.dominokit.samples.library.client.views.NewBookView;
import org.dominokit.samples.library.shared.model.Book;

@UiView(presentable = NewBookProxy.class)
public class NewBookViewImpl extends BaseModalView<ModalDialog> implements NewBookView {

    private NewBookUiHandlers uiHandlers;
    private ModalDialog modal;
    private BookComponent bookComponent;

    @Override
    public ModalDialog getModal() {
        return modal;
    }

    @Override
    public HTMLDivElement init() {
        bookComponent = new BookComponent();
        modal = ModalDialog.create("New Book")
                .appendChild(bookComponent)
                .appendFooterChild(Row.create()
                        .appendChild(Column.span12()
                                .appendChild(FlexLayout.create()
                                        .setGap("10px")
                                        .appendChild(FlexItem.create()
                                                .appendChild(Button.createPrimary(Icons.ALL.floppy_mdi())
                                                        .setContent("Save")
                                                        .setMinWidth("120px")
                                                        .addClickListener(evt -> uiHandlers.onSaveBook())
                                                )
                                        )
                                        .appendChild(FlexItem.create()
                                                .appendChild(Button.create(Icons.ALL.floppy_mdi())
                                                        .setContent("Cancel")
                                                        .linkify()
                                                        .setMinWidth("120px")
                                                        .addClickListener(evt -> uiHandlers.onCancel())
                                                ))
                                )
                        )
                );
        return modal.element();
    }

    @Override
    public void setUiHandlers(NewBookUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    @Override
    public void edit(Book book) {
        bookComponent.edit(book);
    }

    @Override
    public Book save() {
        return bookComponent.save();
    }

    @Override
    public boolean isValid() {
        return bookComponent.isValid();
    }

    @Override
    public void onError(String message) {
        Notification.createDanger(message).show();
    }
}
```

- Add the method `onSaveBook` and `onCancel` to the `NewBookUiHandlers` interface.
- Implement the methods in the `NewBookProxy` like the following :

- `onSaveBook` : Will first validate the input, if it is valid we make a rest call to add a new book.
```java
    @Override
    public void onSaveBook() {
        if(view.isValid()) {
            BooksServiceFactory.INSTANCE
                    .create(view.save())
                    .onSuccess(book -> {
                        history().fireState("books/" + book.getTitle());
                        view.close();
                    })
                    .onFailed(failedResponseBean -> view.onError(failedResponseBean.getBody()))
                    .send();
        }
    }
```
- `onCancel` : We close the dialog
```java
    @Override
    public void onCancel() {
        history().fireState("books");
        view.close();
    }
```

And with this we finish the implementation of adding a new book, rebuild the app and try the new function.