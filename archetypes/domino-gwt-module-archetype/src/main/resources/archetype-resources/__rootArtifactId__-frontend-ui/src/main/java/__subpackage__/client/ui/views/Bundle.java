#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client.ui.views;

import com.google.gwt.core.client.GWT;

public class Bundle {

    public static final ${module}Bundle INSTANCE= GWT.create(${module}Bundle.class);

    private Bundle() {
    }
}
