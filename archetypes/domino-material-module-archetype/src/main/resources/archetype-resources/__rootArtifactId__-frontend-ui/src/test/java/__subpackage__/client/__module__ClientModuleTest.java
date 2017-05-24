#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import ${package}.${subpackage}.client.presenters.${module}Presenter;
import com.progressoft.brix.domino.test.api.ModuleTestCase;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub(RootPanel.class)
public class ${module}ClientModuleTest extends ModuleTestCase{

    private ${module}PresenterSpy presenterSpy;
    private ${module}ViewSpy viewSpy;

    @Override
    public void setUp() {

        testModule.configureModule(new ${module}ModuleConfiguration());
        testModule.configureModule(new ${module}UIModuleConfiguration());

        testModule.replacePresenter(${module}Presenter.class.getCanonicalName(), () -> {
            presenterSpy=new ${module}PresenterSpy();
            return presenterSpy;
        });

        testModule.replaceView(${module}Presenter.class.getCanonicalName(), () -> {
            viewSpy=new ${module}ViewSpy();
            return viewSpy;
        });
    }

    @Test
    public void nothing() throws Exception {

    }

}
