package fi.kyy.Screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fi.kyy.Game.SuperHands;
import fi.kyy.Helpers.Assets;
import fi.kyy.TweenAccessors.ActorAccessor;
import fi.kyy.Utils.PreferencesBean;

public class WorldMenuScreen extends ApplicationAdapter implements Screen,
		InputProcessor {

	private Stage stage;
	private Table container;
	private TextureAtlas atlas;
	private Skin skin;

	private Table content;

	private OrthographicCamera cam;

	public static int world = 1;

	private static int page = 1;

	private float scale = 1280 / Gdx.graphics.getWidth();

	private Image planet1, planet2, planet3, buttonBack, buttonShop,
			buttonLeft, buttonRight, asd, worldLock;

	private boolean helpBoolean = false;

	private int totalCompleted = 0;

	private int allCoins = 0;

	private TweenManager tweenManager;

	BitmapFont font = Assets.font;
	LabelStyle styleF = new LabelStyle(font, Color.WHITE);

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

		Assets.playMusic(Assets.mainTheme);

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

		TextureRegion comp = Assets.worldselect;
		asd = new Image(comp);
		asd.setPosition(1280 / 2 - asd.getWidth() / 2 + 20,
				768 - asd.getHeight() - 45);

		atlas = new TextureAtlas("assets/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("assets/uiskin.json"), atlas);

		Label heading = new Label(" ", skin);
		heading.setFontScale(1.2f);

		stage.addActor(background);

		if (page == 1) {
			background.addActor(new Image(Assets.menuBG1));
		} else if (page == 2) {
			background.addActor(new Image(Assets.menuBG2));
		} else if (page == 3) {
			background.addActor(new Image(Assets.menuBG3));
		}

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
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				((SuperHands) Gdx.app.getApplicationListener())
						.setScreen(new MainMenuScreen());
			}

		});

		buttonShop = new Image(Assets.shopButton);
		buttonShop.setX(1280 - 376);
		buttonShop.setY(46);
		buttonShop.setScale(1);
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
						.setScreen(new MainMenuScreen());
			}

		});

		if (page >= 3) {
			buttonLeft = new Image(Assets.leftBlank);
			buttonLeft.setX(1280 - 256);
			buttonLeft.setY(350);
		} else {
			buttonLeft = new Image(Assets.left);
			buttonLeft.setX(1280 - 256);
			buttonLeft.setY(350);
		}

		buttonLeft.setScale(1f);
		buttonLeft.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (page >= 3) {
					page = 3;
				} else {
					buttonLeft.setScale(0.96f);
					Assets.playSound(Assets.buttonSound);
					page++;

				}
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (page <= 3) {
					((SuperHands) Gdx.app.getApplicationListener())
							.setScreen(new WorldMenuScreen());
				}
			}
		});

		if (page <= 1) {
			buttonRight = new Image(Assets.rightBlank);
			buttonRight.setX(195);
			buttonRight.setY(350);
		} else {
			buttonRight = new Image(Assets.right);
			buttonRight.setX(195);
			buttonRight.setY(350);
		}

		buttonRight.setScale(1f);
		buttonRight.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				if (page <= 1) {
					page = 1;
				} else {
					buttonRight.setScale(0.96f);
					Assets.playSound(Assets.buttonSound);
					page--;

				}
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (page >= 1) {
					((SuperHands) Gdx.app.getApplicationListener())
							.setScreen(new WorldMenuScreen());
				}
			}
		});

		int c = 1;

		for (int l = 0; l < 1; l++) {
			Table levels = new Table().padTop(0).padBottom(130 * scale)
					.padLeft(260 * scale).padRight(244 * scale);
			levels.defaults().expand().fill();
			for (int y = 0; y < 1; y++) {
				levels.row().padBottom(20 * scale);
				for (int x = 0; x < 1; x++) {
					// levels.debug();
					levels.add(getLevelButton(c++)).expand().fill();
					levels.add(getInfo());
				}
			}
			addPage(levels);
		}

		stage.addActor(asd);
		stage.addActor(buttonBack);
		stage.addActor(buttonShop);
		stage.addActor(buttonLeft);
		stage.addActor(buttonRight);
		// container.debug();
		heading.setColor(Color.ORANGE);
		container.add(heading).padTop(100 * scale).padBottom(20 * scale)
				.center().padLeft(40 * scale);
		container.row();
		content.row().expand().fill().padBottom(80 * scale);

		container.add(content).expand().fill();

		Timeline.createSequence().beginSequence().beginParallel()
				.push(Tween.set(heading, ActorAccessor.ALPHA).target(1))
				.push(Tween.set(content, ActorAccessor.ALPHA).target(0))
				.push(Tween.to(content, ActorAccessor.ALPHA, .5f).target(1))
				.end().start(tweenManager);

	}

	public Table getInfo() {
		Table info = new Table(skin);

		if (page == 1) {
			totalCompleted = 0;
			allCoins = 0;
			info.add("LEVELS PLAYED:").padTop(-20 * scale).padRight(50 * scale);
			info.row();
			for (int i = 1; i < 30; i++) {
				allCoins += PreferencesBean.getIntegerPreference("map" + i
						+ "totalcoins", 0);

				if (PreferencesBean.getBooleanPreferences(Integer.toString(i)
						+ "completed", false) == true) {
					totalCompleted++;
				}
			}

			info.add(Integer.toString(totalCompleted) + " / 30");
			info.row();
			info.add("COINS COLLECTED:").padTop(20 * scale)
					.padRight(20 * scale);

			info.row();
			info.add(Integer.toString(allCoins) + " / 100");
		}

		else if (page == 2) {
			totalCompleted = 0;
			allCoins = 0;
			if (PreferencesBean.getBooleanPreferences("waterUnlocked", false) == true) {
			info.add("LEVELS PLAYED:").padTop(-20 * scale).padRight(50 * scale);
			info.row();
			for (int i = 31; i < 60; i++) {
				allCoins += PreferencesBean.getIntegerPreference("map" + i
						+ "totalcoins", 0);

				if (PreferencesBean.getBooleanPreferences(Integer.toString(i)
						+ "completed", false) == true) {
					totalCompleted++;
				}
			}

			info.add(Integer.toString(totalCompleted) + " / 30");
			info.row();
			info.add("COINS COLLECTED:").padTop(20 * scale)
					.padRight(20 * scale);

			info.row();
			info.add(Integer.toString(allCoins) + " / 100");
			} else {
				info.add("GET 90 COINS").padTop(-20 * scale).padRight(50 * scale);
				info.row();
				info.add("OR BUY");
				info.row();
				info.add("TO UNLOCK");
			}
		}

		else if (page == 3) {
			totalCompleted = 0;
			allCoins = 0;
			if (PreferencesBean.getBooleanPreferences("volcanoUnlocked", false) == true) {
			info.add("LEVELS PLAYED:").padTop(-20 * scale).padRight(50 * scale);
			info.row();
			for (int i = 61; i < 90; i++) {
				allCoins += PreferencesBean.getIntegerPreference("map" + i
						+ "totalcoins", 0);

				if (PreferencesBean.getBooleanPreferences(Integer.toString(i)
						+ "completed", false) == true) {
					totalCompleted++;
				}
			}

			info.add(Integer.toString(totalCompleted) + " / 30");
			info.row();
			info.add("COINS COLLECTED:").padTop(20 * scale)
					.padRight(20 * scale);

			info.row();
			info.add(Integer.toString(allCoins) + " / 100");
			} else {
				info.add("GET 180 COINS").padTop(-20 * scale).padRight(50 * scale);
				info.row();
				info.add("OR BUY");
				info.row();
				info.add("TO UNLOCK");
			}
		}

		return info;
	}

	public Button getLevelButton(int level) {

		Button button = new Button(skin);
		button.setScale(scale);
		ButtonStyle style = button.getStyle();
		style.up = style.down = null;

		Label label = new Label(Integer.toString(level), styleF);
		label.setFontScale(1);
		label.setAlignment(Align.center);

		planet1 = new Image(Assets.forest);

		if (PreferencesBean.getBooleanPreferences("waterUnlocked", helpBoolean) == true) {
			planet2 = new Image(Assets.water);
		} else {
			planet2 = new Image(Assets.waterLock);
		}
		if (PreferencesBean.getBooleanPreferences("volcanoUnlocked",
				helpBoolean) == true) {
			planet3 = new Image(Assets.volcano);
		} else {
			planet3 = new Image(Assets.volcanoLock);
		}

		switch (page) {
		case 1:
			button.add(planet1).padRight(30 * scale).padTop(10 * scale)
					.padLeft(30 * scale);
			button.row().padBottom(10 * scale);
			button.add("THE FOREST");
			break;
		case 2:
			button.add(planet2).padRight(30 * scale).padTop(10 * scale)
					.padLeft(30 * scale);
			button.row().padBottom(10);
			button.add("WATERLAND");
			break;
		case 3:
			button.add(planet3).padRight(30 * scale).padTop(10 * scale)
					.padLeft(30 * scale);
			button.row().padBottom(10);
			button.add("VOLCANO PIT");
			break;
		default:
			break;
		}
		// } else {
		// button.stack(lockedKehys, lock, label);
		// button.setScale(scale);
		// }

		button.row();

		button.setName("Level" + Integer.toString(level));
		button.addListener(levelClickListener);
		return button;
	}

	public InputListener levelClickListener = new InputListener() {
		@Override
		public boolean touchDown(InputEvent event, float x, float y,
				int pointer, int button) {
			System.out.println("Click: " + event.getListenerActor().getName());
			System.out.println("" + x + ", " + y);
			if (page == 1) {
				planet1.setScale(0.96f);
				Assets.playSound(Assets.buttonSound);
				world = 1;
			} else if (page == 2) {
				if (PreferencesBean.getBooleanPreferences("waterUnlocked",
						helpBoolean) == true) {
					planet2.setScale(0.96f);
					Assets.playSound(Assets.buttonSound);
					world = 2;
				}

			} else if (page == 3) {
				if (PreferencesBean.getBooleanPreferences("volcanoUnlocked",
						helpBoolean) == true) {
					planet3.setScale(0.96f);
					Assets.playSound(Assets.buttonSound);
					world = 3;
				}
			}
			return true;
		}

		public void touchUp(InputEvent event, float x, float y, int pointer,
				int button) {

			if (page == 1) {
				((SuperHands) Gdx.app.getApplicationListener())
						.setScreen(new LevelSelectScreen());
			} else if (page == 2) {
				if (PreferencesBean.getBooleanPreferences("waterUnlocked",
						helpBoolean) == true) {
					((SuperHands) Gdx.app.getApplicationListener())
							.setScreen(new LevelSelectScreen());
				}

			} else if (page == 3) {
				if (PreferencesBean.getBooleanPreferences("volcanoUnlocked",
						helpBoolean) == true) {
					((SuperHands) Gdx.app.getApplicationListener())
							.setScreen(new LevelSelectScreen());
				}
			}

		}
	};

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public void addPages(Actor... pages) {
		for (Actor page : pages) {
			content.add(page).expand().fill();

		}

	}

	public void addPage(Actor page) {
		content.add(page).expand().fill();
	}

	@Override
	public void dispose() {
		stage.dispose();
		atlas.dispose();
		skin.dispose();
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

}
