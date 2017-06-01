#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import com.progressoft.brix.domino.api.client.annotations.Presenter;
import com.progressoft.brix.domino.api.client.mvp.presenter.BaseClientPresenter;
import ${package}.${subpackage}.client.views.${module}View;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Presenter
public class Default${module}Presenter extends BaseClientPresenter<${module}View> implements ${module}Presenter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Default${module}Presenter.class);

    @Override
    public void contributeToMainModule(MainContext context) {
        LOGGER.info("Main context received at presenter " + Default${module}Presenter.class.getSimpleName());
    }
}