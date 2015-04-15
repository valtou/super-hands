package fi.kyy.TweenAccessors;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorAccessor implements TweenAccessor<Actor> {

	public static final int ALPHA = 2, RGB = 1, POSITION = 3;
	public static final int Y = 0;

	@Override
	public int getValues(Actor target, int tweenType, float[] returnValues) {
		switch (tweenType) {
		case Y:
			returnValues[0] = target.getY();
			return 1;
		case RGB:
			returnValues[0] = target.getColor().r;
			returnValues[0] = target.getColor().g;
			returnValues[0] = target.getColor().b;
			return 3;
		case ALPHA:
			returnValues[0] = target.getColor().a;
			return 1;
		case POSITION:
			returnValues[0] = target.getX();
			returnValues[1] = target.getY();
			return 2;
		default:
			assert false;
			return -1;
		}
	}

	@Override
	public void setValues(Actor target, int tweenType, float[] newValues) {
		switch (tweenType) {
		case Y:
			target.setY(newValues[0]);
			break;
		case RGB:
			target.setColor(newValues[0], newValues[1], newValues[2],
					target.getColor().a);
			break;
		case ALPHA:
			target.setColor(target.getColor().r, target.getColor().g,
					target.getColor().b, newValues[0]);
			break;
		case POSITION:
			target.setPosition(newValues[0], newValues[1]);
			break;
		default:
			assert false;
		}

	}

}
