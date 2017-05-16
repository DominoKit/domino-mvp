#set($symbol_pound='#')
#set($symbol_dollar='$')
#set($symbol_escape='\' )
package ${package}.${subpackage}.client.presenters;

import com.progressoft.brix.domino.api.client.annotations.Presenter;
import com.progressoft.brix.domino.api.client.mvp.presenter.BaseClientPresenter;
import ${package}.${subpackage}.client.views.${module}View;
import com.progressoft.brix.domino.api.shared.extension.MainContext;
import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;

@Presenter
public class Default${module}Presenter extends BaseClientPresenter<${module}View> implements ${module}Presenter {

    private static final CoreLogger LOGGER=CoreLoggerFactory.getLogger(Default${module}Presenter.class);

    @Override
    public void contributeToMainModule(MainContext context){
        LOGGER.info("Main context received at presenter "+Default${module}Presenter.class.getSimpleName());
    }
}