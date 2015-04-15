package fi.kyy.Game;

import fi.kyy.Utils.PreferencesBean;

public class Mode {

	private static Mode mode = new Mode();

	private boolean giftTaken;
	private boolean tapJoyIntestitialAddShown;
	private int tapJoyIntestitialNumber;

	private String currentMode;
	private int currentDifficulty;
	private boolean soundOn;
	private boolean gameMusicOn;
	private boolean obstaclesOn;
	private boolean bloodOn;

	private int maxRopeUsed = 100;
	private int maxTime = 5000;
	private int maxDistance = 20000;

	private int currentRope;
	private int currentCharacter;
	private int currentWorld;
	private int currentMagnetActive;
	private int currentAirplaneActive;
	private int currentHotAirBalloonActive;

	private int currentCoins;

	public boolean rop0;
	public boolean rop1;
	public boolean rop2;
	public boolean rop3;

	public boolean character0;
	public boolean character1;
	public boolean character2;
	public boolean character3;
	public boolean character4;
	public boolean character5;
	public boolean character6;

	public boolean dayTime0;
	public boolean dayTime1;
	public boolean dayTime2;
	public boolean dayTime3;

	public int time1Best;
	public int time2Best;
	public int time3Best;
	public int distance1Best;
	public int distance2Best;
	public int distance3Best;
	public int freeBest;
	public int ropeBest;

	public String facebookIds;

	public boolean firstTimeLogin;

	public int distanceAchievement;
	public int coinAchievement;
	public int coinAchievementTotal;
	public int timeAchievement;

	public int distanceAchievementTotal;

	public long totalDistanceCovered;

