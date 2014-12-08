package fi.kyy.Game.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;

import fi.kyy.Game.SuperHands;
import fi.kyy.Game.android.GameHelper.GameHelperListener;
import fi.kyy.Iab.android.IabHelper;
import fi.kyy.Iab.android.IabResult;
import fi.kyy.Iab.android.Inventory;
import fi.kyy.Iab.android.Purchase;
import fi.kyy.Services.ActionResolver;
import fi.kyy.Utils.PreferencesBean;

public class AndroidLauncher extends AndroidApplication implements
		GameHelperListener, ActionResolver {

	private IabHelper mHelper;

	private GameHelper gameHelper;
	private final static int REQUEST_CODE_UNUSED = 9002;
	static final int RC_REQUEST = 10001;

	public String SKU_REMOVE_ADS = "remove_ads";
	public String SKU_UNLOCK_WATERLAND = "unlock_waterland";
	public String SKU_UNLOCK_VOLCANO = "unlock_volcano";

	private InterstitialAd interstitial;

	private boolean waterLandUnlocked = false;
	private boolean volcanoPitUnlocked = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();

		//
		// KOKO RUUDUN MAINOS
		//

		interstitial = new InterstitialAd(this);
		interstitial.setAdUnitId("ca-app-pub-8443096434304370/8557172840");

		if (gameHelper == null) {
			gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
			gameHelper.enableDebugLog(true);
		}

		gameHelper.setup(this);

		AdRequest adRequest = new AdRequest.Builder().build();

		interstitial.loadAd(adRequest);

		initialize(new SuperHands(this), cfg);

		//
		// BANNERI
		//

		/*
		 * RelativeLayout layout = new RelativeLayout(this);
		 * RelativeLayout.LayoutParams gameViewParams = new
		 * RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
		 * RelativeLayout.LayoutParams.WRAP_CONTENT);
		 * //gameViewParams.bottomMargin = 150; requestWindowFeature(
		 * Window.FEATURE_NO_TITLE ); View gameView = initializeForView(new
		 * SuperHands(this), cfg);
		 * 
		 * if (gameHelper == null) { gameHelper = new GameHelper(this,
		 * GameHelper.CLIENT_GAMES); gameHelper.enableDebugLog(true); }
		 * 
		 * gameHelper.setup(this);
		 * 
		 * 
		 * 
		 * layout.addView(gameView, gameViewParams);
		 * 
		 * AdView adView = new AdView(this);
		 * adView.setAdUnitId("ca-app-pub-8443096434304370/4033769247");
		 * //adView.setAdUnitId("app-id"); adView.setAdSize(AdSize.BANNER);
		 * adView
		 * .setBackgroundColor(getResources().getColor(android.R.color.transparent
		 * ));
		 * 
		 * RelativeLayout.LayoutParams adParams = new
		 * RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
		 * RelativeLayout.LayoutParams.WRAP_CONTENT);
		 * 
		 * adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		 * adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		 * adParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		 * 
		 * 
		 * layout.addView(adView, adParams);
		 * 
		 * AdRequest adRequest = new AdRequest.Builder().build();
		 * adView.loadAd(adRequest);
		 * 
		 * setContentView(layout);
		 */

		// ...
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzTAAtnwkZf//i3S+cu3/"
				+ "i4MctwKUFNcLUjJ+FYUsAUjJGrfPNYMsH/gyk9WsYOD1Huh6JGBKKFij9QgK5Zo7LWXN2nYTgYS8n/bQWQLBC619o"
				+ "MNiqk1xJsLihSU2WigXJZaVzWEZBhM9ksSG7c6J6jOEs36XX+ssTQIQ4M6XJ0jvuXKxXDYORYBMXuYV3Bs/doYwn0n1y"
				+ "TUxiyiKxkyQGevhvHzqi9PlQuh4Wg6A3mCkeYwx9AkTvDlzUYubR90csXKqrPoadyh/U0zI3lxuVNC/5QGpbtgxX+l4+KL"
				+ "IDTk5a+dliFrVJIwt941oG/PTbKECdM7tOO1bxoS+BjukyQIDAQAB";

		// compute your public key and store it in base64EncodedPublicKey
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				if (!result.isSuccess()) {
					Toast.makeText(getApplicationContext(), "PROBLEM",
							Toast.LENGTH_SHORT).show();
				}
				Toast.makeText(getApplicationContext(), "SUCCESS",
						Toast.LENGTH_SHORT).show();

				processPurchases();
			}
		});

	}

	@Override
	public void displayInterstitial() {

		try {
			runOnUiThread(new Runnable() {
				public void run() {
					if (interstitial.isLoaded()) {
						interstitial.show();
						Toast.makeText(getApplicationContext(),
								"Showing Interstitial", Toast.LENGTH_SHORT)
								.show();
					} else {
						AdRequest interstitialRequest = new AdRequest.Builder()
								.build();
						interstitial.loadAd(interstitialRequest);
						Toast.makeText(getApplicationContext(),
								"Loading Interstitial", Toast.LENGTH_SHORT)
								.show();
					}
				}
			});
		} catch (Exception e) {
		}

	}

	@Override
	public boolean getSignedInGPGS() {
		return gameHelper.isSignedIn();
	}

	@Override
	public void loginGPGS() {
		try {
			runOnUiThread(new Runnable() {
				public void run() {
					gameHelper.beginUserInitiatedSignIn();
				}
			});
		} catch (final Exception ex) {
		}
	}

	@Override
	public void submitScoreGPGS(int score) {

		// Games.Leaderboards.submitScore(gameHelper.getApiClient(),
		// "CgkI9c-au6MCEAIQCg", score);
	}

	@Override
	public void unlockAchievementGPGS(String achievementId) {
		Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
	}

	@Override
	public void getLeaderboardGPGS() {
		if (gameHelper.isSignedIn()) {
			startActivityForResult(
					Games.Leaderboards.getLeaderboardIntent(
							gameHelper.getApiClient(), "CgkIpOKCkI0FEAIQBw"),
					REQUEST_CODE_UNUSED);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void getAchievementsGPGS() {
		if (gameHelper.isSignedIn()) {
			// startActivityForResult(Games.Achievements.getAchievementsIntent(gameHelper.getApiClient()),
			// 101);
		} else if (!gameHelper.isConnecting()) {
			loginGPGS();
		}
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mHelper != null)
			mHelper.dispose();
		mHelper = null;
	}

	@Override
	public int requestIabPurchase(int product) {

		if (product == 1) {
			mHelper.launchPurchaseFlow(this, SKU_REMOVE_ADS, RC_REQUEST,
					mPurchaseFinishedListener, "HANDLE_PAYLOADS");
		}
		if (product == 2) {
			mHelper.launchPurchaseFlow(this, SKU_UNLOCK_WATERLAND, RC_REQUEST,
					mPurchaseFinishedListener, "HANDLE_PAYLOADS");
		}
		if (product == 3) {
			mHelper.launchPurchaseFlow(this, SKU_UNLOCK_VOLCANO, RC_REQUEST,
					mPurchaseFinishedListener, "HANDLE_PAYLOADS");
		}

		return 0;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			if (purchase == null)
				return;
			Log.d("IAB", "Purchase finished: " + result + ", purchase: "
					+ purchase);

			if (mHelper == null)
				return;

			if (result.isFailure()) {
				// complain("Error purchasing: " + result);
				// setWaitScreen(false);
				return;
			}
			// if (!verifyDeveloperPayload(purchase)) {
			// //complain("Error purchasing. Authenticity verification failed.");
			// //setWaitScreen(false);
			// return;
			// }

			Log.d("IAB", "Purchase successful.");

			if (purchase.getSku().equals(SKU_REMOVE_ADS)) {

				PreferencesBean.setBooleanPreference("adsOff", true);

			}
			if (purchase.getSku().equals(SKU_UNLOCK_WATERLAND)) {

				setWaterUnlocked(true);
				PreferencesBean.setBooleanPreference("waterUnlocked", true);

			}
			if (purchase.getSku().equals(SKU_UNLOCK_VOLCANO)) {

				setVolcanoUnlocked(true);
				PreferencesBean.setBooleanPreference("volcanoUnlocked", true);

			}

		}
	};

	@Override
	public void processPurchases() {
		// TODO Auto-generated method stub

		mHelper.queryInventoryAsync(mGotInventoryListener);

	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d("IAB", "Query inventory finished.");
			Toast.makeText(getApplicationContext(), "SKANNATAAN OSTOT",
					Toast.LENGTH_SHORT).show();

			// Have we been disposed of in the meantime?
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				return;
			}

			// Do we have the upgrade?
			Purchase removeAdPurchase = inventory.getPurchase(SKU_REMOVE_ADS);
			Purchase waterPurchase = inventory
					.getPurchase(SKU_UNLOCK_WATERLAND);
			Purchase volcanoPurchase = inventory
					.getPurchase(SKU_UNLOCK_VOLCANO);

			if (removeAdPurchase != null) {
				PreferencesBean.setBooleanPreference("adsOff", true);
			}
			if (waterPurchase != null) {
				setWaterUnlocked(true);
				PreferencesBean.setBooleanPreference("waterUnlocked", true);
			}
			if (volcanoPurchase != null) {
				setVolcanoUnlocked(true);
				PreferencesBean.setBooleanPreference("volcanoUnlocked", true);
			}

		}
	};

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);

		if (mHelper != null) {
			// Pass on the activity result to the helper for handling
			if (mHelper.handleActivityResult(request, response, data)) {
				Log.d("IAB", "onActivityResult handled by IABUtil.");
			}
		}
	}

	public void setWaterUnlocked(boolean waterLandUnlocked) {
		this.waterLandUnlocked = waterLandUnlocked;
	}

	public void setVolcanoUnlocked(boolean volcanoUnlocked) {
		this.volcanoPitUnlocked = volcanoUnlocked;
	}

	@Override
	public boolean getWaterUnlocked() {
		return waterLandUnlocked;
	}

	@Override
	public boolean getVolcanoUnlocked() {
		return volcanoPitUnlocked;
	}

	public void backPressed() {
		finish();
	}

	@Override
	public void onStart() {
		super.onStart();
		gameHelper.onStart(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		gameHelper.onStop();
	}

}
