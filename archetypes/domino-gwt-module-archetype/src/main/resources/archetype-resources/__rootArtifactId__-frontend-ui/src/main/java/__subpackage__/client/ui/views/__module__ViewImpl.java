#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.ui.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;

import org.dominokit.domino.api.client.annotations.UiView;

import ${package}.${subpackage}.client.presenters.${module}Presenter;
import ${package}.${subpackage}.client.views.${module}View;

@UiView(presentable = ${module}Presenter.class)
public class ${module}ViewImpl extends Composite implements ${module}View{

    interface ${module}ViewImplUiBinder extends UiBinder<HTMLPanel, ${module}ViewImpl> {
    }

    private static ${module}ViewImplUiBinder ourUiBinder = GWT.create(${module}ViewImplUiBinder.class);

    @UiField
    DivElement mainDiv;

    public ${module}ViewImpl() {
        initWidget(ourUiBinder.createAndBindUi(this));
        mainDiv.setInnerHTML("<h1>Hello world!</h1>");
        Document.get().getBody().appendChild(mainDiv);
    }
}