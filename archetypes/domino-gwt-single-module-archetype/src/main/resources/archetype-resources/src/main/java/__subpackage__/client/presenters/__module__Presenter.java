#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import org.dominokit.domino.api.client.annotations.ListenTo;
import org.dominokit.domino.api.client.annotations.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import ${package}.${subpackage}.client.views.${module}View;
import org.dominokit.domino.api.shared.extension.MainDominoEvent;
import org.dominokit.domino.api.shared.extension.MainEventContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Presenter
public class ${module}Presenter extends ViewBaseClientPresenter<${module}View> {

    private static final Logger LOGGER = LoggerFactory.getLogger(${module}Presenter.class);

    @ListenTo(event=MainDominoEvent.class)
    public void listenToMainEvent(MainEventContext context) {
        LOGGER.info("Main context received at presenter " + ${module}Presenter.class.getSimpleName());
    }
}