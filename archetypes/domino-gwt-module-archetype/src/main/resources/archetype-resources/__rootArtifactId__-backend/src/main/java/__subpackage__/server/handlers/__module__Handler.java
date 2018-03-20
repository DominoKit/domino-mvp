#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.server.handlers;

import org.dominokit.domino.api.server.handler.Handler;
import org.dominokit.domino.api.server.handler.RequestHandler;
import org.dominokit.domino.api.server.context.ExecutionContext;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.shared.request.${module}Request;

import java.util.logging.Logger;

@Handler("${module}Request")
public class ${module}Handler implements RequestHandler<${module}Request, ${module}Response> {
    private static final Logger LOGGER= Logger.getLogger(${module}Handler.class.getName());
    @Override
    public void handleRequest(ExecutionContext<${module}Request, ${module}Response> executionContext) {
        LOGGER.info("message recieved from client : "+executionContext.getRequestBean().getMessage());
        executionContext.end(new ${module}Response("Server message"));
    }
}
