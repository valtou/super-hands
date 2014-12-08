package fi.kyy.Screens;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fi.kyy.Game.SuperHands;
import fi.kyy.Helpers.Assets;
import fi.kyy.TweenAccessors.ActorAccessor;
import fi.kyy.Utils.PreferencesBean;

public class LevelSelectScreen extends ApplicationAdapter implements Screen,
		InputProcessor {

	private Stage stage;
	private Table container;
	private TextureAtlas atlas;
	private Skin skin;

	Image asd;

	private float scale = 1280 / Gdx.graphics.getWidth();

	private Table content;

	private OrthographicCamera cam;

	private static int page = 0;

	private TweenManager tweenManager;

	private Image buttonBack, buttonShop, buttonLeft, buttonRight;

	private Label heading;

	private int selection;

	private Group black;

	private Image cont, lockCont;

	private boolean played, completed;

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

		TextureRegion comp = Assets.levelselect;
		asd = new Image(comp);
		asd.setPosition(1280 / 2 - asd.getWidth() / 2 + 20,
				768 - asd.getHeight() - 45);

		black = new Group();
		black.setBounds(0, 0, 1280, 768);
		black.setCenterPosition(1280 / 2, 768 / 2);

		atlas = new TextureAtlas("assets/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("assets/uiskin.json"), atlas);

		heading = new Label(" ", skin);
		heading.setFontScale(1.2f);

		skin.add("top", skin.newDrawable("default-rect", Color.RED),
				Drawable.class);
		skin.add("star-filled", skin.newDrawable("white", Color.YELLOW),
				Drawable.class);
		skin.add("star-unfilled", skin.newDrawable("white", Color.GRAY),
				Drawable.class);

		stage.addActor(background);

		if (WorldMenuScreen.world == 1) {
			background.addActor(new Image(Assets.menuBG1));
		} else if (WorldMenuScreen.world == 2) {
			background.addActor(new Image(Assets.menuBG2));
		} else if (WorldMenuScreen.world == 3) {
			background.addActor(new Image(Assets.menuBG3));
		}

		stage.addActor(black);
		black.addActor(new Image(Assets.black));

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
						.setScreen(new WorldMenuScreen());
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

		if (page >= 4) {
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

				if (page >= 4) {
					page = 4;
				} else {
					buttonLeft.setScale(0.96f);
					Assets.playSound(Assets.buttonSound);
					page++;

				}
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (page <= 4) {
					((SuperHands) Gdx.app.getApplicationListener())
							.setScreen(new LevelSelectScreen());
				}
			}
		});

		if (page <= 0) {
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

				if (page <= 0) {
					page = 0;
				} else {
					buttonRight.setScale(0.96f);
					Assets.playSound(Assets.buttonSound);
					page--;

				}
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (page >= 0) {
					((SuperHands) Gdx.app.getApplicationListener())
							.setScreen(new LevelSelectScreen());
				}
			}
		});

		int c = 1;
		if (page == 0) {
			c = 1;
		} else if (page == 1) {
			c = 7;
		} else if (page == 2) {
			c = 13;
		} else if (page == 3) {
			c = 19;
		} else if (page == 4) {
			c = 25;
		}

		for (int l = 0; l < 1; l++) {
			Table levels = new Table().padTop(0).padBottom(130 * scale)
					.padLeft(260 * scale).padRight(244 * scale);
			levels.defaults().expand().fill();
			for (int y = 0; y < 2; y++) {
				levels.row().padBottom(40 * scale);
				for (int x = 0; x < 3; x++) {
					// levels.debug();
					levels.add(getLevelButton(c++)).expand().fill();
				}
			}
			addPage(levels);
		}

		stage.addActor(asd);
		stage.addActor(buttonBack);
		stage.addActor(buttonShop);
		stage.addActor(buttonLeft);
		stage.addActor(buttonRight);

		heading.setColor(Color.ORANGE);
		container.add(heading).padTop(100 * scale).padBottom(20 * scale)
				.center().padLeft(30 * scale);
		container.row();
		content.row().expand().fill().padBottom(80 * scale);

		container.add(content).expand().fill();

		Timeline.createSequence().beginSequence().beginParallel()
				.push(Tween.set(black, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(heading, ActorAccessor.ALPHA).target(1))
				.push(Tween.set(content, ActorAccessor.ALPHA).target(0))
				.push(Tween.to(content, ActorAccessor.ALPHA, .5f).target(1))
				.end().start(tweenManager);

	}

	public Button getLevelButton(int level) {
		Button button = new Button(skin);
		button.setScale(scale);
		ButtonStyle style = button.getStyle();
		style.up = style.down = null;

		Label label = new Label(Integer.toString(level), styleF);
		label.setFontScale(1);
		label.setAlignment(Align.center);

		int levelCoins = PreferencesBean.getIntegerPreference(
				"map" + Integer.toString(level) + "alllevelcoins", 0);
		int coinsCollected = PreferencesBean.getIntegerPreference("map"
				+ Integer.toString(level) + "totalcoins", 0);

		Label coins = new Label(Integer.toString(coinsCollected) + "/"
				+ Integer.toString(levelCoins), skin);
		coins.setFontScale(0.8f);
		coins.setAlignment(Align.bottom);

		Image allCoins = new Image(Assets.allCoins);
		Image notAllCoins = new Image(Assets.notAllCoins);

		if (WorldMenuScreen.world == 1) {
			cont = new Image(Assets.levelPlayed1);
			lockCont = new Image(Assets.levelLocked1);
			played = PreferencesBean.getBooleanPreferences(
					Integer.toString(level) + "played", false);
			completed = PreferencesBean.getBooleanPreferences(
					Integer.toString(level - 1) + "completed", false);
		} else if (WorldMenuScreen.world == 2) {
			cont = new Image(Assets.levelPlayed2);
			lockCont = new Image(Assets.levelLocked2);
			played = PreferencesBean.getBooleanPreferences(
					Integer.toString(level + 30) + "played", false);
			completed = PreferencesBean.getBooleanPreferences(
					Integer.toString(level - 1 + 30) + "completed", false);
		} else if (WorldMenuScreen.world == 3) {
			cont = new Image(Assets.levelPlayed3);
			lockCont = new Image(Assets.levelLocked3);
			played = PreferencesBean.getBooleanPreferences(
					Integer.toString(level + 60) + "played", false);
			completed = PreferencesBean.getBooleanPreferences(
					Integer.toString(level - 1 + 60) + "completed", false);
		}

		if (page == 0) {
			switch (level) {
			case 1:
				if (played) {
					if (levelCoins == coinsCollected) {
						button.stack(cont, label, coins, allCoins);
					} else {
						button.stack(cont, label, coins, notAllCoins);
					}
				} else {
					button.stack(cont, label);
				}
				break;
			case 2:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 3:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 4:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 5:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 6:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;

			}

		} else {
			switch (level - page * 6) {
			case 1:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 2:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 3:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 4:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 5:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;
			case 6:
				if (!completed) {
					button.stack(lockCont, label, new Image(Assets.lock));
				}
				if (completed) {
					if (played) {
						if (levelCoins == coinsCollected) {
							button.stack(cont, label, coins, allCoins);
						} else {
							button.stack(cont, label, coins, notAllCoins);
						}
						;
					}
					if (!played) {
						button.stack(cont, label);
					}
				}
				break;

			}

		}

		button.setScale(scale);

		button.row();

		button.setName(Integer.toString(level));
		button.addListener(levelClickListener);
		return button;

	}

	public ClickListener levelClickListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {

			if (WorldMenuScreen.world == 1) {
				selection = Integer
						.parseInt(event.getListenerActor().getName());
			}
			if (WorldMenuScreen.world == 2) {
				selection = Integer
						.parseInt(event.getListenerActor().getName()) + 30;
			}
			if (WorldMenuScreen.world == 3) {
				selection = Integer
						.parseInt(event.getListenerActor().getName()) + 60;
			}

			TweenCallback cb = new TweenCallback() {
				@Override
				public void onEvent(int type, BaseTween<?> source) {

					if (selection == 1 || selection == 31 || selection == 61) {
						GameScreen.map = selection;
						((SuperHands) Gdx.app.getApplicationListener())
								.setScreen(new GameScreen());
					}

					else if (selection > 1) {
						if (PreferencesBean.getBooleanPreferences(
								Integer.toString(selection - 1) + "completed",
								false) == true) {

							GameScreen.map = selection;

							// System.out.println("Click: " +
							// helper.getListenerActor().getName());
							// System.out.println("" + x + ", " + y);
							((SuperHands) Gdx.app.getApplicationListener())
									.setScreen(new GameScreen());

						}
					}
				}
			};
			if (PreferencesBean.getBooleanPreferences(
					Integer.toString(selection - 1) + "completed", false) == true
					|| selection == 1 || selection == 31 || selection == 61) {
				Assets.playSound(Assets.buttonSound);
				Assets.playSound(Assets.menuSwoosh);
				Assets.pauseMusic(Assets.mainTheme);
				Timeline.createSequence()
						.beginSequence()
						.beginParallel()
						.push(Tween.to(heading, 3, 0.6f)
								.target(1280 / 2, 768 + heading.getOriginY())
								.ease(Elastic.IN))
						.push(Tween.to(buttonRight, 3, 0.6f)
								.target(195, 768 + 350).ease(Elastic.IN))
						.push(Tween.to(buttonLeft, 3, 0.6f)
								.target(1280 - 256, 768 + 350).ease(Elastic.IN))
						.push(Tween.to(buttonShop, 3, 0.6f)
								.target(1280 - 376, 768 + 46).ease(Elastic.IN))
						.push(Tween.to(buttonBack, 3, 0.6f)
								.target(290, 768 + 46).ease(Elastic.IN))
						.push(Tween.to(container, 3, 0.6f).target(0, 768)
								.ease(Elastic.IN))
						.push(Tween
								.to(asd, 3, 0.6f)
								.target(1280 / 2 - asd.getWidth() / 2 + 20,
										768 - asd.getHeight() - 45 + 768)
								.ease(Elastic.IN))
						.push(Tween.set(container, ActorAccessor.ALPHA).target(
								1))
						.push(Tween.set(heading, ActorAccessor.ALPHA).target(1))
						.push(Tween.set(buttonRight, ActorAccessor.ALPHA)
								.target(1))
						.push(Tween.set(buttonLeft, ActorAccessor.ALPHA)
								.target(1))
						.push(Tween.set(buttonShop, ActorAccessor.ALPHA)
								.target(1))
						.push(Tween.set(buttonBack, ActorAccessor.ALPHA)
								.target(1))
						.push(Tween.set(asd, ActorAccessor.ALPHA).target(1))
						.push(Tween.to(container, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(heading, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(buttonRight, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(buttonLeft, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(buttonRight, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(buttonShop, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(buttonBack, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(asd, ActorAccessor.ALPHA, .7f).target(0))
						.end()
						.push(Tween.to(black, ActorAccessor.ALPHA, .5f).target(
								1)).end().setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.start(tweenManager);
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
			// content.debug();
			content.add(page).expand().fill();
		}

	}

	public void addPage(Actor page) {
		// content.debug();
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
					.setScreen(new WorldMenuScreen());
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
