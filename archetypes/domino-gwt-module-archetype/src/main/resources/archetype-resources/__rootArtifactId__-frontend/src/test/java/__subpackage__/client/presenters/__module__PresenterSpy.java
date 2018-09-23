#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.presenters;

import ${package}.${subpackage}.client.presenters.${module}Presenter;
import org.dominokit.domino.api.shared.extension.MainEventContext;

public class ${module}PresenterSpy extends ${module}Presenter{

    private MainEventContext mainContext;

    @Override
    public void listenToMainEvent(MainEventContext context) {
        super.listenToMainEvent(context);
        this.mainContext=context;
    }

    public MainEventContext getMainContext() {
        return mainContext;
    }

    @Override
    protected String getConcrete() {
        return ${module}Presenter.class.getCanonicalName();
    }
}
