#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.requests;

import com.progressoft.brix.domino.api.client.request.ServerRequest;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.shared.request.${module}Request;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.annotations.Path;

@Request
@Path("${module}Request")
public class ${module}ServerRequest extends ServerRequest<${module}Request, ${module}Response> {
    public ${module}ServerRequest(${module}Request requestBean) {
        super(requestBean);
    }
}
