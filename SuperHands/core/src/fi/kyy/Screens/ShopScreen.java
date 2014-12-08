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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fi.kyy.Game.SuperHands;
import fi.kyy.Helpers.Assets;
import fi.kyy.Services.PurchaseCallback;
import fi.kyy.TweenAccessors.ActorAccessor;
import fi.kyy.Utils.PreferencesBean;

public class ShopScreen extends ApplicationAdapter implements Screen,
		InputProcessor, PurchaseCallback {

	private Stage stage;
	private Table container;
	private TextureAtlas atlas, atlas2;
	private Skin skin, skin2;

	private Table content;

	private OrthographicCamera cam;

	private float scale = 1280 / Gdx.graphics.getWidth();

	private Image buttonBack, asd;

	private TweenManager tweenManager;

	private int selection;

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

		TextureRegion comp = Assets.shop;
		asd = new Image(comp);
		asd.setPosition(1280 / 2 - asd.getWidth() / 2 + 20,
				768 - asd.getHeight() - 45);

		atlas = new TextureAtlas("assets/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("assets/uiskin.json"), atlas);

		Label heading = new Label(" ", skin);
		heading.setFontScale(1.2f);

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

		int c = 1;
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

		heading.setColor(Color.ORANGE);
		container.add(heading).padTop(100 * scale).padBottom(20 * scale)
				.center().padLeft(30 * scale);
		container.row();
		content.row().expand().fill().padBottom(80 * scale);

		container.add(content).expand().fill();

		Timeline.createSequence().beginSequence().beginParallel()
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

		Label price = new Label("", skin);
		price.setFontScale(0.8f);
		price.setAlignment(Align.bottom);

		switch (level) {
		case 1:
			price.setText("0,99");
			if (PreferencesBean.getBooleanPreferences("adsOff", false) == true) {
				button.stack(new Image(Assets.adsOff), new Image(
						Assets.trueClick));
			} else {
				button.stack(new Image(Assets.adsOff), price);
			}

			break;
		case 2:
			price.setText("1,99e");
			button.stack(new Image(Assets.buyWater), price);
			break;
		case 3:
			price.setText("1,99e");
			button.stack(new Image(Assets.buyVolcano), price);
			break;
		case 4:
			price.setText("2,99e");
			button.stack(new Image(Assets.buyVolcano), price);
			break;
		case 5:
			price.setText("2,99");
			button.stack(new Image(Assets.buyVolcano), price);
			break;
		case 6:
			price.setText("2,99");
			button.stack(new Image(Assets.buyVolcano), price);
			break;

		}

		button.setScale(scale);

		button.row();

		button.setName(Integer.toString(level));
		button.addListener(buttonClickListener);
		return button;

	}

	public ClickListener buttonClickListener = new ClickListener() {
		@Override
		public void clicked(InputEvent event, float x, float y) {

			selection = Integer.parseInt(event.getListenerActor().getName());

			switch (selection) {
			case 1:
				SuperHands.actionResolver.requestIabPurchase(1);
				break;
			case 2:
				SuperHands.actionResolver.requestIabPurchase(2);
				break;
			case 3:
				SuperHands.actionResolver.requestIabPurchase(3);
				break;
			case 4:
				SuperHands.actionResolver.requestIabPurchase(4);
				break;
			case 5:
				break;
			case 6:
				break;
			}

		}

	};

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

	// PALAUTTAA TULOKSEN OSTOSTA

	// Esim. result = ActionResolver.requestIabPurchase(1, this);
	@Override
	public int setPurchaseResult(int result) {
		// TODO Auto-generated method stub
		return 0;
	}
}
