#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.requests;

import com.progressoft.brix.domino.api.client.request.ClientServerRequest;
import ${package}.${subpackage}.shared.response.${module}Response;
import ${package}.${subpackage}.shared.request.${module}Request;
import ${package}.${subpackage}.client.presenters.${module}Presenter;
import com.progressoft.brix.domino.api.client.annotations.Request;
import com.progressoft.brix.domino.api.client.annotations.HandlerPath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Request
@HandlerPath("${module}Request")
public class ${module}ServerRequest extends ClientServerRequest<${module}Presenter, ${module}Request, ${module}Response> {

    private static final Logger LOGGER= LoggerFactory.getLogger(${module}ServerRequest.class);

    @Override
    protected void process(${module}Presenter presenter, ${module}Request serverRequest, ${module}Response response) {
        LOGGER.info("Message recieved from server : "+response.getServerMessage());
    }

    @Override
    public ${module}Request buildArguments() {
        return new ${module}Request("client message");
    }
}
