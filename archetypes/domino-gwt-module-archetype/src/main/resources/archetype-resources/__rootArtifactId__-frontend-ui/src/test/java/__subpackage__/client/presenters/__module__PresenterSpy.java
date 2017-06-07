#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import ${package}.${subpackage}.client.presenters.Default${module}Presenter;
import com.progressoft.brix.domino.api.shared.extension.MainContext;

public class ${module}PresenterSpy extends Default${module}Presenter{

    private MainContext mainContext;

    @Override
    public void contributeToMainModule(MainContext context) {
        super.contributeToMainModule(context);
        this.mainContext=context;
    }

    public MainContext getMainContext() {
        return mainContext;
    }

    @Override
    protected String getConcrete() {
        return Default${module}Presenter.class.getCanonicalName();
    }
}
