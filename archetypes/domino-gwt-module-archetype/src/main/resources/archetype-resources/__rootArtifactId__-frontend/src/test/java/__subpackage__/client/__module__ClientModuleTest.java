#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
#set( $token = ${module.toLowerCase()} )
package ${package}.${subpackage}.client;

import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.test.api.client.ClientContext;
import org.dominokit.domino.test.api.client.annotations.FakeView;
import org.dominokit.domino.test.api.client.annotations.OnBeforeClientStart;
import org.dominokit.domino.test.api.client.annotations.PresenterSpy;
import org.dominokit.domino.test.api.client.annotations.TestConfig;
import org.dominokit.domino.test.api.client.DominoTestCase;
import org.dominokit.domino.test.api.client.DominoTestRunner;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.presenters.${module}Proxy;
import ${package}.${subpackage}.client.services.${module}ServiceFactory;
import ${package}.${subpackage}.client.views.Fake${module}View;
import ${package}.${subpackage}.shared.request.${module}Request;
import ${package}.${subpackage}.shared.response.${module}Response;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(DominoTestRunner.class)
@ClientModule(name = "Test${module}")
@TestConfig(modules= {${module}ModuleConfiguration.class})
public class ${module}ClientModuleTest extends DominoTestCase{

    @PresenterSpy(${module}Proxy.class)
    ${module}PresenterSpy presenterSpy;

    @FakeView(${module}Proxy.class)
    Fake${module}View fakeView;

    public ${module}ClientModuleTest() {
        super(new ${module}ClientModuleTest_Config());
    }

    @OnBeforeClientStart
    public void mockRequest(ClientContext clientContext) {
        clientContext.forRequest(${module}ServiceFactory.${module}Service_request.class)
            .returnResponse(new ${module}Response("Server message"));
    }

    @Test
    public void given${module}Module_whenRoutingTo${module}_thenShould${module}Proxy_PresenterShouldBeActivated() {
        clientContext.history().fireState("${token}");
        assertThat(presenterSpy.isActivated()).isTrue();
    }

    @Test
    public void given${module}ClientModule_when${module}ServerRequestIsSent_thenServerMessageShouldBeRecieved() {
        ${module}ServiceFactory.INSTANCE.request(new ${module}Request("client message"))
                .onSuccess(response -> assertThat(response.getServerMessage()).isEqualTo("Server message"))
                .onFailed(failedResponse -> fail(failedResponse.getThrowable().getMessage()))
                .send();
    }
}