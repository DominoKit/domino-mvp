#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.core.client.EntryPoint;
import com.progressoft.brix.domino.api.client.ModuleConfigurator;
import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import ${package}.${subpackage}.client.ui.views.Bundle;

import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;

import ${package}.${subpackage}.client.ui.views.${module}Bundle;

@ClientModule(name="${module}UI")
public class ${module}UIClientModule implements EntryPoint {

	private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(${module}UIClientModule.class);

	public void onModuleLoad() {
		LOGGER.info("Initializing ${module} client UI module ...");
		Bundle.INSTANCE.style().ensureInjected();
		new ModuleConfigurator().configureModule(new ${module}UIModuleConfiguration());
	}
}
