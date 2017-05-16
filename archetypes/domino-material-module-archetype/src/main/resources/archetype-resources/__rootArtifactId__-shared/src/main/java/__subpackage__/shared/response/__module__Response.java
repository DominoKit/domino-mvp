#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.shared.response;

import com.progressoft.brix.domino.api.shared.request.ServerResponse;

public class ${module}Response extends ServerResponse {

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