	private Mode() {

		currentMode = PreferencesBean.getStringPreference("currentMode",
				GameModes.ModeTypes.FREE_STYLE.modeName);
		currentRope = PreferencesBean.getIntegerPreference("currentRope",
				GameModes.Ropes.ROPE1.ordinal());
		currentCharacter = PreferencesBean.getIntegerPreference(
				"currentCharacter", GameModes.Character.BOY.ordinal());
		currentWorld = PreferencesBean.getIntegerPreference("currentDayTime",
				GameModes.World.FOREST.ordinal());

		currentMagnetActive = PreferencesBean.getIntegerPreference(
				"currentMagnetActive", 0);
		currentAirplaneActive = PreferencesBean.getIntegerPreference(
				"currentAirplaneActive", 0);
		currentHotAirBalloonActive = PreferencesBean.getIntegerPreference(
				"currentHotAirBalloonActive", 0);

		soundOn = PreferencesBean.getBooleanPreferences("soundOn", true);
		gameMusicOn = PreferencesBean
				.getBooleanPreferences("gameMusicOn", true);
		obstaclesOn = PreferencesBean
				.getBooleanPreferences("obstaclesOn", true);
		bloodOn = PreferencesBean.getBooleanPreferences("bloodOn", true);

		currentCoins = PreferencesBean.getIntegerPreference("currentCoins", 0);

		rop0 = PreferencesBean.getBooleanPreferences("rop0", true);
		rop1 = PreferencesBean.getBooleanPreferences("rop1", false);
		rop2 = PreferencesBean.getBooleanPreferences("rop2", false);
		rop3 = PreferencesBean.getBooleanPreferences("rop3", false);

		giftTaken = PreferencesBean.getBooleanPreferences("giftTaken", false);
		tapJoyIntestitialAddShown = PreferencesBean.getBooleanPreferences(
				"tapJoyIntestitialAddShown", false);
		tapJoyIntestitialNumber = PreferencesBean.getIntegerPreference(
				"tapJoyIntestitialNumber", 0);

		character0 = PreferencesBean.getBooleanPreferences("character0", true);
		character1 = PreferencesBean.getBooleanPreferences("character1", false);
		character2 = PreferencesBean.getBooleanPreferences("character2", false);
		character3 = PreferencesBean.getBooleanPreferences("character3", false);
		character4 = PreferencesBean.getBooleanPreferences("character4", false);
		character5 = PreferencesBean.getBooleanPreferences("character5", false);
		character6 = PreferencesBean.getBooleanPreferences("character6", false);

		dayTime0 = PreferencesBean.getBooleanPreferences("dayTime0", true);
		dayTime1 = PreferencesBean.getBooleanPreferences("dayTime1", false);
		dayTime2 = PreferencesBean.getBooleanPreferences("dayTime2", false);
		dayTime3 = PreferencesBean.getBooleanPreferences("dayTime3", false);

		time1Best = PreferencesBean.getIntegerPreference("time1Best", 0);
		time2Best = PreferencesBean.getIntegerPreference("time2Best", 0);
		time3Best = PreferencesBean.getIntegerPreference("time3Best", 0);

		distance1Best = PreferencesBean.getIntegerPreference("distance1Best",
				500);
		distance2Best = PreferencesBean.getIntegerPreference("distance2Best",
				500);
		distance3Best = PreferencesBean.getIntegerPreference("distance3Best",
				500);

		freeBest = PreferencesBean.getIntegerPreference("distanceBest", 0);
		ropeBest = PreferencesBean.getIntegerPreference("distanceBest", 0);

		facebookIds = PreferencesBean.getStringPreference("facebookIds", " ");

		firstTimeLogin = PreferencesBean.getBooleanPreferences(
				"firstTimeLogin", true);

		distanceAchievement = PreferencesBean.getIntegerPreference(
				"distanceAchievement", 0);
		coinAchievement = PreferencesBean.getIntegerPreference(
				"coinAchievement", 0);
		coinAchievementTotal = PreferencesBean.getIntegerPreference(
				"coinAchievementTotal", 0);
		timeAchievement = PreferencesBean.getIntegerPreference(
				"timeAchievement", 0);

		distanceAchievementTotal = PreferencesBean.getIntegerPreference(
				"distanceAchievementTotal", 0);
		totalDistanceCovered = PreferencesBean.getLongPreference(
				"totalDistanceCovered", 0);

		// currentCoins = 50000;
		// currentMagnetActive = 0;
		// PreferencesBean.setIntegerPreference("coinAchievement", 0);
		// PreferencesBean.setIntegerPreference("distanceAchievement", 0);
		// currentMode = GameModes.ModeTypes.FREE_STYLE.modeName;
		// currentDayTime = 0;
		// currentCharacter = 1;
		// currentRope = 2;
		// PreferencesBean.setIntegerPreference("currentCoins", 300);
		// //
		//
		//
		// PreferencesBean.setBooleanPreference("rop0", true);
		// PreferencesBean.setBooleanPreference("rop1", false);
		// PreferencesBean.setBooleanPreference("rop2", false);
		// PreferencesBean.setBooleanPreference("rop3", false);
		//
		// PreferencesBean.setBooleanPreference("character0", true);
		// PreferencesBean.setBooleanPreference("character1", false);
		// PreferencesBean.setBooleanPreference("character2", false);
		// PreferencesBean.setBooleanPreference("character3", false);
		//
		// PreferencesBean.setBooleanPreference("dayTime0", true);
		// PreferencesBean.setBooleanPreference("dayTime1", false);
		// PreferencesBean.setBooleanPreference("dayTime2", false);
		// PreferencesBean.setBooleanPreference("dayTime3", false);
		//

		initializeSettings();
	}

	public static Mode getInstance() {
		return mode;
	}

	public void setFirstTimeLogin(boolean isFirstTimeLogin) {
		firstTimeLogin = isFirstTimeLogin;
		PreferencesBean
				.setBooleanPreference("firstTimeLogin", isFirstTimeLogin);
	}

	public void setTotalDistanceCovered(long totalDistace) {
		totalDistanceCovered = totalDistace;
		PreferencesBean.setLongPreference("totalDistanceCovered",
				totalDistanceCovered);
	}

