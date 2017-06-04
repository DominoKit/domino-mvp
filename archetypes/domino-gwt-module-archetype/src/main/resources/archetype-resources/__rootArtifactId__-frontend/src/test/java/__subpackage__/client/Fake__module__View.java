#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.user.client.ui.IsWidget;
import ${package}.${subpackage}.client.views.${module}View;
import ${package}.${subpackage}.client.presenters.${module}Presenter;
import com.progressoft.brix.domino.api.client.annotations.UiView;

@UiView(presentable=${module}Presenter.class)
public class Fake${module}View implements ${module}View {

    @Override
    public IsWidget get() {
        return null;
    }
}
