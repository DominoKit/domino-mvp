#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import org.dominokit.domino.api.client.annotations.InjectContext;
import org.dominokit.domino.api.client.annotations.Presenter;
import org.dominokit.domino.api.client.mvp.presenter.ViewBaseClientPresenter;
import ${package}.${subpackage}.client.views.${module}View;
import org.dominokit.domino.api.shared.extension.MainExtensionPoint;
import org.dominokit.domino.api.shared.extension.MainContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Presenter
public class ${module}Presenter extends ViewBaseClientPresenter<${module}View> {

    private static final Logger LOGGER = LoggerFactory.getLogger(${module}Presenter.class);

    @InjectContext(extensionPoint=MainExtensionPoint.class)
    public void contributeToMainModule(MainContext context) {
        LOGGER.info("Main context received at presenter " + ${module}Presenter.class.getSimpleName());
    }
}