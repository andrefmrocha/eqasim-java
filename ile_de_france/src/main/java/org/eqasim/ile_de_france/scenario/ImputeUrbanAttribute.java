package org.eqasim.ile_de_france.scenario;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.eqasim.core.scenario.cutter.extent.ScenarioExtent;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;

public class ImputeUrbanAttribute {
	private final static Logger logger = Logger.getLogger(ImputeUrbanAttribute.class);

	private final ScenarioExtent extent;

	public ImputeUrbanAttribute(ScenarioExtent extent) {
		this.extent = extent;
	}

	public void run(Population population) throws MalformedURLException, IOException {
		int totalNumber = population.getPersons().size();
		int currentNumber = 0;

		long lastTime = 0;

		for (Person person : population.getPersons().values()) {
			for (Plan plan : person.getPlans()) {
				for (PlanElement element : plan.getPlanElements()) {
					if (element instanceof Activity) {
						Activity activity = (Activity) element;

						if (extent.isInside(activity.getCoord()) && !activity.getType().contains("interaction")) {
							activity.getAttributes().putAttribute("isUrban", true);
						}
					}
				}
			}

			long currentTime = System.currentTimeMillis();
			currentNumber++;

			if (currentTime - lastTime > 1000 || currentNumber == totalNumber) {
				lastTime = currentTime;
				logger.info(String.format("Imputing urban attribute ... (%d/%d) %.2f%%", currentNumber, totalNumber,
						100.0 * currentNumber / totalNumber));
			}
		}
	}
}