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
package org.dominokit.domino.api.client.annotations.presenter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to define a route for a presenter proxy
 *
 * <p>the annotation will generate the required classes to route to the presenter based on specified
 * token
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoRoute {

  /** @return String url token expression to use as a route for the presenter */
  String token() default "";

  /**
   * @return boolean, if true after routing the presenter the rout will be deleted and wont route
   *     again.
   */
  boolean routeOnce() default false;

  /**
   * @return boolean, if true this will restart the presenter life cycle and activate it again if
   *     the presenter is already active, otherwise if the presenter is active the routing wont
   *     happen
   */
  boolean reRouteActivated() default false;

  /**
   * @return boolean, true to generate a startup task to register the route, otherwise the route
   *     should be registered manually
   */
  boolean generateTask() default true;
}
