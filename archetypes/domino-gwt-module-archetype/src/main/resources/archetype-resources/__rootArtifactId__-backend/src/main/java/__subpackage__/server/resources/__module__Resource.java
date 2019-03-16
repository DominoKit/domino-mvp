#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.server.resources;

import ${package}.${subpackage}.shared.response.${module}Response;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/")
public class ${module}Resource {

    @Path("${module}Request")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public ${module}Response sayHello() {
        return new ${module}Response("Hello from server");
    }
}