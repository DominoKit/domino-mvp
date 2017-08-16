#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwtmockito.GwtMockitoTestRunner;
import com.google.gwtmockito.WithClassesToStub;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.*;

import ${package}.${subpackage}.client.presenters.${module}Presenter;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.views.${module}ViewSpy;

import com.progressoft.brix.domino.test.api.client.DominoTestClient;

@RunWith(GwtMockitoTestRunner.class)
@WithClassesToStub(RootPanel.class)
public class ${module}ClientModuleTest{

    private ${module}PresenterSpy presenterSpy;
    private ${module}ViewSpy viewSpy;

    @Before
    public void setUp() {
        presenterSpy = new ${module}PresenterSpy();
        DominoTestClient.useModules(new ${module}ModuleConfiguration(), new ${module}UIModuleConfiguration())
                .replacePresenter(${module}Presenter.class, new ${module}PresenterSpy())
                .replaceView(${module}Presenter.class, new ${module}ViewSpy(), view -> viewSpy= (${module}ViewSpy) view)
                .start();
    }

    @Test
    public void nothing() throws Exception {

    }
}
