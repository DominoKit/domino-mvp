#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.api.client.ModuleConfigurator;
import org.dominokit.domino.api.client.annotations.ClientModule;
import ${package}.${subpackage}.client.ui.views.Bundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ${package}.${subpackage}.client.ui.views.${module}Bundle;

@ClientModule(name="${module}UI")
public class ${module}UIClientModule implements EntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(${module}UIClientModule.class);

	public void onModuleLoad() {
		LOGGER.info("Initializing ${module} frontend UI module ...");
		Bundle.INSTANCE.style().ensureInjected();
		new ModuleConfigurator().configureModule(new ${module}UIModuleConfiguration());
	}
}
