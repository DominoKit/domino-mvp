#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.${subpackage}.client;

import com.google.gwt.core.client.EntryPoint;
import com.progressoft.brix.domino.api.client.ModuleConfigurator;
import com.progressoft.brix.domino.api.client.annotations.ClientModule;

import com.progressoft.brix.domino.logger.client.CoreLogger;
import com.progressoft.brix.domino.logger.client.CoreLoggerFactory;


@ClientModule(name="${module}")
public class ${module}ClientModule implements EntryPoint {

	private static final CoreLogger LOGGER = CoreLoggerFactory.getLogger(${module}ClientModule.class);

	public void onModuleLoad() {
		LOGGER.info("Initializing ${module} client module ...");
		new ModuleConfigurator().configureModule(new ${module}ModuleConfiguration());
	}
}
