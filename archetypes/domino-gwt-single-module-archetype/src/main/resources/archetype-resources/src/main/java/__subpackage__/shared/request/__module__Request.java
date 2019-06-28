#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.shared.request;

import org.dominokit.jacksonapt.annotation.JSONMapper;

@JSONMapper
public class ${module}Request {

    private String message;

    public ${module}Request() {
    }

    public ${module}Request(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
