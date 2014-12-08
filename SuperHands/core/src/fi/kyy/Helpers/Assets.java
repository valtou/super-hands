package fi.kyy.Helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import fi.kyy.Game.GameConstant;
import fi.kyy.Utils.AnimatedSprite;
import fi.kyy.Utils.PreferencesBean;

public class Assets {

	public static Texture logoTexture;

	public static Texture coinTexture, volcanoBGTexture, forestBGTexture,
			waterBGTexture, caveBGTexture;

	public static TextureRegion adderLogo, menuWindow, menuBG1, menuBG2,
			menuBG3, left, leftBlank, right, rightBlank, volcanoBG,
			finishedWindow, worldLock, noLock, waterLock, volcanoLock,
			coinImage, coinImageNot, trueClick;

	public static TiledMap tileMap, tileMap2;

	// PLAYER
	public static AtlasSprite voodooHead, voodooTorso, voodooLeftLeg,
			voodooRightLeg, voodooRightArm, voodooLeftArm;
	public static AtlasSprite roboHead, roboTorso, roboLeftLeg, roboRightLeg,
			roboRightArm, roboLeftArm;
	public static Sprite rope;

	public static TextureAtlas voodoo, robo, buttons, vines, worlds, coins,
			worldslocked, amount;

	public static TextureRegion coin, superHandsTitle, ropeJoint,
			backgroundRegion;

	// MENU
	public static TextureRegion levelPlayed1, levelLocked1, levelPlayed2,
			levelLocked2, lock, allCoins, notAllCoins, levelPlayed3,
			levelLocked3;

	public static TextureRegion adsOff, buyWater, buyVolcano, buySkip;

	public static AtlasSprite playButton, optionsButton, tutorialButton,
			achievementsButton, shopButton, exitButton, facebookButton,
			achievementsButton_big, shopButton_big, playButton_small,
			menuButton_small, retryButton_small, nextButton_small,
			retryButton_small1;

	public static AtlasSprite modeButton, characterButton, leftButton,
			rightButton, backButton, worldButton, pauseButton, menuButton;

	public static TextureRegion coin1, coin2, coin3, coin4;

	public static Sprite[] vine = new Sprite[4];
	public static Sprite[] vineFlipped = new Sprite[4];

	public static BitmapFont headingFont;

	public static Sprite forest, water, volcano, waterLocked, volcanoLocked;

	public static TextureRegion black, completed, levelselect, worldselect,
			settings, shop;

	public static Animation coinAnimation;

	public static BitmapFont font;

	public static Music volcanoTheme, forestTheme, completedTheme, mainTheme,
			waterTheme, caveTheme;

	public static Sound dead, finish, menuSwoosh, ropeSound, coinSound,
			buttonSound;

	public static boolean adsBought;
	public static boolean waterBought;
	public static boolean volcanoBought;
	public static int skips;

