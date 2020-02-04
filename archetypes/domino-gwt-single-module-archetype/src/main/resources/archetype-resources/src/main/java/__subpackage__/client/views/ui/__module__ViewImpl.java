#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.views.ui;

import elemental2.dom.HTMLDivElement;
import org.dominokit.domino.view.BaseElementView;

import org.dominokit.domino.api.client.annotations.UiView;

import ${package}.${subpackage}.client.presenters.${module}Proxy;
import ${package}.${subpackage}.client.views.${module}View;

import static org.jboss.elemento.Elements.div;
import static org.jboss.elemento.Elements.h;

@UiView(presentable = ${module}Proxy.class)
public class ${module}ViewImpl extends BaseElementView<HTMLDivElement> implements ${module}View{

    @Override
    public void init(HTMLDivElement root) {
        root.appendChild(h(1).textContent("Hello World! from module : ${module}").asElement());
    }

    @Override
    public HTMLDivElement createRoot() {
        return div().asElement();
    }
}