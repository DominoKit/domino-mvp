package org.dominokit.domino.test.api.client;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.dominokit.domino.test.api.client.annotations.AutoStart;
import org.dominokit.domino.test.api.client.annotations.StartServer;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class DominoTestRunner extends VertxUnitRunner {
    private final Class<?> testClass;

    public DominoTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
        this.testClass = klass;
    }

    @Override
    protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context) throws InvocationTargetException, IllegalAccessException {
        if (nonNull(fMethod.getAnnotation(Test.class))) {
            AutoStart autoStart = fMethod.getAnnotation(AutoStart.class);
            if (isNull(autoStart) || autoStart.value()) {

                DominoTest dominoTest = (DominoTest) test;
                String configFile = isNull(autoStart) ? "config.json" : autoStart.configFile();

                StartServer startServer = fMethod.getAnnotation(StartServer.class);
                if (nonNull(startServer) && startServer.value()) {
                    dominoTest.getTestClient().withServer(dominoTest.getTestContext()).start(configFile, dominoTest.getAdditionalConfig());
                } else {
                    dominoTest.getTestClient().start(configFile, dominoTest.getAdditionalConfig());
                }
            }
        }
        super.invokeTestMethod(fMethod, test, context);
    }
}
