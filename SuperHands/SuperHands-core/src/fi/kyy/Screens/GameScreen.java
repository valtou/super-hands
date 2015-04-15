package fi.kyy.Screens;

import static fi.kyy.Handlers.B2DVars.PPM;

import java.text.DecimalFormat;
import java.util.ArrayList;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Elastic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import fi.kyy.Game.SuperHands;
import fi.kyy.Helpers.Assets;
import fi.kyy.TweenAccessors.ActorAccessor;
import fi.kyy.Utils.AnimatedSprite;
import fi.kyy.Utils.Box2dUtils;
import fi.kyy.Utils.PreferencesBean;

public class GameScreen extends ScreenAdapter implements InputProcessor,
		ContactListener {

	private World world;
	private Box2DDebugRenderer b2dr;

	public static final int VWIDTH = 800;
	public static final int VHEIGHT = 480;
	public static final float STEP = 1 / 60f;
	public static int map;

	private SpriteBatch batch;

	private boolean connected = false;
	public static boolean isHooked = false;

	private OrthographicCamera levelCamera;
	private OrthographicCamera camera;
	private OrthographicCamera stageCamera;

	private DistanceJoint distanceJoint;

	public Body hitBody = null;
	public Body playerBody;
	public Body head, torso, leftLeg, rightLeg, rightArm, leftArm;

	private final Vector2 neckPosition = new Vector2();

	public BodyDef wallDef;
	public FixtureDef wallFDef;

	float armLength;
	float armWidth;
	float torsoRadius;
	float headRadius;
	float marginAttach = 0.2f;

	RopeJointDef ropeDef = new RopeJointDef();
	RevoluteJointDef rjDef = new RevoluteJointDef();

	private TiledMap tileMap;
	private float tileSize;
	private OrthogonalTiledMapRenderer tmr;
	MapProperties prop;

	private float realMapWidth, realMapHeight;
	private int mapWidth, mapHeight;

	public static final short BIT_PLAYER = 2;
	public static final short BIT_RED = 4;
	public static final short BIT_GREEN = 8;
	public static final short BIT_BLUE = 16;
	public static final short BIT_COIN = 32;

	private ArrayList<Body> wallList = new ArrayList<Body>();
	private ArrayList<AnimatedSprite> coinSpriteList = new ArrayList<AnimatedSprite>();

	private float distance = 1;

	private Texture bgTexture;

	// PLAYER SPRITES
	private Sprite spriteHead, spriteBody, spriteLeftLeg, spriteRightLeg,
			spriteRightArm, spriteLeftArm;
	private Sprite rope;
	Sprite coin;

	Body removeBody = null;

	private boolean release, start, timeStarted, playFinished, playDead;
	private long startTime, totalTime;
	private double seconds;

	BitmapFont font;

	private DecimalFormat df;

	private Stage stage, finishedStage;
	private Table container;
	private Skin skin;

	private boolean paused = false;

	private Image gameRetryButton, menuButton, retryButton, nextButton;

	private float scale = Gdx.graphics.getWidth() / 800;

	private int coinAmount;
	private String finalTime, bestTimeSeconds;

	private TweenManager tweenManager;
	private TweenCallback cb;

	private Label worldID, levelID, heading, bestTime, time;

	private Group background, black;

	private Image coinImage, amountImage;
	private int totalCoins;
	private ParticleEffect pEffect, goalEffect, coinEffect;
	private boolean coinParticle = false;
	private Vector2 coinPosition;

	private AnimatedSprite coinAnimation;

	@Override
	public void show() {

		playFinished = true;
		playDead = true;

		Gdx.input.setCatchBackKey(true);

		release = false;
		start = true;
		timeStarted = false;

		tweenManager = new TweenManager();

		Tween.registerAccessor(Actor.class, new ActorAccessor());

		font = Assets.headingFont;
		font.setColor(Color.WHITE);
		font.setScale(0.6f);

		df = new DecimalFormat("#.####");

		Assets.pauseMusic(Assets.completedTheme);

		if (WorldMenuScreen.world == 1) {
			Assets.playMusic(Assets.forestTheme);
			bgTexture = Assets.forestBGTexture;
		}
		if (WorldMenuScreen.world == 2) {
			Assets.playMusic(Assets.waterTheme);
			bgTexture = Assets.waterBGTexture;
		}
		if (WorldMenuScreen.world == 3) {
			Assets.playMusic(Assets.volcanoTheme);
			bgTexture = Assets.volcanoBGTexture;
		}

		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, VWIDTH / PPM, VHEIGHT / PPM);
		levelCamera = new OrthographicCamera();
		levelCamera.setToOrtho(false, VWIDTH, VHEIGHT);
		stageCamera = new OrthographicCamera();
		stageCamera.setToOrtho(false, VWIDTH, VHEIGHT);

		b2dr = new Box2DDebugRenderer();

		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(this);

		pEffect = new ParticleEffect();
		pEffect.load(Gdx.files.internal("assets/particles/aaa.p"),
				Gdx.files.internal("assets/particles/"));
		goalEffect = new ParticleEffect();
		goalEffect.load(Gdx.files.internal("assets/particles/maali.p"),
				Gdx.files.internal("assets/particles/"));
		coinEffect = new ParticleEffect();
		coinEffect.load(Gdx.files.internal("assets/particles/coin.p"),
				Gdx.files.internal("assets/coin/"));
		
		pEffect.start();

		createLevel();
		createPlayer();

		stage = new Stage(new StretchViewport(800, 480, stageCamera));
		finishedStage = new Stage(new StretchViewport(800, 480, stageCamera));

		black = new Group();
		black.setBounds(0, 0, 1280, 768);
		black.setCenterPosition(1280 / 2, 768 / 2);
		black.addActor(new Image(Assets.black));

		gameRetryButton = new Image(Assets.retryButton_small1);
		gameRetryButton.setX(20);
		gameRetryButton.setY(480 - gameRetryButton.getHeight() - 20);
		gameRetryButton.setScale(1f);
		gameRetryButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				isHooked = false;
				distanceJoint = null;
				gameRetryButton.setScale(0.96f * gameRetryButton.getScaleX());
				Assets.playSound(Assets.buttonSound);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				gameRetryButton.setScale(1f);
				((SuperHands) Gdx.app.getApplicationListener())
						.setScreen(new GameScreen());
			}

		});

		stage.addActor(black);
		stage.addActor(gameRetryButton);

		InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(finishedStage);
		multiplexer.addProcessor(this);

		Gdx.input.setInputProcessor(multiplexer);

		cb = new TweenCallback() {
			@Override
			public void onEvent(int type, BaseTween<?> source) {
				start = false;
			}
		};

		Timeline.createSequence().beginSequence().beginParallel()
				.push(Tween.set(black, ActorAccessor.ALPHA).target(1))
				.push(Tween.to(black, ActorAccessor.ALPHA, .7f).target(0))
				.end().setCallback(cb)
				.setCallbackTriggers(TweenCallback.COMPLETE)
				.start(tweenManager);

	}

	private void initSprites() {

		rope = Assets.rope;

		spriteRightArm = Assets.voodooRightArm;
		spriteLeftArm = Assets.voodooLeftArm;
		spriteLeftLeg = Assets.voodooLeftLeg;
		spriteRightLeg = Assets.voodooRightLeg;
		spriteHead = Assets.voodooHead;
		spriteBody = Assets.voodooTorso;

		spriteHead.setSize(headRadius * 2, headRadius * 2);
		spriteBody.setSize(torsoRadius * 2, torsoRadius * 2);
		spriteLeftLeg.setSize(50 / PPM / 5, 60 / PPM / 5);
		spriteRightLeg.setSize(50 / PPM / 5, 60 / PPM / 5);
		spriteRightArm.setSize(armWidth, armLength);
		spriteLeftArm.setSize(armWidth, armLength);

		spriteHead.setAlpha(1);
		spriteBody.setAlpha(1);
		spriteRightArm.setAlpha(1);
		spriteRightLeg.setAlpha(1);
		spriteLeftArm.setAlpha(1);
		spriteLeftLeg.setAlpha(1);

		updateSprite(spriteBody, torso);
		updateSprite(spriteHead, head);
		updateSprite(spriteRightLeg, rightLeg);
		updateSprite(spriteLeftLeg, leftLeg);
		updateSprite(spriteRightArm, rightLeg);
		updateSprite(spriteLeftArm, leftLeg);

	}

	public void createPlayer() {

		final short categoryBits = 0x0002;
		final short maskBits = 0x0004;

		MapLayer neckLayer;
		neckLayer = (MapLayer) tileMap.getLayers().get("player");

		createPlayerPosition(neckLayer);

		armLength = 70 / PPM / 5;
		torsoRadius = 73 / PPM / 5;
		headRadius = 110 / PPM / 5;
		armWidth = 50 / PPM / 5;

		float playerDensity = 0.075f;
		float legDensity = 0.075f;

		head = Box2dUtils.createCircle(world, BodyType.DynamicBody, headRadius,
				.01f, categoryBits, maskBits);
		head.setTransform(neckPosition.x, neckPosition.y + headRadius - 0.1f, 0);
		head.setActive(true);

		torso = Box2dUtils.createCircle(world, BodyType.DynamicBody,
				torsoRadius, .04f, categoryBits, maskBits);
		torso.setTransform(neckPosition.x - torsoRadius / 2f, neckPosition.y
				- torsoRadius / 2f, 0);
		torso.setActive(true);

		leftArm = Box2dUtils.createBox(world, BodyType.DynamicBody, armWidth,
				armLength, playerDensity, categoryBits, maskBits);
		leftArm.setTransform(neckPosition.x + 0.3f, neckPosition.y
				- torsoRadius / 2, 0);
		leftArm.setActive(true);

		rightArm = Box2dUtils.createBox(world, BodyType.DynamicBody, armWidth,
				armLength, playerDensity, categoryBits, maskBits);
		rightArm.setTransform(neckPosition.x,
				neckPosition.y - torsoRadius / 10, 0);
		rightArm.setActive(true);

		leftLeg = Box2dUtils.createBox(world, BodyType.DynamicBody, armWidth,
				armLength, legDensity, categoryBits, maskBits);
		leftLeg.setTransform(neckPosition.x + 0.5f, neckPosition.y
				- (torsoRadius) + 0.3f, 0);
		leftLeg.setActive(true);

		rightLeg = Box2dUtils.createBox(world, BodyType.DynamicBody, armWidth,
				armLength, legDensity, categoryBits, maskBits);
		rightLeg.setTransform(neckPosition.x, neckPosition.y - (torsoRadius)
				+ 0.3f, 0);
		rightLeg.setActive(true);

		// JOIN HEAD AND TORSO
		rjDef.bodyA = head;
		rjDef.bodyB = torso;
		rjDef.localAnchorA.set(0, -1f * headRadius);
		rjDef.localAnchorB.set(0, torsoRadius / 2);
		rjDef.collideConnected = false;
		rjDef.enableLimit = true;
		rjDef.lowerAngle = (float) (-Math.PI / 8f);
		rjDef.upperAngle = (float) (Math.PI / 8f);
		world.createJoint(rjDef);

		// JOINT BETWEEN TORSO AND LEFT ARM
		rjDef.bodyA = torso;
		rjDef.bodyB = leftArm;
		rjDef.localAnchorA.set(0.1f, torsoRadius / 100 - 0.2f);
		rjDef.localAnchorB.set(0, -armLength / 2f);
		rjDef.collideConnected = false;
		rjDef.enableLimit = true;
		rjDef.lowerAngle = (float) (-Math.PI);
		rjDef.upperAngle = (float) (Math.PI * 0);
		world.createJoint(rjDef);

		// TORSO AND RIGHT ARM
		rjDef.bodyA = torso;
		rjDef.bodyB = rightArm;
		rjDef.localAnchorA.set(0, torsoRadius / 100);
		rjDef.localAnchorB.set(0f, -armLength / 2f);
		rjDef.collideConnected = false;
		rjDef.enableLimit = false;
		world.createJoint(rjDef);

		// TORSO AND LEFT LEG
		rjDef.bodyA = torso;
		rjDef.bodyB = leftLeg;
		rjDef.localAnchorA.set(.1f, -torsoRadius + 0.14f);
		rjDef.localAnchorB.set(0, armLength / 2f);
		rjDef.enableLimit = true;
		rjDef.collideConnected = false;
		rjDef.lowerAngle = (float) (-Math.PI / 8f);
		rjDef.upperAngle = (float) (Math.PI / 8f);
		world.createJoint(rjDef);

		// TORSO AND RIGHT LEG
		rjDef.bodyA = torso;
		rjDef.bodyB = rightLeg;
		rjDef.localAnchorA.set(0, -torsoRadius + 0.12f);
		rjDef.localAnchorB.set(0, armLength / 2f);
		rjDef.collideConnected = false;
		rjDef.enableLimit = true;
		rjDef.lowerAngle = (float) (-Math.PI / 8f);
		rjDef.upperAngle = (float) (Math.PI / 8f);
		world.createJoint(rjDef);

		head.setUserData("player");
		torso.setUserData("player");
		leftLeg.setUserData("player");
		rightLeg.setUserData("player");
		leftArm.setUserData("player");
		rightArm.setUserData("player");

		initSprites();

	}

	private final Vector2 spritePosition = new Vector2();
	private final Vector2 spriteOrigin = new Vector2();

	private void updateSprite(Sprite sprite, Body body) {

		spritePosition.set(body.getTransform().getPosition());
		spriteOrigin.set(spritePosition.x - sprite.getWidth() / 2f,
				spritePosition.y - sprite.getHeight() / 2f);
		spritePosition.add(spriteOrigin.sub(spritePosition).rotate(
				body.getTransform().getRotation()));

		sprite.setOrigin(sprite.getWidth() / 2f, sprite.getHeight() / 2f);
		sprite.setRotation((float) (Math.toDegrees(body.getAngle())));
		sprite.setPosition(spritePosition.x, spritePosition.y);

	}

	public void createLevel() {

		wallDef = new BodyDef();
		wallFDef = new FixtureDef();

		tileMap = Assets.getLevel(map);
		tmr = new OrthogonalTiledMapRenderer(tileMap);

		prop = tileMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);

		tileSize = tileMap.getProperties().get("tilewidth", Integer.class);
		realMapWidth = mapWidth * tileSize;
		realMapHeight = mapHeight * tileSize;

		TiledMapTileLayer layer;
		MapLayer coinLayer;

		layer = (TiledMapTileLayer) tileMap.getLayers().get("red");
		createLayer(layer, BIT_RED, 1);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("blue");
		createLayer(layer, BIT_RED, 2);
		coinLayer = (MapLayer) tileMap.getLayers().get("crystals");
		createCoinLayer(coinLayer, BIT_RED);
		layer = (TiledMapTileLayer) tileMap.getLayers().get("green");
		createLayer(layer, BIT_RED, 3);

	}

	private void createCoinLayer(MapLayer layer, short bits) {

		wallDef = new BodyDef();
		wallFDef = new FixtureDef();

		for (MapObject mo : layer.getObjects()) {
			wallDef.type = BodyType.StaticBody;

			if (mo instanceof EllipseMapObject) {
				Ellipse e = ((EllipseMapObject) mo).getEllipse();
				float x = e.x;
				float y = e.y;

				coin = new Sprite(Assets.coin);
				wallDef.position.set(x / PPM, y / PPM);

				coinAnimation = new AnimatedSprite(Assets.coinAnimation);
				coinAnimation.setBounds(x / PPM - 12 / PPM, y / PPM - 12 / PPM,
						32 / PPM, 32 / PPM);

				coinAnimation.setAutoUpdate(false);
				coinSpriteList.add(coinAnimation);

			}

			CircleShape cshape = new CircleShape();
			cshape.setRadius(18 / PPM);

			wallFDef.shape = cshape;
			wallFDef.isSensor = true;
			wallFDef.filter.categoryBits = BIT_RED;
			wallFDef.filter.maskBits = BIT_PLAYER;

			Body body = world.createBody(wallDef);
			body.createFixture(wallFDef);
			body.setUserData("coin");
			body.setActive(true);

			wallList.add(body);

		}

		coinAmount = wallList.size();
		PreferencesBean.setIntegerPreference("map" + Integer.toString(map)
				+ "alllevelcoins", coinAmount);

	}

	private void createPlayerPosition(MapLayer layer) {

		for (MapObject mo : layer.getObjects()) {
			if (mo instanceof EllipseMapObject) {
				Ellipse e = ((EllipseMapObject) mo).getEllipse();
				float x = e.x;
				float y = e.y;

				neckPosition.set(x / PPM, y / PPM);
				pEffect.setPosition(x / PPM, y / PPM);

			}
		}

	}

	private void createLayer(TiledMapTileLayer layer, short bits, int j) {

		PolygonShape boxPoly = new PolygonShape();
		boxPoly.setAsBox(tileSize / PPM / 2, tileSize / PPM / 2);

		for (int row = 0; row < layer.getHeight(); row++) {
			for (int col = 0; col < layer.getWidth(); col++) {

				Cell cell = layer.getCell(col, row);
				if (cell == null)
					continue;
				if (cell.getTile() == null)
					continue;

				wallDef.type = BodyType.StaticBody;
				wallDef.position.set((col + 0.5f) * tileSize / PPM,
						(row + 0.5f) * tileSize / PPM);

				ChainShape cs = new ChainShape();
				Vector2[] v = new Vector2[4];
				v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
				v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
				v[3] = new Vector2(tileSize / 2 / PPM, -tileSize / 2 / PPM);
				cs.createChain(v);
				wallFDef.friction = .5f;
				wallFDef.shape = cs;
				wallFDef.filter.categoryBits = bits;
				wallFDef.filter.maskBits = BIT_PLAYER;

				Body wallBody = world.createBody(wallDef);

				if (j == 3) {
					wallFDef.isSensor = true;
					wallBody.setUserData("goal");
				} else if (j == 2) {
					wallFDef.isSensor = true;
					wallBody.setUserData("decoratives");
				} else if (j == 1) {
					wallFDef.isSensor = false;
					wallBody.setUserData("wall");
				}

				wallBody.createFixture(boxPoly, 0);
				wallBody.createFixture(wallFDef);
				wallBody.setActive(true);
			}
		}

	}

	@Override
	public void render(float delta) {

		if (!paused) {
			world.step(STEP * 1.2f, 8, 3);
			if (start) {
				world.setGravity(new Vector2(0, -0.2f));
			} else {
				if (timeStarted == false) {
					startTime = System.nanoTime();
					timeStarted = true;
				}
				totalTime = -startTime + System.nanoTime();
				seconds = (double) totalTime / 1000000000.0f;
				world.setGravity(new Vector2(0, -9.81f));
			}

		}

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(bgTexture, 0, 0, realMapWidth / PPM, realMapHeight / PPM);

		if (coinParticle) {
			coinEffect.setPosition(coinPosition.x, coinPosition.y);
			coinEffect.draw(batch, delta);
		}

		batch.end();

		tmr.setView(levelCamera);
		tmr.render();

		// DEBUGGAUSTA VARTEN
		// b2dr.render(world, camera.combined);

		manageHook();

		Vector2 pos = torso.getWorldCenter();

		if (pos.x <= camera.viewportWidth / 2) {
			pos.x = camera.viewportWidth / 2;
		}

		if (pos.y <= camera.viewportHeight / 2) {
			pos.y = camera.viewportHeight / 2;
		}

		if (pos.x * PPM >= realMapWidth - camera.viewportWidth * PPM / 2) {
			pos.x = realMapWidth / PPM - camera.viewportWidth / 2;
		}

		if (pos.y * PPM >= realMapHeight - camera.viewportHeight * PPM / 2) {
			pos.y = realMapHeight / PPM - camera.viewportHeight / 2;
		}

		levelCamera.position.set(pos.x * PPM, pos.y * PPM, 0);
		camera.position.set(pos.x, pos.y, 0);

		levelCamera.update();
		camera.update();
		stageCamera.update();

		renderPlayer(rightArm, delta);

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (int i = 0; i < coinSpriteList.size(); i++) {
			coinSpriteList.get(i).update(STEP);
			coinSpriteList.get(i).draw(batch);
		}
		batch.end();

		batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch.begin();

		if (paused) {
			finishedStage.act(delta);
			finishedStage.draw();
		}

		font.setScale(0.1f + scale * 0.65f);
		finalTime = df.format(seconds);
		font.draw(batch, "Time: " + finalTime, 4, 30 * scale * 1.1f);
		batch.end();
		tweenManager.update(delta);
		stage.act(delta);
		stage.draw();

	}

	Matrix4 transform = new Matrix4();

	public void renderPlayer(Body body, float delta) {

		Vector2 pos = body.getWorldCenter();

		transform.setToTranslation(pos.x, pos.y, 0);
		transform.rotate(0, 0, 1, ((float) ((Math.atan2(testPoint.x - pos.x,
				-(testPoint.y - pos.y)) * 180.0d / Math.PI) + 180.0f)));
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		pEffect.setPosition(pos.x, pos.y);
		pEffect.draw(batch, delta);

		if (paused) {
			goalEffect.setPosition(pos.x, pos.y);
			goalEffect.draw(batch, delta);
		}

		updateSprite(spriteHead, head);
		updateSprite(spriteLeftArm, leftArm);
		spriteLeftArm.draw(batch);
		updateSprite(spriteLeftLeg, leftLeg);
		spriteLeftLeg.draw(batch);
		updateSprite(spriteRightLeg, rightLeg);
		spriteRightLeg.draw(batch);
		updateSprite(spriteBody, torso);
		spriteBody.draw(batch);
		spriteHead.draw(batch);
		batch.end();

		if (distanceJoint != null && isHooked) {
			renderRope(0.9f, 0.9f);
		}

		batch.begin();
		updateSprite(spriteRightArm, rightArm);
		spriteRightArm.draw(batch);
		batch.end();

	}

	private void renderRope(float halfWidth, float halfHeight) {

		Vector2 pos = rightArm.getWorldCenter();
		float angle = rightArm.getAngle();

		transform.setToTranslation(pos.x, pos.y, 0);
		transform.rotate(0, 0, 1, (float) Math.toDegrees(angle));

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		/*
		 * hand = new Sprite(new Texture("img/arm-b.png"));
		 * 
		 * hand.flip(true, false); hand.setSize(halfWidth * 1f, halfHeight *
		 * 1.5f); hand.setOrigin(hand.getWidth() / 2, hand.getHeight() / 2);
		 * hand.setPosition(testPoint.x - hand.getWidth() / 2, testPoint.y -
		 * hand.getHeight() / 2); hand.setRotation((float) ((Math.atan2 (pos.x -
		 * testPoint.x, -(pos.y - testPoint.y))*180.0d/Math.PI)+180.0f));
		 */

		distance = rightArm.getWorldCenter().dst(
				new Vector2(testPoint.x, testPoint.y));
		rope.setOrigin(0.20f, 0.05f);
		rope.setPosition(pos.x, pos.y);
		rope.setRotation((float) ((Math.atan2(pos.x - testPoint.x,
				-(pos.y - testPoint.y)) * 180.0d / Math.PI) + 180.0f));
		rope.setBounds(testPoint.x, testPoint.y, 0.9f, distance + 0.1f);
		rope.draw(batch);
		batch.end();

	}

	Vector3 testPoint = new Vector3();
	Vector3 hitPoint = new Vector3();

	QueryCallback callback = new QueryCallback() {
		@Override
		public boolean reportFixture(Fixture fixture) {

			if (fixture.testPoint(testPoint.x, testPoint.y)) {
				hitBody = fixture.getBody();

				float distancee = rightArm.getPosition().dst(
						new Vector2(testPoint.x, testPoint.y));

				if (distancee < 2000 && hitBody.getUserData() == "wall") {
					Assets.playSound(Assets.ropeSound);

					MouseJointDef def = new MouseJointDef();
					def.bodyA = playerBody;
					def.bodyB = hitBody;
					def.collideConnected = true;

					def.target.set(testPoint.x, testPoint.y);
					DistanceJointDef distanceJointDef = new DistanceJointDef();
					distanceJointDef.initialize(rightArm, hitBody, rightArm
							.getWorldCenter(), new Vector2(testPoint.x,
							testPoint.y));

					distanceJointDef.collideConnected = true;
					distanceJoint = (DistanceJoint) world
							.createJoint(distanceJointDef);
					isHooked = true;
					release = true;
					connected = true;
					hitBody.setAwake(true);
					return false;

				} else {
					if (distanceJoint != null) {
						world.destroyJoint(distanceJoint);
						distanceJoint = null;
						connected = false;
						isHooked = false;
					}
					return true;

				}

			}

			return true;
		}
	};

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		testPoint.set(screenX, screenY, 0);
		camera.unproject(testPoint);
		hitBody = null;

		if (connected == false && release == false) {
			world.QueryAABB(callback, testPoint.x - 0.3f, testPoint.y - 0.3f,
					testPoint.x + 0.3f, testPoint.y + 0.3f);
		} else if (connected == true && release == true) {
			release = false;
			world.destroyJoint(distanceJoint);
			distanceJoint = null;
			isHooked = false;
			connected = false;
		}

		return false;

	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {

		return false;

	}

	private void manageHook() {
		if (isHooked && distanceJoint != null) {
			rightArm.setAwake(true);
			distanceJoint.setLength((float) (distanceJoint.getLength() * 0.99));

		}
	}

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();

		if (fa.getBody().getUserData() == "wall"
				&& fb.getBody().getUserData() == "player") {
			if (playDead) {
				Assets.playSound(Assets.dead);
				playDead = false;
			}
			((SuperHands) Gdx.app.getApplicationListener())
					.setScreen(new GameScreen());
		}

		if (fb.getBody().getUserData() == "coin") {
			System.out.println("Get coin");
			int j = wallList.indexOf(fb.getBody());
			coinPosition = new Vector2(fb.getBody().getWorldCenter().x, fb
					.getBody().getWorldCenter().y);
			if (j >= 0) {
				coinEffect.start();
				coinParticle = true;

				Assets.playSound(Assets.coinSound);
				coinSpriteList.remove(j);
			}

			wallList.remove(fb.getBody());
			System.out.println("" + wallList.size());

		}

		if (fa.getBody().getUserData() == "coin") {
			System.out.println("Get coin");
			int j = wallList.indexOf(fa.getBody());
			coinPosition = new Vector2(fa.getBody().getWorldCenter().x, fa
					.getBody().getWorldCenter().y);
			if (j >= 0) {
				coinEffect.start();
				coinParticle = true;

				Assets.playSound(Assets.coinSound);
				coinSpriteList.remove(j);
			}

			wallList.remove(fa.getBody());
			System.out.println("" + wallList.size());

		}

		if (fa.getBody().getUserData() == "goal"
				|| fb.getBody().getUserData() == "goal") {
			System.out.println("Go to goal");

			goalEffect.setPosition(torso.getWorldCenter().x,
					torso.getWorldCenter().y);
			goalEffect.start();
			
			spriteHead.setAlpha(0);
			spriteBody.setAlpha(0);
			spriteRightArm.setAlpha(0);
			spriteRightLeg.setAlpha(0);
			spriteLeftArm.setAlpha(0);
			spriteLeftLeg.setAlpha(0);

			if (playFinished) {
				Assets.playSound(Assets.finish);
				playFinished = false;
			}

			if (PreferencesBean.getBooleanPreferences(Integer.toString(map)
					+ "played", false) == false) {
				PreferencesBean.setLongPreference(Integer.toString(map)
						+ "time", totalTime);
			} else {
				if (totalTime < PreferencesBean.getLongPreference(
						Integer.toString(map) + "time", 99999)) {
					PreferencesBean.setLongPreference(Integer.toString(map)
							+ "time", totalTime);
				}
			}

			PreferencesBean.setBooleanPreference(Integer.toString(map)
					+ "completed", true);
			PreferencesBean.setBooleanPreference(Integer.toString(map)
					+ "played", true);

			totalCoins = coinAmount - wallList.size();

			if (totalCoins > PreferencesBean.getIntegerPreference("map"
					+ Integer.toString(map) + "totalcoins", 0)) {
				PreferencesBean.setIntegerPreference(
						"map" + Integer.toString(map) + "totalcoins",
						totalCoins);
			}

			int allCoins = 0;

			for (int i = 1; i <= 30; i++) {
				allCoins += PreferencesBean.getIntegerPreference("map" + i
						+ "totalcoins", 0);
			}
			if (allCoins > 100 - 10) {
				PreferencesBean.setBooleanPreference("waterUnlocked", true);
			}
			if (map == 60) {
				PreferencesBean.setBooleanPreference("volcanoUnlocked", true);
			}
			paused = true;
			isHooked = false;
			createFinishedStage();

		}

	}

	public void createFinishedStage() {

		pEffect.setDuration(1);

		if (WorldMenuScreen.world == 1) {
			Assets.pauseMusic(Assets.forestTheme);
		}
		if (WorldMenuScreen.world == 2) {
			Assets.pauseMusic(Assets.waterTheme);
		}
		if (WorldMenuScreen.world == 3) {
			Assets.pauseMusic(Assets.volcanoTheme);
		}

		Assets.playMusic(Assets.completedTheme);

		if (totalTime > PreferencesBean.getLongPreference(Integer.toString(map)
				+ "time", 99999)) {
			seconds = (double) PreferencesBean.getLongPreference(
					Integer.toString(map) + "time", 99999) / 1000000000.0f;
			bestTimeSeconds = df.format(seconds);
		} else {
			PreferencesBean.setLongPreference(Integer.toString(map) + "time",
					totalTime);
			seconds = (double) totalTime / 1000000000.0f;
			bestTimeSeconds = df.format(seconds);
		}

		seconds = (double) totalTime / 1000000000.0f;
		finalTime = df.format(seconds);

		stage.clear();
		finishedStage.clear();
		tweenManager.killAll();

		background = new Group();
		background.setBounds(0, 0, Gdx.graphics.getWidth() * scale,
				Gdx.graphics.getHeight() * scale);
		background.setSize(Gdx.graphics.getWidth() * scale,
				Gdx.graphics.getHeight() * scale);
		background.setCenterPosition(Gdx.graphics.getWidth() / 2,
				Gdx.graphics.getHeight() / 2);
		background.setPosition(0, 0);

		black = new Group();
		black.setBounds(0, 0, 1280, 768);
		black.setCenterPosition(1280 / 2, 768 / 2);
		black.addActor(new Image(Assets.black));

		container = new Table();

		Table coinTable = new Table();
		coinTable.left();
		coinTable.setBounds(0, 0, 400, 480);

		Table textTable = new Table();
		textTable.right();
		textTable.setBounds(400, 0, 400, 480);

		container.center();
		container.setBounds(0, 0, 800, 480);

		finishedStage.addActor(background);
		finishedStage.addActor(black);

		TextureRegion finishedBG = Assets.finishedWindow;
		TextureRegion comp = Assets.completed;
		Image asd = new Image(comp);
		asd.setPosition(800 / 2 - asd.getWidth() / 2 + 3,
				480 - asd.getHeight() - 70);

		if (coinAmount == totalCoins) {
			coinImage = new Image(Assets.coinImage);
		} else {
			coinImage = new Image(Assets.coinImageNot);
		}

		amountImage = new Image(new TextureRegion(
				Assets.amount.findRegion(Integer.toString(totalCoins) + "x")));

		background.addActor(new Image(finishedBG));
		background.addActor(asd);

		BitmapFont font = Assets.font;
		LabelStyle style = new LabelStyle(font, Color.WHITE);

		TextureAtlas atlas = new TextureAtlas("assets/uiskin.atlas");
		skin = new Skin(Gdx.files.internal("assets/uiskin.json"), atlas);

		worldID = new Label("THE FOREST", style);
		worldID.setFontScale(.6f);
		// worldID.setPosition(800 / 2 - 130, 260);

		levelID = new Label("LEVEL: " + Integer.toString(map), style);
		levelID.setFontScale(.5f);
		// levelID.setPosition(800 / 2 - 130, 220);

		time = new Label("TIME: " + finalTime + " s", style);
		time.setFontScale(.5f);
		// time.setPosition(800 / 2 - 130, 180);

		bestTime = new Label("BEST: " + bestTimeSeconds + " s", style);
		bestTime.setFontScale(.5f);
		// bestTime.setPosition(800 / 2 - 130, 480 + 140);

		finishedStage.addActor(container);

		container.add(textTable);
		container.add(coinTable);
		container.row();
		container.add(coinImage);
		container.row();
		textTable.add(worldID).center().padTop(10);
		textTable.row();
		textTable.add(levelID).center();
		textTable.row();
		textTable.add(time).center();
		textTable.row();
		textTable.add(bestTime).center();

		coinTable.stack(coinImage, amountImage).padLeft(10 * scale);

		nextButton = new Image(Assets.nextButton_small);
		nextButton.setX(800 - 252);
		nextButton.setY(64);
		nextButton.setScale(1f);
		nextButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				nextButton.setScale(0.96f * nextButton.getScaleX());
				Assets.playSound(Assets.buttonSound);
				Assets.pauseMusic(Assets.completedTheme);
				Assets.playSound(Assets.menuSwoosh);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				cb = new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (PreferencesBean.getBooleanPreferences("adsOff",
								false) == false) {
							 SuperHands.actionResolver.displayInterstitial();
						}
						paused = false;
						isHooked = false;
						map++;
						tweenManager.killAll();
						((SuperHands) Gdx.app.getApplicationListener())
								.setScreen(new GameScreen());
					}
				};

				Timeline.createSequence()
						.beginSequence()
						.beginParallel()
						.push(Tween.to(background, 3, 0.6f).target(0, 480 + 0)
								.ease(Elastic.IN))
						.push(Tween.to(heading, 3, 0.6f)
								.target(800 / 2 - 100, 480 + 400)
								.ease(Elastic.IN))
						.push(Tween.to(worldID, 3, 0.6f).target(0, 480 + 260)
								.ease(Elastic.IN))
						.push(Tween.to(levelID, 3, 0.6f).target(0, 480 + 220)
								.ease(Elastic.IN))
						.push(Tween.to(time, 3, 0.6f).target(0, 480 + 180)
								.ease(Elastic.IN))
						.push(Tween.to(bestTime, 3, 0.6f).target(0, 480 + 140)
								.ease(Elastic.IN))
						.push(Tween.to(retryButton, 3, 0.6f)
								.target(204, 480 + 64).ease(Elastic.IN))
						.push(Tween
								.to(menuButton, 3, 0.6f)
								.target(800 / 2 - menuButton.getWidth() / 2 + 2,
										480 + 66).ease(Elastic.IN))
						.push(Tween.to(nextButton, 3, 0.6f)
								.target(800 - 252, 480 + 64).ease(Elastic.IN))
						.push(Tween.to(coinImage, 3, 0.6f).target(0, 480 + 140)
								.ease(Elastic.IN))
						.push(Tween.to(amountImage, 3, 0.6f)
								.target(0, 480 + 140).ease(Elastic.IN))
						.push(Tween.to(background, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(heading, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(worldID, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(levelID, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(time, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(bestTime, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(retryButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(nextButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(menuButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(coinImage, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(amountImage, ActorAccessor.ALPHA, .4f)
								.target(0))
						.end()
						.push(Tween.to(black, ActorAccessor.ALPHA, .5f).target(
								1)).end().setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.start(tweenManager);

			}

		});

		retryButton = new Image(Assets.retryButton_small);
		retryButton.setX(204);
		retryButton.setY(64);
		retryButton.setScale(1f);
		retryButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				retryButton.setScale(0.96f * retryButton.getScaleX());
				Assets.playSound(Assets.buttonSound);
				Assets.pauseMusic(Assets.completedTheme);
				Assets.playSound(Assets.menuSwoosh);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				cb = new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						if (PreferencesBean.getBooleanPreferences("adsOff",
								false) == false) {
							 SuperHands.actionResolver.displayInterstitial();
						}
						tweenManager.killAll();
						((SuperHands) Gdx.app.getApplicationListener())
								.setScreen(new GameScreen());
					}
				};

				Timeline.createSequence()
						.beginSequence()
						.beginParallel()
						.push(Tween.to(background, 3, 0.6f).target(0, 480 + 0)
								.ease(Elastic.IN))
						.push(Tween.to(heading, 3, 0.6f)
								.target(800 / 2 - 100, 480 + 400)
								.ease(Elastic.IN))
						.push(Tween.to(worldID, 3, 0.6f).target(0, 480 + 260)
								.ease(Elastic.IN))
						.push(Tween.to(levelID, 3, 0.6f).target(0, 480 + 220)
								.ease(Elastic.IN))
						.push(Tween.to(time, 3, 0.6f).target(0, 480 + 180)
								.ease(Elastic.IN))
						.push(Tween.to(bestTime, 3, 0.6f).target(0, 480 + 140)
								.ease(Elastic.IN))
						.push(Tween.to(retryButton, 3, 0.6f)
								.target(204, 480 + 64).ease(Elastic.IN))
						.push(Tween
								.to(menuButton, 3, 0.6f)
								.target(800 / 2 - menuButton.getWidth() / 2 + 2,
										480 + 66).ease(Elastic.IN))
						.push(Tween.to(nextButton, 3, 0.6f)
								.target(800 - 252, 480 + 64).ease(Elastic.IN))
						.push(Tween.to(coinImage, 3, 0.6f).target(0, 480 + 140)
								.ease(Elastic.IN))
						.push(Tween.to(amountImage, 3, 0.6f)
								.target(0, 480 + 140).ease(Elastic.IN))
						.push(Tween.to(background, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(heading, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(worldID, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(levelID, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(time, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(bestTime, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(retryButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(nextButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(menuButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(coinImage, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(amountImage, ActorAccessor.ALPHA, .4f)
								.target(0))
						.end()
						.push(Tween.to(black, ActorAccessor.ALPHA, .5f).target(
								1)).end().setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.start(tweenManager);

			}
		});

		menuButton = new Image(Assets.menuButton_small);
		menuButton.setX(800 / 2 - menuButton.getWidth() / 2 + 2);
		menuButton.setY(66);
		menuButton.setScale(1f);
		menuButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				menuButton.setScale(0.96f * menuButton.getScaleX());
				Assets.playSound(Assets.buttonSound);
				Assets.pauseMusic(Assets.completedTheme);
				Assets.playSound(Assets.menuSwoosh);
				return true;
			}

			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {

				cb = new TweenCallback() {
					@Override
					public void onEvent(int type, BaseTween<?> source) {
						// finishedStage.dispose();
						if (PreferencesBean.getBooleanPreferences("adsOff",
								false) == false) {
							 SuperHands.actionResolver.displayInterstitial();
						}
						tweenManager.killAll();
						((SuperHands) Gdx.app.getApplicationListener())
								.setScreen(new LevelSelectScreen());
					}
				};

				Timeline.createSequence()
						.beginSequence()
						.beginParallel()
						.push(Tween.to(background, 3, 0.6f).target(0, 480 + 0)
								.ease(Elastic.IN))
						.push(Tween.to(heading, 3, 0.6f)
								.target(800 / 2 - 100, 480 + 400)
								.ease(Elastic.IN))
						.push(Tween.to(worldID, 3, 0.6f).target(0, 480 + 260)
								.ease(Elastic.IN))
						.push(Tween.to(levelID, 3, 0.6f).target(0, 480 + 220)
								.ease(Elastic.IN))
						.push(Tween.to(time, 3, 0.6f).target(0, 480 + 180)
								.ease(Elastic.IN))
						.push(Tween.to(bestTime, 3, 0.6f).target(0, 480 + 140)
								.ease(Elastic.IN))
						.push(Tween.to(retryButton, 3, 0.6f)
								.target(204, 480 + 64).ease(Elastic.IN))
						.push(Tween
								.to(menuButton, 3, 0.6f)
								.target(800 / 2 - menuButton.getWidth() / 2 + 2,
										480 + 66).ease(Elastic.IN))
						.push(Tween.to(nextButton, 3, 0.6f)
								.target(800 - 252, 480 + 64).ease(Elastic.IN))
						.push(Tween.to(coinImage, 3, 0.6f).target(0, 480 + 140)
								.ease(Elastic.IN))
						.push(Tween.to(amountImage, 3, 0.6f)
								.target(0, 480 + 140).ease(Elastic.IN))
						.push(Tween.to(background, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(heading, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(worldID, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(levelID, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(time, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(bestTime, ActorAccessor.ALPHA, .7f)
								.target(0))
						.push(Tween.to(retryButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(nextButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(menuButton, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(coinImage, ActorAccessor.ALPHA, .4f)
								.target(0))
						.push(Tween.to(amountImage, ActorAccessor.ALPHA, .4f)
								.target(0))
						.end()
						.push(Tween.to(black, ActorAccessor.ALPHA, .5f).target(
								1)).end().setCallback(cb)
						.setCallbackTriggers(TweenCallback.COMPLETE)
						.start(tweenManager);

			}

		});

		finishedStage.addActor(menuButton);
		finishedStage.addActor(retryButton);
		finishedStage.addActor(nextButton);

		Timeline.createSequence()
				.beginSequence()
				.beginParallel()
				.push(Tween.set(black, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(background, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(heading, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(worldID, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(levelID, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(time, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(bestTime, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(retryButton, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(menuButton, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(nextButton, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(coinImage, ActorAccessor.ALPHA).target(0))
				.push(Tween.set(amountImage, ActorAccessor.ALPHA).target(0))
				.push(Tween.to(background, ActorAccessor.ALPHA, .4f).target(1))
				.push(Tween.to(heading, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(worldID, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(levelID, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(time, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(bestTime, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(retryButton, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(nextButton, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(menuButton, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(coinImage, ActorAccessor.ALPHA, .7f).target(1))
				.push(Tween.to(amountImage, ActorAccessor.ALPHA, 1f).target(1))
				.end().start(tweenManager);
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
	public boolean keyDown(int keycode) {
		if (keycode == Keys.BACK) {

			if (PreferencesBean.getBooleanPreferences("adsOff", false) == false) {
				SuperHands.actionResolver.displayInterstitial();
			}
			if (WorldMenuScreen.world == 1) {
				Assets.pauseMusic(Assets.forestTheme);
			}
			if (WorldMenuScreen.world == 2) {
				Assets.pauseMusic(Assets.waterTheme);
			}
			if (WorldMenuScreen.world == 3) {
				Assets.pauseMusic(Assets.volcanoTheme);
			}

			Assets.pauseMusic(Assets.completedTheme);
			paused = false;
			isHooked = false;
			((SuperHands) Gdx.app.getApplicationListener())
					.setScreen(new LevelSelectScreen());
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
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

}


//
// WORMS-tyyli (Ray cast)
//

/*
 * private boolean isHit; Vector2 collision = new Vector2(), normal = new
 * Vector2();
 * 
 * RayCastCallback rayCallback = new RayCastCallback() {
 * 
 * @Override public float reportRayFixture(Fixture fixture, Vector2 point,
 * Vector2 normal, float fraction) {
 * 
 * hitBody = fixture.getBody(); //distance = rightArm.getPosition().dst(new
 * Vector2(testPoint.x, testPoint.y));
 * 
 * if (hitBody.getUserData() == "wall") {
 * 
 * Vector2 osuma = new Vector2(); if (point.x < 0) { point.x = point.x * -1; }
 * if (point.y < 0) { point.y = point.y * -1; } collision.set(point);
 * osuma.set(point); osumaList.add(osuma); isHit = true; return fraction;
 * 
 * } else { return -1; }
 * 
 * } };
 * 
 * @Override public boolean touchDown(int screenX, int screenY, int pointer, int
 * button) {
 * 
 * if (start == false) {
 * 
 * testPoint.set(screenX, screenY, 0); camera.unproject(testPoint);
 * 
 * Vector2 pos = torso.getWorldCenter();
 * 
 * Vector2 direction = new Vector2();
 * 
 * hitBody = null;
 * 
 * //world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f,
 * testPoint.x + 0.1f, testPoint.y + 0.1f);
 * 
 * 
 * if (release == false) { float k = .5f; while (!isHit) {
 * 
 * direction.set(pos.x + (testPoint.x - pos.x) * k * 100, pos.y + (testPoint.y -
 * pos.y) * k * 100); System.out.println(" X" + direction.x + " Y" +
 * direction.y);
 * 
 * world.rayCast(rayCallback, pos, direction); k += .5f; } }
 * 
 * if (release == false && hitBody != null && hitBody.getUserData() == "wall") {
 * //world.QueryAABB(callback, testPoint.x - 0.1f, testPoint.y - 0.1f,
 * testPoint.x + 0.1f, testPoint.y + 0.1f); //world.rayCast(rayCallback,
 * rightArm.getPosition(), new Vector2(testPoint.x, testPoint.y)); isHit = true;
 * distance = rightArm.getPosition().dst(osumaList.get(0)); if (distance < 0) {
 * distance = distance * -1; }
 * 
 * Vector2 apu = new Vector2(); apu.set(collision); for (Vector2 o : osumaList)
 * { float nextDistance = rightArm.getPosition().dst(o); if (nextDistance < 0) {
 * nextDistance = nextDistance * -1; } if (distance < nextDistance) { apu =
 * osumaList.get(0); } else { apu = o; } }
 * 
 * MouseJointDef def = new MouseJointDef(); def.bodyA = playerBody; def.bodyB =
 * hitBody; def.collideConnected = true;
 * 
 * def.target.set(apu); DistanceJointDef distanceJointDef = new
 * DistanceJointDef(); distanceJointDef.initialize(rightArm, hitBody,
 * rightArm.getWorldCenter(), apu); distanceJointDef.collideConnected = true;
 * distanceJoint = (DistanceJoint) world.createJoint(distanceJointDef); isHooked
 * = true; release = true; hitBody.setAwake(true); }
 * 
 * else {
 * 
 * isHooked = false; if (distanceJoint != null) { isHit = false;
 * osumaList.clear(); world.destroyJoint(distanceJoint); release = false; }
 * distanceJoint = null; }
 * 
 * return false;
 * 
 * }
 * 
 * return false; }
 */