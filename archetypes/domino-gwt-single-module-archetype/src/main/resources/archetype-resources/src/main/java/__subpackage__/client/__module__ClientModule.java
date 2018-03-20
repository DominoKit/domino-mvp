#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.core.client.EntryPoint;
import org.dominokit.domino.api.client.ModuleConfigurator;
import org.dominokit.domino.api.client.annotations.ClientModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientModule(name="${module}")
public class ${module}ClientModule implements EntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(${module}ClientModule.class);

	public void onModuleLoad() {
		LOGGER.info("Initializing ${module} frontend module ...");
		new ModuleConfigurator().configureModule(new ${module}ModuleConfiguration());
	}
}
