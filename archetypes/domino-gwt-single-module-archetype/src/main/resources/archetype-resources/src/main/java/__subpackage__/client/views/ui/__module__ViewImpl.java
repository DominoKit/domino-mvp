#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.views.ui;

import ${package}.${subpackage}.client.views.${module}View;
import org.dominokit.domino.api.client.annotations.UiView;
import ${package}.${subpackage}.client.presenters.${module}Presenter;

@UiView(presentable = ${module}Presenter.class)
public class ${module}ViewImpl implements ${module}View{

    public ${module}ViewImpl() {
    }
}