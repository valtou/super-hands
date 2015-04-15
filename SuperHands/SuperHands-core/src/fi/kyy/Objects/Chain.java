package fi.kyy.Objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.physics.box2d.joints.RopeJointDef;

import fi.kyy.Game.Mode;
import fi.kyy.Helpers.Assets;
import fi.kyy.Utils.Box2dUtils;

public class Chain {

	public final float linkWidth = .4f;
	public final float linkHeight = 3f;
	private float linkDensity = .5f;

	public boolean isPowerupConnected = false;

	private final float motorSpeed = -0;
	private final float maxTorqueSpeed = 0f;

	private World world;

	int numLinks;

	float numLinksRendered = 0f;

	public List<Body> links;
	private Sprite spriteLink;
	private Sprite spriteLinkFlipped;

	Joint distanceJoint;
	RevoluteJointDef jdef = new RevoluteJointDef();
	RopeJointDef ropeDef = new RopeJointDef();
	DistanceJointDef djdef = new DistanceJointDef();

	public static enum ChainType {
		LINKS, RUBBER
	}

	private boolean isActive;

	public Chain(World world) {
		this.world = world;
		links = new ArrayList<Body>();
		isActive = false;
		spriteLink = Assets.vine[Mode.getInstance().getCurrentRope()];
		spriteLinkFlipped = Assets.vineFlipped[Mode.getInstance()
				.getCurrentRope()];

	}

	public void createChain(Body bodyA, Body bodyB, Vector2 localAnchorA,
			Vector2 localAnchorB) {
		ChainType type = ChainType.LINKS;
		if (type == ChainType.LINKS)
			createLinkedChain(bodyA, bodyB, localAnchorA, localAnchorB);
		else
			createRubberChain(bodyA, bodyB, localAnchorA, localAnchorB);
	}

	public void createChain(Body bodyA, Body bodyB, Vector2 localAnchorA,
			Vector2 localAnchorB, int typeChain) {
		ChainType type = ChainType.RUBBER;
		if (type == ChainType.LINKS)
			createLinkedChain(bodyA, bodyB, localAnchorA, localAnchorB);
		else
			createRubberChain(bodyA, bodyB, localAnchorA, localAnchorB);
	}

	private void createRubberChain(Body bodyA, Body bodyB,
			Vector2 localAnchorA, Vector2 localAnchorB) {

		numLinksRendered = 0f;
		Vector2 handPosition = bodyA.getWorldPoint(localAnchorA);
		Vector2 chainDirection = bodyB.getWorldPoint(localAnchorB).cpy()
				.sub(handPosition);
		float chainAngle = (float) Math.toRadians(chainDirection.angle());

		numLinks = (int) Math.ceil(chainDirection.len() / linkHeight);

		chainDirection.nor();

		Body link1, link2;
		link1 = Box2dUtils.createBox(world, BodyType.DynamicBody, linkWidth,
				linkHeight, linkDensity, (short) 0x0002, (short) 0x0004);
		link2 = Box2dUtils.createBox(world, BodyType.DynamicBody, linkWidth,
				linkHeight, linkDensity, (short) 0x0002, (short) 0x0004);
		link1.setTransform(
				handPosition.cpy().add(chainDirection.cpy().scl(linkHeight)),
				chainAngle);
		link2.setTransform(bodyB.getPosition().x, bodyB.getPosition().y,
				chainAngle);
		link1.setUserData("LINK");
		link2.setUserData("LINK");

		jdef.bodyA = bodyA;
		jdef.bodyB = link1;
		jdef.collideConnected = false;
		jdef.localAnchorA.set(localAnchorA);
		jdef.localAnchorB.set(0, -linkHeight / 2f);
		jdef.enableMotor = true;
		jdef.motorSpeed = motorSpeed;
		jdef.maxMotorTorque = maxTorqueSpeed;
		jdef.enableLimit = false;
		world.createJoint(jdef);
		links.add(link1);

		ropeDef.bodyA = bodyA;
		ropeDef.bodyB = link1;
		ropeDef.localAnchorA.set(localAnchorA);
		ropeDef.localAnchorB.set(0, 0);
		ropeDef.maxLength = linkHeight / 2;
		world.createJoint(ropeDef);

		jdef.bodyA = bodyB;
		jdef.bodyB = link2;
		jdef.collideConnected = false;
		jdef.localAnchorA.set(localAnchorB);
		jdef.localAnchorB.set(0, linkHeight / 2f);
		jdef.enableMotor = true;
		jdef.motorSpeed = motorSpeed;
		jdef.maxMotorTorque = maxTorqueSpeed;
		jdef.enableLimit = false;
		world.createJoint(jdef);
		links.add(link2);

		ropeDef.bodyA = bodyB;
		ropeDef.bodyB = link2;
		ropeDef.localAnchorA.set(localAnchorB);
		ropeDef.localAnchorB.set(0, 0);
		ropeDef.maxLength = linkHeight / 2;
		world.createJoint(ropeDef);

		djdef.bodyA = link1;
		djdef.bodyB = link2;
		djdef.localAnchorA.set(0, 0);
		djdef.localAnchorB.set(0, 0);
		// djdef.dampingRatio = 0f;
		// djdef.frequencyHz = 0f;
		djdef.length = chainDirection.len();

		world.createJoint(djdef);

		ropeDef.bodyA = link1;
		ropeDef.bodyB = link2;
		ropeDef.localAnchorA.set(0, 0);
		ropeDef.localAnchorB.set(0, 0);
		ropeDef.maxLength = chainDirection.len();

		isActive = true;
	}

