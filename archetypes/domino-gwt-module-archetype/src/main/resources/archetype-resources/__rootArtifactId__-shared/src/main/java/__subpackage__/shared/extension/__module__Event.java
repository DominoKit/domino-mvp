#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.shared.extension;

import org.dominokit.domino.api.shared.extension.ActivationEvent;
<<<<<<< Updated upstream
import org.dominokit.domino.api.shared.extension.ActivationEventContext;

public class ${module}Event extends ActivationEvent {
    public ${module}Event(ActivationEventContext context) {
        super(context);
=======

public class ${module}Event extends ActivationEvent {
    public ${module}Event(boolean state) {
        super(state);
>>>>>>> Stashed changes
    }
}
