#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.requests;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.shared.request.${module}Request;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.annotations.Path;

@Request
@Path("${module}Request")
public class ${module}ServerRequest extends ClientServerRequest<${module}Request, ${module}Response> {

}