	public long getTotalDistanceCovered() {
		return totalDistanceCovered;
	}

	public boolean getFirstTimeLogin() {
		return firstTimeLogin;
	}

	private void initializeSettings() {
		maxRopeUsed = GameModes.ModeTypes.getModeByModeName(currentMode).maxRope;
		maxTime = GameModes.ModeTypes.getModeByModeName(currentMode).maxTime;
		maxDistance = GameModes.ModeTypes.getModeByModeName(currentMode).maxDistance;
	}

	public void setCurrentMode(String modeName) {
		currentMode = modeName;
		PreferencesBean.setStringPreference("currentMode", currentMode);
		initializeSettings();
	}

	public void setGiftTaken(boolean giftTaken) {
		this.giftTaken = giftTaken;
		PreferencesBean.setBooleanPreference("giftTaken", giftTaken);

	}

	public boolean getGiftTaken() {
		return giftTaken;
	}

	public void setTapJoyIntestitialAdd(boolean isShown) {
		tapJoyIntestitialAddShown = isShown;
		PreferencesBean.setBooleanPreference("PreferencesBean",
				tapJoyIntestitialAddShown);
	}

	public boolean getTapJoyIntestitialAdd() {
		return tapJoyIntestitialAddShown;
	}

	public void setTapJoyIntestitialNumber(int num) {
		tapJoyIntestitialNumber = num;
		PreferencesBean.setIntegerPreference("tapJoyIntestitialNumber",
				tapJoyIntestitialNumber);
	}

	public int getTapJoyIntestitialNumber() {
		return tapJoyIntestitialNumber;
	}

	public void setCurrentCoins(int numCoins) {
		currentCoins = numCoins;
		PreferencesBean.setIntegerPreference("currentCoins", currentCoins);
	}

	public int getCurrentCoins() {

		return currentCoins;
	}

	/**
	 * call this after add is called
	 */
	public void setCurrentCoins() {
		currentCoins = PreferencesBean.getIntegerPreference("currentCoins",
				currentCoins);
	}

	// public void setFacebookIds(String id){
	// facebookIds = id;
	// PreferencesBean.setStringPreference("facebookIds", facebookIds);
	// }
	// public String getFacebookIds(){
	// return facebookIds;
	// }

	public void setRop(String ropeName, boolean bool) {
		if (ropeName.equals("rop0")) {
			rop0 = bool;
		} else if (ropeName.equals("rop1")) {
			rop1 = bool;
		} else if (ropeName.equals("rop2")) {
			rop2 = bool;
		} else if (ropeName.equals("rop3")) {
			rop3 = bool;
		}
		PreferencesBean.setBooleanPreference(ropeName, bool);
	}

	public void setCharacter(String characterName, boolean bool) {
		if (characterName.equals("character0")) {
			character0 = bool;
		} else if (characterName.equals("character1")) {
			character1 = bool;
		} else if (characterName.equals("character2")) {
			character2 = bool;
		} else if (characterName.equals("character3")) {
			character3 = bool;
		} else if (characterName.equals("character4")) {
			character4 = bool;
		} else if (characterName.equals("character5")) {
			character5 = bool;
		} else if (characterName.equals("character6")) {
			character6 = bool;
		}

		PreferencesBean.setBooleanPreference(characterName, bool);
	}

	public void setWorld(String dayTimeName, boolean bool) {
		if (dayTimeName.equals("dayTime0")) {
			dayTime0 = bool;
		} else if (dayTimeName.equals("dayTime1")) {
			dayTime1 = bool;
		} else if (dayTimeName.equals("dayTime2")) {
			dayTime2 = bool;
		} else if (dayTimeName.equals("dayTime3")) {
			dayTime3 = bool;
		}
		PreferencesBean.setBooleanPreference(dayTimeName, bool);
	}

