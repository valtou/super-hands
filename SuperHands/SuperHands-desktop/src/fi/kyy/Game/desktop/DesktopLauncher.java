package fi.kyy.Game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import fi.kyy.Game.SuperHands;
import fi.kyy.Services.ActionResolver;

public class DesktopLauncher implements ActionResolver {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.vSyncEnabled = true;
		config.width = 800;
		config.height = 480;
		new LwjglApplication(new SuperHands(null), config);
	}

	@Override
	public boolean getSignedInGPGS() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void loginGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitScoreGPGS(int score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getLeaderboardGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAchievementsGPGS() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int requestIabPurchase(int product) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void processPurchases() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void displayInterstitial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getWaterUnlocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getVolcanoUnlocked() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
