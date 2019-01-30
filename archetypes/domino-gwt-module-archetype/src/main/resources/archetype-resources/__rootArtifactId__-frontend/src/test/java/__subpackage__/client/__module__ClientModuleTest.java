#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.api.client.mvp.presenter.ViewablePresenterSupplier;
import org.dominokit.domino.test.api.client.ClientContext;
import org.dominokit.domino.test.api.client.DominoTestClient;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.presenters.${module}Proxy_Presenter_Config;
import ${package}.${subpackage}.client.requests.${module}RequestsFactory;
import ${package}.${subpackage}.client.views.Fake${module}View;
import ${package}.${subpackage}.shared.request.${module}Request;
import ${package}.${subpackage}.shared.response.${module}Response;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@ClientModule(name = "Test${module}")
public class ${module}ClientModuleTest {

    private ${module}PresenterSpy presenterSpy;
    private Fake${module}View fakeView;
    private ClientContext clientContext;

    @Before
    public void setUp() {
        DominoTestClient.useModules(new ${module}ModuleConfiguration(), new Test${module}ModuleConfiguration())
                .overrideConfig(() -> {
                        ${module}Proxy_Presenter_Config ${module}Proxy_presenter_config = new ${module}Proxy_Presenter_Config();
                        ${module}Proxy_presenter_config.setPresenterSupplier(new ViewablePresenterSupplier<>(false, () -> {
                        presenterSpy = new ${module}PresenterSpy();
                        return presenterSpy;
                    }));
                        ${module}Proxy_presenter_config.setViewSupplier(() -> {
                        fakeView = new Fake${module}View();
                        return fakeView;
                    });
                })
                .onClientStarted(clientContext -> this.clientContext = clientContext)
                .start();
    }

    @Test
    public void given${module}Module_whenRoutingTo${module}_thenShould${module}Proxy_PresenterShouldBeActivated() {
        clientContext.history().fireState("${module}");
        assertThat(presenterSpy.isActivated()).isTrue();
    }

    @Test
    public void given${module}ClientModule_when${module}ServerRequestIsSent_thenServerMessageShouldBeRecieved() {
        clientContext.forRequest(${module}RequestsFactory.${module}Requests_request.class)
                .returnResponse(new ${module}Response("Server message"));

        ${module}RequestsFactory.INSTANCE.request(new ${module}Request("client message"))
                .onSuccess(response -> assertThat(response.getServerMessage()).isEqualTo("Server message"))
                .onFailed(failedResponse -> fail(failedResponse.getThrowable().getMessage()))
                .send();
    }
}