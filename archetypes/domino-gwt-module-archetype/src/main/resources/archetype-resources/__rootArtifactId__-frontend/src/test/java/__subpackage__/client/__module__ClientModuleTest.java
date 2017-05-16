#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import org.junit.Test;

import static org.junit.Assert.*;

import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import ${package}.${subpackage}.client.presenters.${module}Presenter;
import ${package}.${subpackage}.client.requests.${module}ServerRequest;
import ${package}.${subpackage}.shared.request.${module}Request;
import ${package}.${subpackage}.shared.response.${module}Response;

import com.progressoft.brix.domino.test.api.ModuleTestCase;

@ClientModule(name="Test${module}")
public class ${module}ClientModuleTest extends ModuleTestCase{

    private ${module}PresenterSpy presenterSpy;
    private ${module}ViewSpy viewSpy;

    @Override
    public void setUp() {

        testModule.configureModule(new ${module}ModuleConfiguration());
        testModule.configureModule(new Test${module}ModuleConfiguration());

        testModule.replacePresenter(${module}Presenter.class.getCanonicalName(), () -> {
            presenterSpy=new ${module}PresenterSpy();
            return presenterSpy;
        });

        viewSpy=testModule.getView(${module}Presenter.class.getCanonicalName());
    }

    @Test
    public void given${module}Module_whenContributingToMainExtensionPoint_thenShouldReceiveMainContext() throws Exception {
        assertNotNull(presenterSpy.getMainContext());
    }

    @Test
    public void given${module}ClientModule_when${module}ServerRequestIsSent_thenServerMessageShouldBeRecieved()
            throws Exception {

        new ${module}ServerRequest(){
            @Override
            protected void process(${module}Presenter presenter, ${module}Request serverArgs, ${module}Response response) {
                super.process(presenter, serverArgs, response);
                assertEquals("Server message",response.getServerMessage());
            }

            @Override
            public String getKey() {
                return ${module}ServerRequest.class.getCanonicalName();
            }
        }.send();
    }
}
