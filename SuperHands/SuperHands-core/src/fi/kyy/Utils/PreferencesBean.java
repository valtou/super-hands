package fi.kyy.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesBean {

	static Preferences level = Gdx.app.getPreferences("SUPERHANDS");
	public static int DIFF_DONE[] = new int[3];

	public static void setIntegerPreference(String key, int value) {
		level.putInteger(key, value);
		level.flush();
	}

	public static int getIntegerPreference(String key, int correspond) {
		correspond = level.getInteger(key, correspond);
		level.flush();
		return correspond;
	}

	public static void setStringPreference(String key, String value) {
		level.putString(key, value);
		level.flush();
	}

	public static void setFloatPreference(String key, float value) {
		level.putFloat(key, value);
		level.flush();
	}

	public static float getFloatPreference(String key, float correspond) {
		correspond = level.getFloat(key, correspond);
		level.flush();
		return correspond;
	}

	public static String getStringPreference(String key, String value) {
		value = level.getString(key, value);
		level.flush();
		return value;
	}

	public static void setBooleanPreference(String key, boolean value) {
		level.putBoolean(key, value);
		level.flush();
	}

	public static boolean getBooleanPreferences(String key, boolean correspond) {
		correspond = level.getBoolean(key, correspond);
		level.flush();
		return correspond;
	}

	public static long getLongPreference(String key, long correspond) {
		correspond = level.getLong(key, correspond);
		level.flush();
		return correspond;
	}

	public static void setLongPreference(String key, long correspond) {
		level.putLong(key, correspond);
		level.flush();
	}
}