	public boolean isCharacterUnLocked(int characterNumber) {
		switch (characterNumber) {
		case 0:
			return character0;
		case 1:
			return character1;
		case 2:
			return character2;
		case 3:
			return character3;
		case 4:
			return character4;
		case 5:
			return character5;
		case 6:
			return character6;

		}
		return false;
	}

	public boolean isRopeUnLocked(int num) {
		switch (num) {
		case 0:
			return rop0;
		case 1:
			return rop1;
		case 2:
			return rop2;
		case 3:
			return rop3;

		}
		return false;
	}

	public boolean isWorldUnLocked(int num) {
		switch (num) {
		case 0:
			return dayTime0;
		case 1:
			return dayTime1;
		case 2:
			return dayTime2;
		case 3:
			return dayTime3;

		}
		return false;
	}

	/**
	 * 
	 * @param mode
	 *            Enter GameModes.ModeTypes.--
	 * @param score
	 */
	public void setBestScore(GameModes.ModeTypes mode, int score) {
		switch (mode) {
		case TIME_30:
			if (time1Best < score) {
				time1Best = score;
				PreferencesBean.setIntegerPreference("time1Best", time1Best);
			}
			break;
		case TIME_60:
			if (time2Best < score) {
				time2Best = score;
				PreferencesBean.setIntegerPreference("time2Best", time2Best);
			}
			break;
		case TIME_90:
			if (time3Best < score) {
				time3Best = score;
				PreferencesBean.setIntegerPreference("time3Best", time3Best);
			}
			break;
		case DISTANCE_1:
			if (distance1Best > score) {
				distance1Best = score;
				PreferencesBean.setIntegerPreference("distance1Best",
						distance1Best);
			}
			break;
		case DISTANCE_2:
			if (distance2Best > score) {
				distance2Best = score;
				PreferencesBean.setIntegerPreference("distance2Best",
						distance2Best);
			}
			break;
		case DISTANCE_3:
			if (distance3Best > score) {
				distance3Best = score;
				PreferencesBean.setIntegerPreference("distance3Best",
						distance3Best);
			}
			break;
		case FREE_STYLE:
			if (freeBest < score) {
				freeBest = score;
				PreferencesBean.setIntegerPreference("freeBest", freeBest);
			}
			break;
		case ROPE_USAGE:
			if (ropeBest < score) {
				ropeBest = score;
				PreferencesBean.setIntegerPreference("ropeBest", ropeBest);
			}

		}
	}

	/**
	 * 
	 * @param mode
	 * @return
	 */
	public int getBestScore(GameModes.ModeTypes mode) {
		switch (mode) {
		case TIME_30:
			return time1Best;

		case TIME_60:
			return time2Best;

		case TIME_90:
			return time3Best;
		case DISTANCE_1:
			return distance1Best;
		case DISTANCE_2:
			return distance2Best;
		case DISTANCE_3:
			return distance3Best;
		case FREE_STYLE:
			return freeBest;
		case ROPE_USAGE:
			return ropeBest;

		}
		return 0;
	}

	public void setDistanceAchievement(int value) {
		if (distanceAchievement < value) {
			distanceAchievement = value;
			PreferencesBean.setIntegerPreference("distanceAchievement",
					distanceAchievement);
		}

	}

	public int getDistanceAchievement() {
		return distanceAchievement;
	}

	public void setCoinAchievement(int value) {
		if (coinAchievement < value) {
			coinAchievement = value;
			PreferencesBean.setIntegerPreference("coinAchievement",
					coinAchievement);
		}

	}

	public int getCoinAchievement() {
		return coinAchievement;
	}

	public void setCoinAchievementTotal(int value) {
		if (coinAchievementTotal < value) {
			coinAchievementTotal = value;
			PreferencesBean.setIntegerPreference("coinAchievementTotal",
					coinAchievementTotal);
		}

	}

	public int getCoinAchievementTotal() {
		return coinAchievementTotal;
	}

