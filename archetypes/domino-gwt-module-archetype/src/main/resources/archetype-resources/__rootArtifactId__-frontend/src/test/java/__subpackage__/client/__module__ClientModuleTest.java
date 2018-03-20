#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;
import com.google.gwtmockito.GwtMockitoTestRunner;

import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.test.api.client.DominoTestClient;
import org.dominokit.domino.test.api.client.ClientContext;
import ${package}.${subpackage}.client.presenters.${module}Presenter;
import ${package}.${subpackage}.client.requests.${module}RequestsFactory;
import ${package}.${subpackage}.shared.request.${module}Request;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.views.Fake${module}View;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ClientModule(name="Test${module}")
@RunWith(GwtMockitoTestRunner.class)
public class ${module}ClientModuleTest{

    private ${module}PresenterSpy presenterSpy;
    private Fake${module}View fakeView;
    private ClientContext clientContext;

    @Before
    public void setUp() {
        presenterSpy = new ${module}PresenterSpy();
        DominoTestClient.useModules(new ${module}ModuleConfiguration(), new Test${module}ModuleConfiguration())
            .replacePresenter(${module}Presenter.class, presenterSpy)
            .viewOf(${module}Presenter.class, view -> fakeView= (Fake${module}View) view)
            .onStartCompleted(clientContext -> this.clientContext = clientContext)
            .start();
    }

    @Test
    public void given${module}Module_whenContributingToMainExtensionPoint_thenShouldReceiveMainContext() {
        assertThat(presenterSpy.getMainContext()).isNotNull();
    }

    @Test
    public void given${module}ClientModule_when${module}ServerRequestIsSent_thenServerMessageShouldBeRecieved() {
        clientContext.forRequest(${module}RequestsFactory.${module}Requests_request.class)
            .returnResponse(new ${module}Response("Server message"));

        ${module}RequestsFactory.INSTANCE.request(new ${module}Request("client message")).onSuccess(response -> assertThat(response.getServerMessage()).isEqualTo("Server message"))
        .onFailed(failedResponse -> fail(failedResponse.getError().getMessage()))
        .send();
    }
}