	public static void load() {

		adsBought = PreferencesBean.getBooleanPreferences("ads", false);
		waterBought = PreferencesBean.getBooleanPreferences("waterUnlocked",
				false);
		volcanoBought = PreferencesBean.getBooleanPreferences(
				"volcanoUnlocked", false);

		// SPLASH SCREEN
		logoTexture = new Texture(Gdx.files.internal("assets/logo.png"));
		logoTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		adderLogo = new TextureRegion(logoTexture, 0, 0, 512, 114);

		// LEVEL TEXTURES
		vines = new TextureAtlas(Gdx.files.internal("assets/haha.pack"));

		forestBGTexture = new Texture(
				Gdx.files.internal("assets/lvl/bg/forest.jpg"));
		forestBGTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		waterBGTexture = new Texture(
				Gdx.files.internal("assets/lvl/bg/water.jpg"));
		waterBGTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		caveBGTexture = new Texture(
				Gdx.files.internal("assets/lvl/bg/cave.jpg"));
		caveBGTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		volcanoBGTexture = new Texture(
				Gdx.files.internal("assets/lvl/bg/volcano.jpg"));
		volcanoBGTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		// PLAYER
		rope = new Sprite(new Texture("assets/player/rope.png"));

		robo = new TextureAtlas("assets/player/robo.pack");
		voodoo = new TextureAtlas("assets/player/voodoo.pack");

		voodooLeftLeg = new AtlasSprite(voodoo.findRegion("leg-b"));
		voodooLeftArm = new AtlasSprite(voodoo.findRegion("arm-b"));
		voodooLeftArm.flip(false, true);
		voodooRightLeg = new AtlasSprite(voodoo.findRegion("leg-f"));
		voodooRightArm = new AtlasSprite(voodoo.findRegion("arm-f"));
		voodooRightArm.flip(false, true);
		voodooTorso = new AtlasSprite(voodoo.findRegion("body"));
		voodooHead = new AtlasSprite(voodoo.findRegion("head"));

		roboHead = new AtlasSprite(robo.findRegion("roboHead"));
		roboTorso = new AtlasSprite(robo.findRegion("roboBody"));
		roboRightLeg = new AtlasSprite(robo.findRegion("roboLegFront"));
		roboLeftLeg = new AtlasSprite(robo.findRegion("roboLegBack"));
		roboLeftArm = new AtlasSprite(robo.findRegion("roboArmBack"));
		roboLeftArm.flip(false, true);
		roboRightArm = new AtlasSprite(robo.findRegion("roboArmFront"));
		roboRightArm.flip(false, true);

		// COIN
		coins = new TextureAtlas("assets/coin/coins.pack");

		coin1 = new TextureRegion(coins.findRegion("1"));
		coin2 = new TextureRegion(coins.findRegion("2"));
		coin3 = new TextureRegion(coins.findRegion("3"));
		coin4 = new TextureRegion(coins.findRegion("4"));

		coinAnimation = new Animation(1 / 8f, coin1, coin2, coin3, coin4);
		coinAnimation.setPlayMode(Animation.PlayMode.LOOP);

		coin = new TextureRegion(new Texture(
				Gdx.files.internal("assets/coin/coin1.png")));

		coinImage = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/coinComplete.png")));
		coinImageNot = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/coinCompleteNotAll.png")));

		amount = new TextureAtlas("assets/ui/amount.pack");

		// MENUBUTTONSANDBG
		buttons = new TextureAtlas(
				Gdx.files.internal("assets/buttons/buttons.pack"));

		playButton = new AtlasSprite(buttons.findRegion("play"));
		optionsButton = new AtlasSprite(buttons.findRegion("settings"));
		tutorialButton = new AtlasSprite(buttons.findRegion("help"));
		achievementsButton = new AtlasSprite(buttons.findRegion("achievements"));
		shopButton = new AtlasSprite(buttons.findRegion("shop"));
		exitButton = new AtlasSprite(buttons.findRegion("shop"));
		facebookButton = new AtlasSprite(buttons.findRegion("facebook"));
		menuButton = new AtlasSprite(buttons.findRegion("menu"));
		menuButton_small = new AtlasSprite(
				buttons.findRegion("menubutton_small"));
		retryButton_small = new AtlasSprite(
				buttons.findRegion("retrybutton_small"));
		retryButton_small1 = new AtlasSprite(
				buttons.findRegion("retrybutton_small"));
		nextButton_small = new AtlasSprite(
				buttons.findRegion("nextbutton_small"));

		achievementsButton_big = new AtlasSprite(
				buttons.findRegion("achievements_big"));
		shopButton_big = new AtlasSprite(buttons.findRegion("shop_big"));
		playButton_small = new AtlasSprite(buttons.findRegion("play_small"));

		pauseButton = new AtlasSprite(buttons.findRegion("pause_small"));

		left = new AtlasSprite(buttons.findRegion("left"));
		leftBlank = new AtlasSprite(buttons.findRegion("leftblank"));
		right = new AtlasSprite(buttons.findRegion("right"));
		rightBlank = new AtlasSprite(buttons.findRegion("rightblank"));

		menuWindow = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/levelmenu_window.png")));

		finishedWindow = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/finished_window.png")));

		menuBG1 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/levelmenu_background2.png")));
		menuBG2 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/levelmenu_background3.png")));
		menuBG3 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/levelmenu_background4.png")));

		backgroundRegion = new TextureRegion(menuBG1);

		leftButton = new AtlasSprite(buttons.findRegion("left"));
		leftButton.setScale(0.6f);
		rightButton = new AtlasSprite(buttons.findRegion("right"));
		rightButton.setScale(0.6f);
		backButton = new AtlasSprite(buttons.findRegion("back"));
		worldButton = new AtlasSprite(buttons.findRegion("facebook"));
		worldButton.setScale(0.6f);

		trueClick = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/bought.png")));

		completed = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/levelcompleted.png")));
		levelselect = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/levelselect.png")));
		worldselect = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/worldselect.png")));
		settings = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/options.png")));
		shop = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/shop.png")));

		superHandsTitle = new TextureRegion(new Texture(
				Gdx.files.internal("assets/menuLogo.png")));
		ropeJoint = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ropeJoint.png")));

		for (int i = 1; i <= 4; i++) {
			vine[i - 1] = vines.createSprite("vine" + i);
			vine[i - 1].setSize(vine[i - 1].getWidth()
					* GameConstant.WIDTH_CONVERTER / 1.5f,
					vine[i - 1].getHeight() * GameConstant.HEIGHT_CONVERTER
							/ 1.75f);

		}
		for (int i = 1; i <= 4; i++) {
			vineFlipped[i - 1] = vines.createSprite("vine" + i);
			vineFlipped[i - 1].setSize(vineFlipped[i - 1].getWidth()
					* GameConstant.WIDTH_CONVERTER / 1.5f,
					vineFlipped[i - 1].getHeight()
							* GameConstant.HEIGHT_CONVERTER / 1.9f);

		}
		vineFlipped[1].flip(true, false);

		worlds = new TextureAtlas("assets/world/worlds.pack");
		worldslocked = new TextureAtlas("assets/world/worldslocked.pack");

		forest = worlds.createSprite("BG1");
		water = worlds.createSprite("BG2");
		volcano = worlds.createSprite("BG3");
		waterLock = worldslocked.createSprite("BG2");
		volcanoLock = worldslocked.createSprite("BG3");

		// LEVEL ICONS
		levelPlayed1 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level_played.png")));
		levelPlayed2 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level2_played.png")));
		levelPlayed3 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level3_played.png")));

		levelLocked1 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level_locked.png")));
		levelLocked2 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level2_locked.png")));
		levelLocked3 = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level3_locked.png")));

		lock = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/level_lock.png")));
		worldLock = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/worldLock.png")));
		noLock = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/nolock.png")));

		allCoins = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/allcoins.png")));
		notAllCoins = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/notallcoins.png")));

		black = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/black.png")));

		headingFont = new BitmapFont(
				Gdx.files.internal("assets/fonts/komika.fnt"));

		font = new BitmapFont(Gdx.files.internal("assets/fonts/haha.fnt"),
				Gdx.files.internal("assets/fonts/haha_0.png"), false);

		adsOff = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/adsoff.png")));
		buyWater = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/openwaterland.png")));
		buyVolcano = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/openvolcanopit.png")));
		buySkip = new TextureRegion(new Texture(
				Gdx.files.internal("assets/ui/skiplevel.png")));

		// SOUNDS
		volcanoTheme = Gdx.audio.newMusic(Gdx.files
				.internal("assets/sounds/volcanotheme.ogg"));
		forestTheme = Gdx.audio.newMusic(Gdx.files
				.internal("assets/sounds/foresttheme.ogg"));
		waterTheme = Gdx.audio.newMusic(Gdx.files
				.internal("assets/sounds/watertheme.ogg"));
		caveTheme = Gdx.audio.newMusic(Gdx.files
				.internal("assets/sounds/cavetheme.ogg"));
		completedTheme = Gdx.audio.newMusic(Gdx.files
				.internal("assets/sounds/completed.ogg"));
		mainTheme = Gdx.audio.newMusic(Gdx.files
				.internal("assets/sounds/maintheme.ogg"));

		dead = Gdx.audio.newSound(Gdx.files.internal("assets/sounds/dead.wav"));
		finish = Gdx.audio.newSound(Gdx.files
				.internal("assets/sounds/finish.wav"));
		menuSwoosh = Gdx.audio.newSound(Gdx.files
				.internal("assets/sounds/menuswoosh.wav"));
		ropeSound = Gdx.audio.newSound(Gdx.files
				.internal("assets/sounds/rope.wav"));
		coinSound = Gdx.audio.newSound(Gdx.files
				.internal("assets/sounds/coin.wav"));
		buttonSound = Gdx.audio.newSound(Gdx.files
				.internal("assets/sounds/button.wav"));

	}

