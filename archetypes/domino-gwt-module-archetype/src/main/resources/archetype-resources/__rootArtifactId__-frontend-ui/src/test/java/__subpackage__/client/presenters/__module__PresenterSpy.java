#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import ${package}.${subpackage}.client.presenters.${module}Presenter;
import org.dominokit.domino.api.shared.extension.MainContext;

public class ${module}PresenterSpy extends ${module}Presenter{

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
        return ${module}Presenter.class.getCanonicalName();
    }
}
