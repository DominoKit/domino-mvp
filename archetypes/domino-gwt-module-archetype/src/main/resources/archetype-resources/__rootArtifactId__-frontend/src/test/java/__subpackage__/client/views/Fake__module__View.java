#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.views;

import ${package}.${subpackage}.client.views.${module}View;
import ${package}.${subpackage}.client.presenters.${module}Presenter;
import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.api.shared.extension.Content;

@UiView(presentable=${module}Presenter.class)
public class Fake${module}View implements ${module}View {
    @Override
    public Content getContent() {
        return null;
    }
}
