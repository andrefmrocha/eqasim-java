package org.eqasim.projects.astra16.service_area;

import java.util.Collections;

import org.eqasim.projects.astra16.AstraConfigGroup;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.controler.AbstractModule;

import com.google.inject.Provides;
import com.google.inject.Singleton;

import ch.ethz.matsim.av.network.AVNetworkFilter;

public class ServiceAreaModule extends AbstractModule {
	@Override
	public void install() {
		bind(AVNetworkFilter.class).to(ServiceAreaFilter.class);
	}

	@Provides
	@Singleton
	public ServiceAreaFilter provideServiceAreaFilter(ServiceArea serviceArea) {
		return new ServiceAreaFilter(serviceArea);
	}

	@Provides
	@Singleton
	public ServiceArea provideServiceArea(AstraConfigGroup astraConfig, Network network) {
		if (astraConfig.getFleetSize() == 0) {
			return new ServiceArea(Collections.emptySet());
		} else {
			return ServiceArea.load(astraConfig.getOperatingAreaIndexAttribute(), network,
					ConfigGroup.getInputFileURL(getConfig().getContext(), astraConfig.getOperatingAreaPath()));
		}
	}
}