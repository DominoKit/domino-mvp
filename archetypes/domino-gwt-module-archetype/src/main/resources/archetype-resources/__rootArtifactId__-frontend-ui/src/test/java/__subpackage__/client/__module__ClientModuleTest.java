#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import org.dominokit.domino.api.client.annotations.ClientModule;
import org.dominokit.domino.test.api.client.annotations.PresenterSpy;
import org.dominokit.domino.test.api.client.annotations.TestConfig;
import org.dominokit.domino.test.api.client.DominoTestCase;
import org.dominokit.domino.test.api.client.DominoTestRunner;
import ${package}.${subpackage}.client.presenters.${module}PresenterSpy;
import ${package}.${subpackage}.client.presenters.${module}Proxy;
import ${package}.${subpackage}.client.views.${module}ViewSpy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DominoTestRunner.class)
@ClientModule(name = "Test${module}")
@TestConfig(modules= {${module}ModuleConfiguration.class})
public class ${module}ClientModuleTest extends DominoTestCase {

    @PresenterSpy(${module}Proxy.class)
    ${module}PresenterSpy presenterSpy;

    public ${module}ClientModuleTest() {
        super(new ${module}ClientModuleTest_Config());
    }

    @Test
    public void nothing() throws Exception {

    }

}
