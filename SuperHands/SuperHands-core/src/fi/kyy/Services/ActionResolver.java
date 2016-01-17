package fi.kyy.Services;

public interface ActionResolver {
	
	public boolean getSignedInGPGS();

	public void loginGPGS();

	public void submitScoreGPGS(int score);

	public void unlockAchievementGPGS(String achievementId);

	public void getLeaderboardGPGS();

	public void getAchievementsGPGS();

	public int requestIabPurchase(int product);
	
	public void processPurchases();
	
	public void displayInterstitial();
	
	public boolean getWaterUnlocked();
	
	public boolean getVolcanoUnlocked();
	
	public void openFacebook();
	
}
