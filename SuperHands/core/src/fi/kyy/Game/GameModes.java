package fi.kyy.Game;

public class GameModes {

	// NOT NEEDED ANYMORE

	private static final int MAX_DISTANCE = 5000;
	private static final int MAX_TIME = 5000;
	private static final int MAX_ROPE = 100;

	public static enum ModeTypes {
		FREE_STYLE("endless", MAX_TIME, -1, MAX_ROPE,
				"Swing for fun only, no Time Limit"), ROPE_USAGE("5 Ropes",
				MAX_TIME, MAX_DISTANCE, 5,
				"Get as far as possible with five ropes only"), TIME_30(
				"30 sec", 30, MAX_DISTANCE, MAX_ROPE,
				"Get as far as possible in 30 sec"), TIME_60("60 sec", 60,
				MAX_DISTANCE, MAX_ROPE, "Get as far as possible in 60 sec"), TIME_90(
				"90 sec", 90, MAX_DISTANCE, MAX_ROPE,
				"Get as far as possible in 90 sec"), DISTANCE_1("1000 m",
				MAX_TIME, 1000, MAX_ROPE, "Cover 1000 m as fast as possible"), DISTANCE_2(
				"2000 m", MAX_TIME, 2000, MAX_ROPE,
				"Cover 2000 m as fast as possible"), DISTANCE_3("3000 m",
				MAX_TIME, 3000, MAX_ROPE, "Cover 3000 m as fast as possible");

		public String modeName;
		public String modeDescription;
		public int maxTime;
		public int maxDistance;
		public int maxRope;

		private ModeTypes(String modeName, int maxTime, int maxDistance,
				int maxRope, String modeDescription) {
			this.modeName = modeName;
			this.modeDescription = modeDescription;
			this.maxRope = maxRope;
			this.maxTime = maxTime;
			this.maxDistance = maxDistance;
		}

		public static ModeTypes getModeByModeName(String modeName) {
			if (modeName != null) {
				for (ModeTypes type : ModeTypes.values()) {
					if (type.modeName.equalsIgnoreCase(modeName)) {
						// return type.ordinal();
						return type;
					}
				}
			}
			System.out.println("Error in GameModes ---- mode index problem");
			return null;
		}
	};

	public static enum Character {
		BOY("MONKEY", 1), FATTY("GIRAFFE", 2000), GIRL("COW", 3000);

		public String charName;
		public int cost;

		private Character(String charName, int cost) {
			this.charName = charName;
			this.cost = cost;
		}
	};

	public static enum World {
		FOREST("DYNAMIC", 1), WATER("DAWN", 1200), VOLCANO("DUSK", 2000);

		public String worldName;
		public int cost;

		private World(String worldName, int cost) {
			this.worldName = worldName;
			this.cost = cost;
		}
	};

	public static enum Ropes {
		ROPE1("ROPE 1", 1), ROPE2("ROPE 2", 1500), ROPE3("ROPE 3", 2000), ROPE4(
				"ROPE 4", 2500);

		public String ropeName;
		public int cost;

		private Ropes(String ropeName, int cost) {
			this.ropeName = ropeName;
			this.cost = cost;
		}
	};

	public static enum PowerUp {
		MAGNET1("MAGNET", 1), AIRPLANE("BIRD", 1500), HOTAIRBALLOON(
				"HOT AIR BALLOON", 2000);

		public String powerUpName;
		public int cost;

		private PowerUp(String powerUpName, int cost) {
			this.powerUpName = powerUpName;
			this.cost = cost;
		}
	};

}