	public static void dispose() {

		finishedWindow.getTexture().dispose();
		buttons.dispose();
		headingFont.dispose();

	}

	public static TiledMap getLevel(int level) {

		String path = "assets/lvl/test" + Integer.toString(level) + ".tmx";
		tileMap = new TmxMapLoader().load(path);

		return tileMap;

	}

	public static TextureRegion getCoins(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void playMusic(Music music) {

		if (PreferencesBean.getBooleanPreferences("music", true)) {
			if (!music.isPlaying()) {
				music.play();
				music.setVolume(PreferencesBean.getFloatPreference(
						"musicVolume", 0f));
			}
			if (!music.isLooping())
				music.setLooping(true);
		}

	}

	public static void pauseMusic(Music music) {
		if (music.isPlaying()) {
			music.pause();
			music.stop();
		}
		if (music.isLooping())
			music.setLooping(false);
	}

	public static void playSound(Sound sound) {

		if (PreferencesBean.getBooleanPreferences("sound", true)) {
			sound.play(PreferencesBean.getFloatPreference("sfxVolume", .4f));
		}

	}

	public static void play_Music_Sound(Music music) {

		if (PreferencesBean.getBooleanPreferences("sound", true)) {
			if (!music.isPlaying()) {
				music.play();
			}
			if (!music.isLooping())
				music.setLooping(true);
		}
	}

}
