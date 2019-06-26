#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.shared.services;

import org.dominokit.domino.rest.shared.request.service.annotations.RequestFactory;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.shared.request.${module}Request;

import javax.ws.rs.Path;

@RequestFactory
public interface ${module}Service {
    @Path("${module}Request")
    ${module}Response request(${module}Request request);
}
