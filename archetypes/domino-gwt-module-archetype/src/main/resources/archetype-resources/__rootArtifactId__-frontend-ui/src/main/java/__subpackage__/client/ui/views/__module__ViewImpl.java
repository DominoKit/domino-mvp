#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.ui.views;

import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.view.BaseElementView;

import org.dominokit.domino.api.client.annotations.UiView;

import ${package}.${subpackage}.client.presenters.${module}Presenter;
import ${package}.${subpackage}.client.views.${module}View;

import static org.jboss.gwt.elemento.core.Elements.div;
import static org.jboss.gwt.elemento.core.Elements.h;

@UiView(presentable = ${module}Presenter.class)
public class ${module}ViewImpl extends BaseElementView<HTMLDivElement> implements ${module}View{

    @Override
    public void init(HTMLDivElement root) {
        root.appendChild(h(1).textContent("Hello World!").asElement());
        DomGlobal.document.body.appendChild(root);
    }

    @Override
    public HTMLDivElement createRoot() {
        return div().asElement();
    }
}