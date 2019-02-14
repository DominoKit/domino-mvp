package org.dominokit.domino.api.server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

@Path("/{path:(.*)?}")
public class AppResource {

    @GET
    public Response index(@PathParam("path") String path) throws IOException {
        File root = new File("src/main/resources/" + AppGlobals.get().getConfig().getString("webroot", "app"));
        File f = new File(root, "index.html");
        if (!f.getCanonicalPath().startsWith(root.getCanonicalPath() + "/"))
            return Response.status(404).build();
        if (f.isDirectory())
            return Response.ok("Folder " + "index.html").build();
        if (!f.exists())
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(f).build();
    }
}