	private void createLinkedChain(Body bodyA, Body bodyB,
			Vector2 localAnchorA, Vector2 localAnchorB) {

		numLinksRendered = 0f;

		Vector2 handPosition = bodyA.getWorldPoint(localAnchorA);
		Vector2 chainDirection = bodyB.getWorldPoint(localAnchorB).cpy()
				.sub(handPosition);

		float chainAngle = (float) Math.toRadians(chainDirection.angle());

		numLinks = (int) Math.ceil(chainDirection.len() / linkHeight);
		// int numLinks = (int) Math.ceil(Distance/ linkHeight);

		chainDirection.nor();

		Body link1, link2;
		link1 = Box2dUtils.createBox(world, BodyType.DynamicBody, linkWidth,
				linkHeight, linkDensity, (short) 0x0002, (short) 0x0004);

		link1.setUserData("LINK");
		link1.setTransform(
				handPosition.cpy().add(chainDirection.cpy().scl(linkHeight)),
				chainAngle);

		jdef.bodyA = bodyA;
		jdef.bodyB = link1;
		jdef.collideConnected = false;
		jdef.localAnchorA.set(localAnchorA);
		jdef.localAnchorB.set(0, -linkHeight / 2f);

		jdef.enableMotor = true;
		jdef.motorSpeed = motorSpeed;
		jdef.maxMotorTorque = maxTorqueSpeed;

		jdef.enableLimit = false;
		/*
		 * jdef.lowerAngle=-(float)Math.PI/4; jdef.upperAngle=(float)Math.PI/4;
		 */
		world.createJoint(jdef);
		links.add(link1);

		ropeDef.bodyA = bodyA;
		ropeDef.bodyB = link1;
		ropeDef.localAnchorA.set(localAnchorA);
		ropeDef.localAnchorB.set(0, 0);
		ropeDef.maxLength = linkWidth;

		world.createJoint(ropeDef);

		for (int i = 0; i < numLinks - 2; i++) {
			link2 = link1;
			link1 = Box2dUtils.createBox(world, BodyType.DynamicBody,
					linkWidth, linkHeight, linkDensity, (short) 0x0002,
					(short) 0x0004);

			link1.setUserData("LINK");

			link1.setTransform(
					handPosition.cpy().add(
							chainDirection.cpy().scl((i + 1) * linkHeight)),
					chainAngle);
			links.add(link1);
			createLink(link2, link1);
		}

		jdef.bodyA = link1;
		jdef.bodyB = bodyB;
		jdef.collideConnected = false;
		jdef.localAnchorA.set(0, linkHeight / 2f);
		jdef.localAnchorB.set(localAnchorB);
		// jdef.enableLimit = true;
		jdef.enableLimit = false;

		jdef.enableMotor = true;
		jdef.motorSpeed = motorSpeed;
		jdef.maxMotorTorque = maxTorqueSpeed;

		world.createJoint(jdef);

		ropeDef.bodyA = link1;
		ropeDef.bodyB = bodyB;
		jdef.collideConnected = false;
		ropeDef.localAnchorA.set(0, 0);
		ropeDef.localAnchorB.set(localAnchorB);
		ropeDef.maxLength = linkWidth;

		world.createJoint(ropeDef);

		ropeDef.bodyA = bodyA;
		ropeDef.bodyB = links.get(links.size() - 1);
		ropeDef.localAnchorA.set(localAnchorA);
		ropeDef.localAnchorB.set(0, 0);
		ropeDef.maxLength = (numLinks) * linkHeight;

		// / if(isPlaneConnected)
		// world.createJoint(ropeDef);

		isActive = true;

		djdef.bodyA = links.get(0);
		djdef.bodyB = links.get(links.size() - 1);
		djdef.localAnchorA.set(0, 0);
		djdef.localAnchorB.set(0, 0);
		djdef.dampingRatio = 0.001f;
		djdef.length = chainDirection.len();

		distanceJoint = world.createJoint(djdef);
		isDistanceJointActive = true;
	}

