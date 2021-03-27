/*
 * Copyright © 2019 Dominokit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dominokit.domino.test.api.client;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import java.lang.reflect.InvocationTargetException;
import org.dominokit.domino.test.api.client.annotations.AutoStart;
import org.dominokit.domino.test.api.client.annotations.StartServer;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class DominoTestRunner extends VertxUnitRunner {
  private final Class<?> testClass;

  public DominoTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
    this.testClass = klass;
  }

  @Override
  protected void invokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context)
      throws InvocationTargetException, IllegalAccessException {
    if (nonNull(fMethod.getAnnotation(Test.class))) {
      DominoTest dominoTest = (DominoTest) test;

      StartServer startServer = fMethod.getAnnotation(StartServer.class);
      if (isNull(startServer)) {
        startServer = testClass.getAnnotation(StartServer.class);
      }
      if (nonNull(startServer) && startServer.value()) {
        dominoTest.getTestClient().withServer(context);
      }

      AutoStart autoStart = fMethod.getAnnotation(AutoStart.class);
      if (isNull(autoStart) || autoStart.value()) {
        String configFile = isNull(autoStart) ? "config.json" : autoStart.configFile();
        dominoTest
            .getTestClient()
            .start(
                configFile,
                dominoTest.getAdditionalConfig(),
                () -> {
                  try {
                    doInvokeTestMethod(fMethod, test, context);
                  } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new IllegalStateException(e);
                  }
                });
      } else {
        try {
          doInvokeTestMethod(fMethod, test, context);
        } catch (InvocationTargetException | IllegalAccessException e) {
          throw new IllegalStateException(e);
        }
      }
    } else {
      doInvokeTestMethod(fMethod, test, context);
    }
  }

  private void doInvokeTestMethod(FrameworkMethod fMethod, Object test, TestContext context)
      throws InvocationTargetException, IllegalAccessException {
    super.invokeTestMethod(fMethod, test, context);
  }
}
