package fi.kyy.Screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fi.kyy.Game.SuperHands;
import fi.kyy.Helpers.Assets;
import fi.kyy.TweenAccessors.ActorAccessor;
import fi.kyy.Utils.PreferencesBean;

public class SettingsScreen extends ApplicationAdapter implements Screen,
		InputProcessor {

	private Stage stage;
	private Table container;
	private TextureAtlas atlas, atlas2;
	private Skin skin, skin2;

	private Table content;

	private OrthographicCamera cam;

	private float scale = 1280 / Gdx.graphics.getWidth();

	private Image buttonBack, asd;

	private TweenManager tweenManager;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tweenManager.update(delta);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

	}

	@Override
	public void show() {

		tweenManager = new TweenManager();

		Tween.registerAccessor(Actor.class, new ActorAccessor());

		if (scale <= 1) {
			scale = 1;
		}

		Gdx.input.setCatchBackKey(true);

		cam = new OrthographicCamera(1280, 768);
		cam.position.set(1280 / 2, 768 / 2, 0);
		cam.setToOrtho(false, 1280, 768);

		stage = new Stage(new StretchViewport(1280, 768, cam));

		content = new Table();
		content.defaults().space(30);

		Group background = new Group();
		background.setBounds(0, 0, 1280, 768);
		background.setCenterPosition(1280 / 2, 768 / 2);

		background.addActor(new Image(Assets.backgroundRegion));

		TextureRegion comp = Assets.settings;
		asd = new Image(comp);
		asd.setPosition(1280 / 2 - asd.getWidth() / 2 + 20,
				768 - asd.getHeight() - 45);

		atlas = new TextureAtlas("assets/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("assets/uiskin.json"), atlas);

		atlas2 = new TextureAtlas("assets/ui/emt.atlas");
		skin2 = new Skin(Gdx.files.internal("assets/ui/emt.JSON"), atlas2);

		Label heading = new Label(" ", skin);
		heading.setFontScale(1.2f);

		Label sfx = new Label("SFX", skin);

		Label music = new Label("MUSIC", skin);

		stage.addActor(background);

		Gdx.input.setInputProcessor(stage);

		container = new Table();
		stage.addActor(container);
		container.setBackground(new TextureRegionDrawable(Assets.menuWindow));
		container.center();
		container.setBounds(0, 0, 1280, 768);

		buttonBack = new Image(Assets.backButton);
		buttonBack.setX(290);
		buttonBack.setY(46);
		buttonBack.setScale(1);
		buttonBack.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				buttonBack.setScale(0.96f);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				((SuperHands) Gdx.app.getApplicationListener())
						.setScreen(new MainMenuScreen());
			}

		});

		final Slider sfxVolume = new Slider(0f, 1, 0.05f, false, skin2);
		final Slider musicVolume = new Slider(0f, 1, 0.05f, false, skin2);
		final Label sfxValue;
		final Label musicValue;

		if (!PreferencesBean.getBooleanPreferences("sfxMoved", false)) {
			PreferencesBean.setFloatPreference("sfxVolume", 0.4f);
			sfxVolume.setValue(0.4f);
			sfxValue = new Label("8", skin);
		} else {
			sfxVolume.setValue(PreferencesBean.getFloatPreference("sfxVolume",
					0.4f));
			sfxValue = new Label(Integer.toString((int) (PreferencesBean
					.getFloatPreference("sfxVolume", 0.4f) * 20)), skin);
		}

		if (!PreferencesBean.getBooleanPreferences("musicMoved", false)) {
			PreferencesBean.setFloatPreference("musicVolume", 0f);
			musicVolume.setValue(0f);
			musicValue = new Label("0", skin);
		} else {
			musicVolume.setValue(PreferencesBean.getFloatPreference(
					"musicVolume", 0.3f));
			musicValue = new Label(Integer.toString((int) (PreferencesBean
					.getFloatPreference("musicVolume", 0f) * 20)), skin);
		}

		sfxVolume.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				PreferencesBean.setFloatPreference("sfxVolume",
						sfxVolume.getValue());
				// sound.setVolume(soundId, sfxVolume.getValue());
				sfxValue.setText("" + (int) (sfxVolume.getValue() * 20));
				PreferencesBean.setBooleanPreference("sfxMoved", true);
			}
		});

		musicVolume.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				PreferencesBean.setFloatPreference("musicVolume",
						musicVolume.getValue());
				// sound.setVolume(soundId, musicVolume.getValue());
				musicValue.setText("" + (int) (musicVolume.getValue() * 20));
				PreferencesBean.setBooleanPreference("musicMoved", true);
			}
		});

		stage.addActor(asd);
		stage.addActor(buttonBack);

		content.add(sfx);
		content.add(sfxVolume).width(400);
		content.add(sfxValue);
		content.row();
		content.add(music);
		content.add(musicVolume).width(400);
		content.add(musicValue);

		heading.setColor(Color.ORANGE);
		container.add(heading).padTop(10 * scale).padBottom(20 * scale)
				.center().padLeft(40 * scale);
		container.row();
		content.row().expand().fill();

		container.add(content).expand().fill();

		Timeline.createSequence().beginSequence().beginParallel()
				.push(Tween.set(heading, ActorAccessor.ALPHA).target(1))
				.push(Tween.set(content, ActorAccessor.ALPHA).target(0))
				.push(Tween.to(content, ActorAccessor.ALPHA, .5f).target(1))
				.end().start(tweenManager);

	}

	@Override
	public boolean keyDown(int keycode) {
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			((SuperHands) Gdx.app.getApplicationListener())
					.setScreen(new MainMenuScreen());
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
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

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