	private void createLink(Body body1, Body body2) {

		jdef.bodyA = body1;
		jdef.bodyB = body2;
		jdef.collideConnected = false;
		jdef.localAnchorA.set(0, linkHeight / 2f);
		jdef.localAnchorB.set(0, -linkHeight / 2f);

		jdef.lowerAngle = -(float) Math.PI / 16;
		// jdef.lowerAngle=1f;
		// jdef.upperAngle = 1f;
		jdef.upperAngle = (float) Math.PI / 16;

		world.createJoint(jdef);

		ropeDef.bodyA = body1;
		ropeDef.bodyB = body2;
		// ropeDef.localAnchorA.set(center);
		// ropeDef.localAnchorB.set(center);

		ropeDef.localAnchorA.set(0, linkHeight / 2f);
		ropeDef.localAnchorB.set(0, -linkHeight / 2f);
		// ropeDef.maxLength = linkHeight ;

		ropeDef.maxLength = .01f;

		if (isPowerupConnected)
			world.createJoint(ropeDef);
	}

	public boolean isActive() {
		return isActive;
	}

	public void destroy() {
		// removeLinks();
		numLinksRendered = 0f;
		isPowerupConnected = false;
		links.removeAll(links);
		isActive = false;

	}

	public void destroy(Body ch) {
		world.destroyBody(ch);
	}

	boolean isDistanceJointActive = false;

	public void render(SpriteBatch batch) {
		if (!isPowerupConnected)
			renderChainNormal(batch);
		else
			renderChainDistanceJoint(batch);

	}

	private final Vector2 tempVector = new Vector2();
	private final Vector2 copyVector = new Vector2();
	Vector2 handPosition;
	Vector2 chainDirection;

	private void renderChainDistanceJoint(SpriteBatch batch) {

		if (links.size() > 1) {
			tempVector.set(0, linkHeight / 2);
			handPosition = links.get(0).getWorldPoint(tempVector);
			copyVector.set(links.get(links.size() - 1)
					.getWorldPoint(tempVector));
			chainDirection = copyVector.sub(handPosition);

			float chainAngle = (float) Math.toRadians(chainDirection.angle());

			numLinks = (int) Math.ceil(chainDirection.len() / linkHeight);
			for (int i = 0; i < (int) numLinks; i++) {
				if (i % 2 == 0) {
					spriteLink.setOrigin(spriteLink.getWidth() / 2f,
							spriteLink.getHeight() / 2f);
					spriteLink
							.setRotation((float) (chainDirection.angle()) + 90f);
					spriteLink.setPosition(
							handPosition.x + (float) Math.cos(chainAngle)
									* linkHeight * (i) - spriteLink.getWidth()
									/ 2f,
							handPosition.y + (float) Math.sin(chainAngle)
									* linkHeight * (i) - spriteLink.getHeight()
									/ 2f);
					spriteLink.draw(batch);
				} else {
					spriteLinkFlipped.setOrigin(
							spriteLinkFlipped.getWidth() / 2f,
							spriteLinkFlipped.getHeight() / 2f);
					spriteLinkFlipped.setRotation((float) (chainDirection
							.angle()) + 90f);
					spriteLinkFlipped.setPosition(
							handPosition.x + (float) Math.cos(chainAngle)
									* linkHeight * (i)
									- spriteLinkFlipped.getWidth() / 2f,
							handPosition.y + (float) Math.sin(chainAngle)
									* linkHeight * (i)
									- spriteLinkFlipped.getHeight() / 2f);
					spriteLinkFlipped.draw(batch);
				}

			}
		}
	}

	private void renderChainNormal(SpriteBatch batch) {
		for (int i = 0; i < (int) numLinksRendered; i++) {
			if (i % 2 == 0) {
				spriteLink.setOrigin(spriteLink.getWidth() / 2f,
						spriteLink.getHeight() / 2f);
				spriteLink.setRotation((float) (Math.toDegrees(links.get(i)
						.getAngle())));
				spriteLink.setPosition(links.get(i).getPosition().x
						- spriteLink.getWidth() / 2f, links.get(i)
						.getPosition().y - spriteLink.getHeight() / 2f);
				spriteLink.draw(batch);
			} else {
				spriteLinkFlipped.setOrigin(spriteLinkFlipped.getWidth() / 2f,
						spriteLinkFlipped.getHeight() / 2f);
				spriteLinkFlipped.setRotation((float) (Math.toDegrees(links
						.get(i).getAngle())));
				spriteLinkFlipped.setPosition(links.get(i).getPosition().x
						- spriteLinkFlipped.getWidth() / 2f, links.get(i)
						.getPosition().y - spriteLinkFlipped.getHeight() / 2f);
				spriteLinkFlipped.draw(batch);
			}
		}
		if (isActive)
			if (numLinksRendered < links.size() - 2) {
				if (links.size() > 2)
					if (numLinksRendered > links.size() - 1)
						numLinksRendered = links.size();
					else
						numLinksRendered += 2;
			} else {
				if (isDistanceJointActive) {
					if (!isPowerupConnected)
						world.destroyJoint(distanceJoint);
					isDistanceJointActive = false;
				}

				numLinksRendered = links.size();
			}
	}

}
