package fi.kyy.Game;

import com.badlogic.gdx.Game;

import fi.kyy.Helpers.Assets;
import fi.kyy.Screens.SplashScreen;
import fi.kyy.Services.ActionResolver;

public class SuperHands extends Game {

	public static ActionResolver actionResolver;
	boolean firstTimeCreate = true;

	public SuperHands(ActionResolver actionResolver) {
		super();
		SuperHands.actionResolver = actionResolver;
	}

	@Override
	public void create() {
		Assets.load();
		setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.dispose();
	}

}
