package humming.bee.sprinkler.ui.activation;

import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TimerTask;


import humming.bee.sprinkler.service.SprinklerService;
import humming.bee.sprinkler.service.model.OverrideTemperatureConfiguration;
import humming.bee.sprinkler.service.model.Sprinkler;
import humming.bee.sprinkler.service.model.SprinklerConfiguration;
import humming.bee.sprinkler.service.model.SprinklerGroup;
import humming.bee.sprinkler.service.model.SprinklerGroupConfiguration;
import humming.bee.sprinkler.service.model.SprinklerSystem;
import humming.bee.sprinkler.service.model.TemperatureSensor;

public class SprinklerController extends TimerTask {

	public static String[] DAYS = new String[] { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
			"Saturday" };

	private SprinklerService service = new SprinklerService();

	private long durationInSeconds;

	public SprinklerController(long duration) {
		this.durationInSeconds = duration / 1000;
	}

	@Override
	public void run() {
		// run only if system is enabled
		if (SprinklerSystem.isEnabled()) {
			List<SprinklerGroup> groupList = service.getGroup();
			List<Sprinkler> sprinklerList = service.getSprinkler();
			Set<Integer> upperLimisprinklerGroupSet = new HashSet<Integer>();
			Set<Integer> lowerLimisprinklerGroupSet = new HashSet<Integer>();

			checkTemperatureOverride(upperLimisprinklerGroupSet, lowerLimisprinklerGroupSet);

			Calendar c1 = Calendar.getInstance();
			String currentDay = DAYS[c1.get(Calendar.DAY_OF_WEEK) - 1];

			Map<Integer, Long> groupWiseDuration = new HashMap<Integer, Long>();
			// initially set all group wise duration to 0 for all groups
			for (SprinklerGroup group : groupList) {
				groupWiseDuration.put(group.getGroupId(), 0L);
			}

			List<SprinklerGroupConfiguration> sprinklerGroupConfigList = service
					.getSprinklerGroupConfigurationByDay(currentDay);

			for (Sprinkler sprinkler : sprinklerList) {
				if (sprinkler.isByUser()) {
					if (sprinkler.getSprinklerStatus().equals("ON")) {
						addGroupWiseDurationForSprinkler(groupWiseDuration, sprinkler);
					}
					continue;
				}
				// consider the sprinklers which are not part of the upperLimit,
				// lowerLimit group
				if (!upperLimisprinklerGroupSet.contains(sprinkler.getGroupId())
						&& !lowerLimisprinklerGroupSet.contains(sprinkler.getGroupId())) {

					if (sprinkler.isFunctional()) {
						boolean isStatusUpdated = false;

						// get current time
						Time current = new Time(c1.get(Calendar.HOUR_OF_DAY), c1.get(Calendar.MINUTE),
								c1.get(Calendar.SECOND));

						// get sprinkler level configuration list for this
						// sprinkler and current day
						List<SprinklerConfiguration> sprinklerConfigList = service
								.getSprinklerConfigurationByDay(sprinkler.getSprinklerId(), currentDay);

						// if we have sprinkler configuration for current day
						// then only consider sprinkler configuration
						// ignore group level configuration
						if (sprinklerConfigList != null && !sprinklerConfigList.isEmpty()) {
							for (SprinklerConfiguration sprinklerConfig : sprinklerConfigList) {
								if (current.after(sprinklerConfig.getStartTime())
										&& current.before(sprinklerConfig.getEndTime())) {
									// update this sprinkler status as it
									// matches the sprinkler level configuration
									service.updateSprinklerStatusById(sprinkler.getSprinklerId(), "ON");
									addGroupWiseDurationForSprinkler(groupWiseDuration, sprinkler);
									isStatusUpdated = true;
									break;
								}
							}
						} else {
							// consider group level configuration only if
							// sprinkler leve configuration is not available for
							// this day
							for (SprinklerGroupConfiguration groupConfig : sprinklerGroupConfigList) {
								if (sprinkler.getGroupId() == groupConfig.getGroupId()) {
									if (current.after(groupConfig.getStartTime())
											&& current.before(groupConfig.getEndTime())) {
										// update this sprinkler status as it
										// matches the group level configuration
										service.updateSprinklerStatusById(sprinkler.getSprinklerId(), "ON");
										addGroupWiseDurationForSprinkler(groupWiseDuration, sprinkler);
										isStatusUpdated = true;
										break;
									}
								}
							}
						}

						if (!isStatusUpdated && !sprinkler.isByUser()) {
							// check if the current status is ON. if so then
							// change
							// it to OFF as it did not match
							// any sprinkler configuration
							if (sprinkler.getSprinklerStatus().equals("ON")) {
								//System.out
									//	.println(" setting status to off for sprinkler " + sprinkler.getSprinklerId());
								service.updateSprinklerStatusById(sprinkler.getSprinklerId(), "OFF");
								service.updateSprinklerGroupStatusOnly(sprinkler.getGroupId(), "OFF");
							}
						}
					}
				} else {
					if ((sprinkler.isFunctional()) && upperLimisprinklerGroupSet.contains(sprinkler.getGroupId())) {
						// add group wise duration for this sprinkler if the
						// sprinkler belongs to group
						// which has exceeded temperature overeride
						// configuration upper limit
						addGroupWiseDurationForSprinkler(groupWiseDuration, sprinkler);
					}
				}
			}
			// update all group wise duration in database
			updateGroupWiseDuration(groupWiseDuration, c1.get(Calendar.DAY_OF_MONTH), c1.get(Calendar.MONTH) + 1);
		}
	}

	private void updateGroupWiseDuration(Map<Integer, Long> groupWiseDuration, int dayOfMonth, int month) {
		Set<Entry<Integer, Long>> set = groupWiseDuration.entrySet();

		for (Entry<Integer, Long> entry : set) {
			int groupId = entry.getKey();
			long duration = entry.getValue();
			if (duration > 0) {
				service.updateSprinklerGroupEvent(month, dayOfMonth, groupId, duration);
			}
		}
	}

	private void addGroupWiseDurationForSprinkler(Map<Integer, Long> groupWiseDuration, Sprinkler sprinkler) {
		Long currentValue = groupWiseDuration.get(sprinkler.getGroupId());
		groupWiseDuration.put(sprinkler.getGroupId(), currentValue + durationInSeconds);
	}

	private void checkTemperatureOverride(Set<Integer> upperLimisprinklerGroupSet,
			Set<Integer> lowerLimisprinklerGroupSet) {

		List<OverrideTemperatureConfiguration> tempConfigurationList = service.getTemperatureConfiguration();
		for (int i = 0; i < tempConfigurationList.size(); i++) {
			OverrideTemperatureConfiguration tempConfig = tempConfigurationList.get(i);
			if (TemperatureSensor.getCurrentTemperature() > tempConfig.getTempUpperLimit()) {
				upperLimisprinklerGroupSet.add(tempConfig.getGroupId());
			} else if (TemperatureSensor.getCurrentTemperature() < tempConfig.getTempLowerLimit()) {
				lowerLimisprinklerGroupSet.add(tempConfig.getGroupId());
			}
		}

		for (Integer groupId : upperLimisprinklerGroupSet) {
			System.out.println("upper limit reached for group " + groupId);
			service.updateSprinklerGroupStatusById(groupId, "ON");
		}

		for (Integer groupId : lowerLimisprinklerGroupSet) {
			System.out.println("lower limit reached for group " + groupId);
			service.updateSprinklerGroupStatusById(groupId, "OFF");
		}
	}
}
