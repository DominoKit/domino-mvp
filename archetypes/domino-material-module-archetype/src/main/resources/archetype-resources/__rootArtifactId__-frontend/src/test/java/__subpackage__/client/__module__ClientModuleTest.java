#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwtmockito.GwtMockitoTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Before;

import static org.junit.Assert.*;

import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import com.progressoft.brix.domino.test.api.client.DominoTestClient;
import com.progressoft.brix.domino.test.api.client.ClientContext;
import com.progressoft.brix.domino.api.shared.request.FailedServerResponse;
import ${package}.${subpackage}.client.presenters.${module}Presenter;
import ${package}.${subpackage}.client.requests.${module}ServerRequest;
import ${package}.${subpackage}.shared.request.${module}Request;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.views.Fake${module}View;


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
    public void given${module}Module_whenContributingToMainExtensionPoint_thenShouldReceiveMainContext() throws Exception {
        assertNotNull(presenterSpy.getMainContext());
    }

    @Test
    public void given${module}ClientModule_when${module}ServerRequestIsSent_thenServerMessageShouldBeRecieved() {
        clientContext.forRequest(new ${module}Request().getKey()).returnResponse(new ${module}Response("Server message"));
        new ${module}ServerRequest(){
            @Override
            protected void process(${module}Presenter presenter, ${module}Request serverArgs, ${module}Response response) {
                super.process(presenter, serverArgs, response);
                assertEquals("Server message",response.getServerMessage());
            }

            @Override
            public void processFailed(${module}Presenter presenter, {module}Request serverArgs,
                FailedServerResponse failedResponse) {
                fail(failedResponse.getError().getMessage());
            }

            @Override
            public String getKey() {
                return ${module}ServerRequest.class.getCanonicalName();
            }
        }.send();
    }
}
