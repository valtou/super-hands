package fi.kyy.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fi.kyy.Helpers.Assets;
import fi.kyy.Game.SuperHands;
import fi.kyy.Services.ActionResolver;
import fi.kyy.Services.PurchaseCallback;
import fi.kyy.TweenAccessors.ActorAccessor;
import fi.kyy.Utils.Box2dUtils;
import fi.kyy.Utils.PreferencesBean;
import fi.kyy.Objects.Chain;

public class MainMenuScreen extends ScreenAdapter implements Screen,
		InputProcessor {

	World world;
	Chain logoChain1, logoChain2;
	Body logoBody;
	Body menuBody1Top, menuBody2Top;

	private Stage stage;
	private Table table;
	private TweenManager manager;

	private OrthographicCamera cam, guiCam;

	private Label heading;

	private Image buttonShop, buttonPlay, buttonSettings, buttonFacebook,
			buttonInfo, buttonAchievements;

	private Group background, black;

	TweenCallback cb;
	private float scale = 1280 / Gdx.graphics.getWidth();

	SpriteBatch batch;

	Sprite ropeSwingBase = new Sprite();
	Sprite ropeJointLeft = new Sprite();
	Sprite ropeJointRight = new Sprite();

	Vector2 force = new Vector2(10f, 0f);

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		manager.update(delta);

		stage.act(delta);
		stage.draw();

		batch.enableBlending();
		batch.begin();

		renderWorldObjects();

		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderSprites(batch);

		world.step(1 / 20f, 8, 3);

		batch.end();

	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {

		//Gdx.input.setCatchBackKey(true);

		if (PreferencesBean.getBooleanPreferences("waterUnlocked", true) == false) {
			PreferencesBean.setBooleanPreference("waterUnlocked",
					SuperHands.actionResolver.getWaterUnlocked());
		}
		if (PreferencesBean.getBooleanPreferences("volcanoUnlocked", true) == false) {
			PreferencesBean.setBooleanPreference("volcanoUnlocked",
					SuperHands.actionResolver.getVolcanoUnlocked());
		}

		Assets.playMusic(Assets.mainTheme);

		if (scale <= 1) {
			scale = 1;
		}

		batch = new SpriteBatch();

		cam = new OrthographicCamera(1280, 768);
		cam.position.set(1280 / 2, 768 / 2, 0);

		stage = new Stage(new StretchViewport(1280, 768, cam));
		stage.getWidth();

		Gdx.input.setInputProcessor(stage);

		background = new Group();
		background.setBounds(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		background.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);

		stage.addActor(background);
		TextureRegion menubg = new TextureRegion(Assets.backgroundRegion);

		background.addActor(new Image(menubg));

		table = new Table();
		table.setBounds(0, 0, 1280, 768);

		buttonPlay = new Image(Assets.playButton);
		buttonPlay.setX(1280 / 2 - buttonPlay.getWidth() / 2);
		buttonPlay.setY(150);
		buttonPlay.setScale(1);
		buttonPlay.addListener(new ClickListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonPlay.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				WorldMenuScreen.world = 1;
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				stage.addActor(black);

				Timeline.createSequence()
						.beginSequence()
						.beginParallel()
						.push(Tween.set(black, ActorAccessor.ALPHA).target(1))
						.push(Tween.to(black, ActorAccessor.ALPHA, .0f).target(
								0)).end()
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.start(manager);

				world.setGravity(new Vector2(0, -45f));
				world.destroyBody(menuBody1Top);
				world.destroyBody(menuBody2Top);
				// world.destroyBody(logoBody);

				cb = new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						((SuperHands) Gdx.app.getApplicationListener())
								.setScreen(new WorldMenuScreen());
					}
				};

				Timeline.createSequence()
						.beginSequence()
						.beginSequence()
						.push(Tween.set(table, ActorAccessor.ALPHA).target(1))
						.push(Tween.to(table, ActorAccessor.ALPHA, 0.8f)
								.target(0))
						.push(Tween.set(black, ActorAccessor.ALPHA).target(0))
						.push(Tween.to(black, ActorAccessor.ALPHA, .3f).target(
								1)).end().setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.start(manager);

			}

		});

		buttonSettings = new Image(Assets.optionsButton);
		buttonSettings.setX(20);
		buttonSettings.setY(18);
		buttonSettings.setScale(1);
		buttonSettings.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonSettings.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				((SuperHands) Gdx.app.getApplicationListener())
						.setScreen(new SettingsScreen());
			}

		});

		buttonFacebook = new Image(Assets.facebookButton);
		buttonFacebook.setX(1280 - buttonFacebook.getWidth() - 20);
		buttonFacebook.setY(18);
		buttonFacebook.setScale(1f);
		buttonFacebook.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonFacebook.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				SuperHands.actionResolver.openFacebook();
			}

		});

		buttonShop = new Image(Assets.shopButton_big);
		buttonShop.setX(1280 / 2 - buttonShop.getWidth() - 140);
		buttonShop.setY(100);
		buttonShop.setScale(1f);
		buttonShop.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonShop.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				((SuperHands) Gdx.app.getApplicationListener())
						.setScreen(new ShopScreen());
			}

		});

		buttonInfo = new Image(Assets.tutorialButton);
		buttonInfo.setX(20);
		buttonInfo.setY(768 - buttonInfo.getHeight() - 18);
		buttonInfo.setScale(1f);
		buttonInfo.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonInfo.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				// TODO
				// ((SuperHands) Gdx.app.getApplicationListener()).setScreen(new
				// TutorialScreen());

			}

		});

		buttonAchievements = new Image(Assets.achievementsButton_big);
		buttonAchievements.setX(1280 / 2 + 140);
		buttonAchievements.setY(100);
		buttonAchievements.setScale(1f);
		buttonAchievements.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonAchievements.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				SuperHands.actionResolver.getLeaderboardGPGS();
			}

		});

		table.addActor(buttonSettings);
		table.addActor(buttonPlay);
		table.addActor(buttonInfo);
		table.addActor(buttonFacebook);
		table.addActor(buttonShop);
		table.addActor(buttonAchievements);
		stage.addActor(table);

		manager = new TweenManager();
		Tween.registerAccessor(Actor.class, new ActorAccessor());

		initWorld();
		initSprites();

		black = new Group();
		black.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		black.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		black.addActor(new Image(Assets.black));

	}

	public void initWorld() {

		guiCam = new OrthographicCamera(150, 90);
		guiCam.position.set(150 / 2, 90 / 2, 0);
		world = new World(new Vector2(0f, -10f), false);
		logoChain1 = new Chain(world);
		logoChain2 = new Chain(world);

		menuBody1Top = Box2dUtils.createCircle(world, BodyType.StaticBody, .1f,
				1000);
		menuBody2Top = Box2dUtils.createCircle(world, BodyType.StaticBody, .1f,
				1000);
		logoBody = Box2dUtils.createBox(world, BodyType.DynamicBody, 60f, 1f,
				.001f, (short) 0, (short) 0);

		menuBody1Top.setTransform(50f, 90f, 0);
		menuBody2Top.setTransform(100f, 90f, 0);
		logoBody.setTransform(74f, 74f, 0);

		logoChain1.createChain(menuBody1Top, logoBody,
				menuBody1Top.getLocalCenter(), new Vector2(-25, -4));
		logoChain2.createChain(menuBody2Top, logoBody,
				menuBody2Top.getLocalCenter(), new Vector2(24, -13));

	}

	private void initSprites() {

		ropeSwingBase.setRegion(Assets.superHandsTitle);
		ropeJointLeft.setRegion(Assets.ropeJoint);
		ropeJointRight.setRegion(Assets.ropeJoint);

		ropeJointLeft.setBounds(0, 0, ropeJointLeft.getRegionWidth(),
				ropeJointLeft.getRegionHeight());
		ropeJointRight.setBounds(0, 0, ropeJointLeft.getRegionWidth(),
				ropeJointLeft.getRegionHeight());
		ropeSwingBase.setBounds(0, 0, ropeSwingBase.getRegionWidth(),
				ropeSwingBase.getRegionHeight());

	}

	public void renderWorldObjects() {

		guiCam.update();
		batch.setProjectionMatrix(guiCam.combined);
		logoChain1.render(batch);
		logoChain2.render(batch);

	}

	private void renderSprites(SpriteBatch batch) {

		ropeSwingBase.setPosition(128 * logoBody.getPosition().x / 15
				- ropeSwingBase.getWidth() / 2, 76.8f
				* logoBody.getPosition().y / 9 - ropeSwingBase.getHeight()
				/ 1.1f);

		ropeJointLeft.setPosition(
				1280 * (logoBody.getPosition().x - 25) / 150
						- ropeJointRight.getWidth() / 2,
				2 * 768 * logoBody.getPosition().y / 90
						- ropeJointLeft.getHeight());
		ropeJointRight.setPosition(1280 * (logoBody.getPosition().x + 24) / 150
				- ropeJointRight.getWidth() / 2, 768 * logoBody.getPosition().y
				/ 90 - ropeJointRight.getHeight());
		ropeSwingBase.draw(batch);
		ropeJointLeft.draw(batch);
		ropeJointRight.draw(batch);

	}

	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}

	@Override
	public void dispose() {
		super.dispose();
		stage.dispose();

	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Keys.BACK:
			Gdx.app.exit();
			break;
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