	public void setTimeAchievement(int value) {
		if (timeAchievement < value) {
			timeAchievement = value;
			PreferencesBean.setIntegerPreference("timeAchievement",
					timeAchievement);
		}

	}

	public int getTimeAchievement() {
		return timeAchievement;
	}

	public int getDistanceAchievementTotal() {
		return distanceAchievementTotal;
	}

	public void setDistanceAchievementTotal(int value) {
		if (distanceAchievementTotal < value) {
			distanceAchievementTotal = value;
			PreferencesBean.setIntegerPreference("distanceAchievementTotal",
					distanceAchievementTotal);
		}

	}

	public void soundOn(boolean bool) {
		soundOn = bool;
		PreferencesBean.setBooleanPreference("soundOn", soundOn);
	}

	public void gameMusicOn(boolean bool) {
		gameMusicOn = bool;
		PreferencesBean.setBooleanPreference("gameMusicOn", gameMusicOn);
	}

	public void obstaclesOn(boolean bool) {
		obstaclesOn = bool;
		PreferencesBean.setBooleanPreference("obstaclesOn", obstaclesOn);
	}

	public void bloodOn(boolean bool) {
		bloodOn = bool;
		PreferencesBean.setBooleanPreference("bloodOn", bloodOn);
	}

	public void setCurrentCharacter(int characterNumber) {
		if (isCharacterUnLocked(characterNumber)) {
			currentCharacter = characterNumber;
			PreferencesBean.setIntegerPreference("currentCharacter",
					currentCharacter);
		}
	}

	public void setCurrentRope(int ropeNumber) {
		if (isRopeUnLocked(ropeNumber)) {
			currentRope = ropeNumber;
			PreferencesBean.setIntegerPreference("currentRope", ropeNumber);
		}
	}

	public void setCurrentWorld(int dayTime) {
		if (isWorldUnLocked(dayTime)) {
			currentWorld = dayTime;
			PreferencesBean.setIntegerPreference("currentDayTime", dayTime);
		}
	}

	public void setCurrentMagnet(int powerUp) {
		if (powerUp <= GameConstant.MAX_UPGRADE) {
			currentMagnetActive = powerUp;
			PreferencesBean.setIntegerPreference("currentMagnetActive",
					currentMagnetActive);
		}

	}

	public void setCurrentAirplane(int powerUp) {
		if (powerUp < GameConstant.MAX_UPGRADE) {
			currentAirplaneActive = powerUp;
			PreferencesBean.setIntegerPreference("currentAirplaneActive",
					currentAirplaneActive);
		}

	}

	public void setCurrentHotAirBalloon(int powerUp) {
		if (powerUp < GameConstant.MAX_UPGRADE) {
			currentHotAirBalloonActive = powerUp;
			PreferencesBean.setIntegerPreference("currentHotAirBalloonActive",
					currentHotAirBalloonActive);
		}

	}

	public int getCurrentRope() {
		return currentRope;
	}

	public int getCurrentCharacter() {
		return currentCharacter;
	}

	public boolean soundOn() {
		return soundOn;
	}

	public boolean gameMusicOn() {
		return gameMusicOn;
	}

	public boolean obstaclesOn() {
		return obstaclesOn;
	}

	public boolean bloodOn() {
		return bloodOn;
	}

	public String getMode() {
		return currentMode;
	}

	public int getMaxRopeUsed() {
		return maxRopeUsed;
	}

	public int getDifficulty() {
		return currentDifficulty;
	}

	public int maxDistance() {
		return maxDistance;
	}

	public int maxTime() {
		return maxTime;
	}

	public int getCurrentWorld() {
		return currentWorld;
	}

	public int getCurrentMagnet() {
		return currentMagnetActive;
	}

	public int getCurrentAirplane() {
		return currentAirplaneActive;
	}

	public int getCurrentHotAirBalloon() {
		return currentHotAirBalloonActive;
	}

}
