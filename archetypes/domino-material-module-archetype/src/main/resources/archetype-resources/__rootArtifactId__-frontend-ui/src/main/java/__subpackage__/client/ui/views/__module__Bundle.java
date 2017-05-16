#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.ui.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ImageResource;

public interface ${module}Bundle extends ClientBundle{

    interface Style extends CssResource {
        String ${module}();
    }

    @Source("css/${module}.gss")
    Style style();

    @Source("text/welcome.txt")
    ExternalTextResource welcome();

}