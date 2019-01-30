#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.views;

import org.dominokit.domino.api.client.annotations.UiView;
import org.dominokit.domino.test.api.client.FakeElement;
import org.dominokit.domino.test.api.client.FakeView;
import ${package}.${subpackage}.client.presenters.${module}Proxy_Presenter;

@UiView(presentable= ${module}Proxy_Presenter.class)
public class Fake${module}View extends FakeView implements ${module}View {

    private FakeElement root;

    @Override
    protected void init(FakeElement root) {
        this.root = root;
    }
}
