package fi.kyy.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Box2dUtils {

	public static Body createChainShape(World world, BodyType type,
			Vector2[] vertices, float density) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		ChainShape chain = new ChainShape();
		chain.createChain(vertices);
		box.createFixture(chain, density);
		chain.dispose();

		return box;
	}

	public static Body createChainShape(World world, BodyType type,
			Vector2[] vertices, float density, short categoryBits,
			short maskBits) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		ChainShape chain = new ChainShape();
		chain.createChain(vertices);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;
		fdef.shape = chain;

		box.createFixture(fdef);
		chain.dispose();

		return box;
	}

	public static Body createChainShape(World world, BodyType type,
			Vector2[] vertices, float density, float restitution,
			short categoryBits, short maskBits) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		ChainShape chain = new ChainShape();
		chain.createChain(vertices);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		fdef.restitution = restitution;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;
		fdef.shape = chain;

		box.createFixture(fdef);
		chain.dispose();

		return box;
	}

	public static Body createEdgeShape(World world, BodyType type, float x1,
			float y1, float x2, float y2, float density) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		EdgeShape edge = new EdgeShape();
		edge.set(x1, y1, x2, y2);
		box.createFixture(edge, density);
		edge.dispose();

		return box;
	}

	public static Body createEdgeShape(World world, BodyType type, float x1,
			float y1, float x2, float y2, float density, short categoryBits,
			short maskBits) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		EdgeShape edge = new EdgeShape();
		edge.set(x1, y1, x2, y2);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;
		fdef.shape = edge;

		box.createFixture(fdef);
		edge.dispose();

		return box;
	}

	public static Body createBox(World world, BodyType type, float width,
			float height, float density) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);
		box.createFixture(poly, density);
		poly.dispose();

		return box;
	}

	public static Body createBoxPhysics(World world, BodyType type,
			float width, float height, float density, String JSONfileName,
			String imageName, short categoryBits, short maskBits) {

		BodyEditorLoader loader = new BodyEditorLoader(
				Gdx.files.internal(JSONfileName));

		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		// fdef.friction=3f; // friction added by paras
		fdef.shape = poly;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;

		fdef.shape = poly;

		// box.createFixture(fdef);

		loader.attachFixture(box, imageName, fdef, width);
		// box.setTransform((loader.getOrigin(imageName, width).cpy()), 0);
		// box.setTransform(box.getPosition().cpy().sub(loader.getOrigin(imageName,
		// width).cpy()), 0);

		poly.dispose();

		return box;
	}

	public static Body createBoxPhysicsFiltered(World world, BodyType type,
			float width, float height, float density, String JSONfileName,
			String imageName, short categoryBits, short maskBits,
			short groupIndex) {

		BodyEditorLoader loader = new BodyEditorLoader(
				Gdx.files.internal(JSONfileName));

		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		// fdef.friction=3f; // friction added by paras
		fdef.shape = poly;
		fdef.filter.groupIndex = groupIndex;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;

		fdef.shape = poly;

		// box.createFixture(fdef);

		loader.attachFixture(box, imageName, fdef, width);
		// box.setTransform((loader.getOrigin(imageName, width).cpy()), 0);
		// box.setTransform(box.getPosition().cpy().sub(loader.getOrigin(imageName,
		// width).cpy()), 0);

		poly.dispose();

		return box;
	}

	public static Body createBox(World world, BodyType type, float width,
			float height, float density, short categoryBits, short maskBits) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;
		fdef.friction = 3f; // friction added by paras
		fdef.shape = poly;

		box.createFixture(fdef);
		poly.dispose();

		return box;
	}

	public static Body createBoxFiltered(World world, BodyType type,
			float width, float height, float density, short categoryBits,
			short maskBits, short groupIndex) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.setAsBox(width / 2, height / 2);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;
		fdef.filter.groupIndex = groupIndex;
		fdef.friction = 3f; // friction added by paras
		fdef.shape = poly;

		box.createFixture(fdef);
		poly.dispose();

		return box;
	}

	public static Body createPolygon(World world, BodyType type,
			Vector2[] vertices, float density) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		PolygonShape poly = new PolygonShape();
		poly.set(vertices);
		box.createFixture(poly, density);
		poly.dispose();

		return box;
	}

	public static Body createCircle(World world, BodyType type, float radius,
			float density) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		CircleShape poly = new CircleShape();
		poly.setRadius(radius);

		box.createFixture(poly, density);
		poly.dispose();

		return box;
	}

	public static Body createCircle(World world, BodyType type, float radius,
			float density, short categoryBits, short maskBits) {
		BodyDef def = new BodyDef();
		def.type = type;
		Body box = world.createBody(def);

		CircleShape poly = new CircleShape();
		poly.setRadius(radius);

		FixtureDef fdef = new FixtureDef();
		fdef.density = density;
		fdef.filter.categoryBits = categoryBits;
		fdef.filter.maskBits = maskBits;
		fdef.shape = poly;

		box.createFixture(fdef);
		poly.dispose();

		return box;
	}

}
