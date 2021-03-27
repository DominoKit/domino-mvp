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
package org.dominokit.domino.plugins.resteasy;

import java.io.File;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("/{path:(.*)?}")
public class AppResource {

  @GET
  public Response index(@PathParam("path") String path) throws IOException {
    File root =
        new File("src/main/resources/" + AppGlobals.get().getConfig().getString("webroot", "app"));
    File f = new File(root, "index.html");
    if (!f.getCanonicalPath().startsWith(root.getCanonicalPath() + "/"))
      return Response.status(404).build();
    if (f.isDirectory()) return Response.ok("Folder " + "index.html").build();
    if (!f.exists()) return Response.status(Response.Status.NOT_FOUND).build();
    return Response.ok(f).build();
  }
}
