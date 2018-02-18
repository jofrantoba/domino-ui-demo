package com.progressoft.brix.domino.badges.client;

import com.google.gwt.core.client.EntryPoint;
import com.progressoft.brix.domino.api.client.ModuleConfigurator;
import com.progressoft.brix.domino.api.client.annotations.ClientModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ClientModule(name="BadgesUI")
public class BadgesUIClientModule implements EntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(BadgesUIClientModule.class);

	public void onModuleLoad() {
		LOGGER.info("Initializing Badges frontend UI module ... ");
		new ModuleConfigurator().configureModule(new BadgesUIModuleConfiguration());
	}
}