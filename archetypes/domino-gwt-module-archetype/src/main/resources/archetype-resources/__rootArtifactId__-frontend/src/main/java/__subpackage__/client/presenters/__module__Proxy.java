#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import org.dominokit.domino.api.client.annotations.*;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import ${package}.${subpackage}.client.views.${module}View;
import ${package}.${subpackage}.shared.extension.${module}Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter.DOCUMENT_BODY;

@PresenterProxy
@AutoRoute(token = "${module}")
@Slot(DOCUMENT_BODY)
@AutoReveal
@OnStateChanged(${module}Event.class)
public class ${module}Proxy extends ViewBaseClientPresenter<${module}View> {

    private static final Logger LOGGER = LoggerFactory.getLogger(${module}Proxy.class);

    @OnInit
    public void on${module}Init(){
        LOGGER.info("${module} initialized");
    }

    @OnReveal
    public void on${module}Revealed() {
        LOGGER.info("${module} view revealed");
    }
}