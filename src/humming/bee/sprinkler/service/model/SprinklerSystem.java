package humming.bee.sprinkler.service.model;

public class SprinklerSystem {

	private static boolean isEnabled = true;

	public static boolean isEnabled() {
		return isEnabled;
	}

	public static void setEnabled(boolean isEnabled) {
		SprinklerSystem.isEnabled = isEnabled;
	}

}
