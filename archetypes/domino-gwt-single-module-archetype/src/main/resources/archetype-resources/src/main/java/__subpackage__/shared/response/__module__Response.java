#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.shared.response;

import org.dominokit.domino.api.shared.request.ResponseBean;

public class ${module}Response implements ResponseBean {

    private String serverMessage;

    public ${module}Response() {
    }

    public ${module}Response(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }
}
