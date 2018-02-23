#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import com.progressoft.brix.domino.api.client.annotations.InjectContext;
import com.progressoft.brix.domino.api.client.annotations.Presenter;
import com.progressoft.brix.domino.api.client.mvp.presenter.BaseClientPresenter;
import ${package}.${subpackage}.client.views.${module}View;
import com.progressoft.brix.domino.api.shared.extension.MainExtensionPoint;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Presenter
public class ${module}Presenter extends BaseClientPresenter<${module}View> {

    private static final Logger LOGGER = LoggerFactory.getLogger(${module}Presenter.class);

    @InjectContext(extensionPoint=MainExtensionPoint.class)
    public void contributeToMainModule(MainContext context) {
        LOGGER.info("Main context received at presenter " + ${module}Presenter.class.getSimpleName());
    }
}