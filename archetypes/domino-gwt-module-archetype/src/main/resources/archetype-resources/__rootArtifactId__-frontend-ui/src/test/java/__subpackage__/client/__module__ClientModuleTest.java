#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwtmockito.GwtMockitoTestRunner;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenterSupplier;
import org.dominokit.domino.test.api.client.ClientContext;
import org.dominokit.domino.test.api.client.DominoTestClient;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.presenters.${module}Proxy_Presenter_Config;
import ${package}.${subpackage}.client.views.${module}ViewSpy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GwtMockitoTestRunner.class)
public class ${module}ClientModuleTest{

    private ${module}PresenterSpy presenterSpy;
    private ${module}ViewSpy viewSpy;
    private ClientContext clientContext;

    @Before
    public void setUp() {
        DominoTestClient.useModules(new ${module}ModuleConfiguration(), new ${module}UIModuleConfiguration())
                .overrideConfig(() -> {

                    ${module}Proxy_Presenter_Config ${module}Proxy_presenter_config = new ${module}Proxy_Presenter_Config();
                    ${module}Proxy_presenter_config.setPresenterSupplier(new ViewablePresenterSupplier<>(false, () -> {
                        presenterSpy = new ${module}PresenterSpy();
                        return presenterSpy;
                    }));

                    ${module}Proxy_presenter_config.setViewSupplier(() -> {
                        viewSpy = new ${module}ViewSpy();
                        return viewSpy;
                    });

                }).onClientStarted(clientContext -> this.clientContext = clientContext)
                .start();
    }

    @Test
    public void nothing() throws Exception {

    }

}
