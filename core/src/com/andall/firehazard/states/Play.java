package com.andall.firehazard.states;

import com.andall.firehazard.DubstepGun;
import com.andall.firehazard.Game;
import com.andall.firehazard.handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.andall.firehazard.handlers.B2DVars.PPM;

public class Play extends GameState {
    DubstepGun dubstepGun = new DubstepGun();
    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam, hudCamera;
    private BitmapFont font = new BitmapFont();
    private Body body;
    private FixtureDef fdef;
    private BodyDef bdef;
    private PolygonShape shape;
    private boolean jumped = false;
    private ContactListener listener;
    private Sprite playerSprite;
    private Array<Body> tmpBodies = new Array<Body>();

    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        // Create Platform
        bdef = new BodyDef();
        bdef.position.set(100 / PPM, 100 / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        shape = new PolygonShape();
        shape.setAsBox(1000 / PPM, 5 / PPM);

        fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        bdef.position.set(100 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        shape.setAsBox(25 / PPM, 25 / PPM);
        fdef.shape = shape;
        fdef.restitution = 0.15f;
        fdef.friction = 2f;
        body.createFixture(fdef);
        playerSprite = new Sprite(new Texture(Gdx.files.internal("p1_front.png")));
        playerSprite.setSize(50 / PPM, 50 / PPM);
        playerSprite.setOrigin(playerSprite.getWidth() / 2, playerSprite.getHeight() / 2);
        body.setUserData(playerSprite);

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

        hudCamera = new OrthographicCamera(Game.V_WIDTH, Game.V_HEIGHT);
        hudCamera.position.set(Game.V_WIDTH / 2, Game.V_HEIGHT / 2, 0);

        listener = new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                jumped = false;
            }

            @Override
            public void endContact(Contact contact) {
                jumped = true;
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };
        world.setContactListener(listener);

        dubstepGun.loadGun();
    }

    public void handleInput() {

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.applyForce(8, 0, 0, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.applyForce(-8, 0, 0, 0, true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            if (!jumped) {
                body.applyForce(0, 200, 0, 0, true);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            body.setLinearVelocity(0, -0.01f);
            body.setTransform(100 / PPM, 200 / PPM, body.getAngle());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            b2dCam.zoom -= 0.05;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            b2dCam.zoom += 0.05;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            b2dCam.translate(0, 0.1f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            b2dCam.translate(0, -0.1f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            b2dCam.translate(-0.1f, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            b2dCam.translate(0.1f, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            b2dCam.rotate(-0.5f, 0, 0, 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            b2dCam.rotate(0.5f, 0, 0, 1);
        }

    }

    public void update(float dt) {

        handleInput();


        b2dCam.update();
        hudCamera.update();
        world.step(dt, 6, 2);

    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, b2dCam.combined);
        sb.setProjectionMatrix(b2dCam.combined);
        sb.begin();
        world.getBodies(tmpBodies);
        for (Body body : tmpBodies) {
            if (body.getUserData() instanceof Sprite) {
                Sprite sprite = (Sprite) body.getUserData();
                sprite.setSize(50 / PPM, 50 / PPM);
                sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
                sprite.draw(sb);
            }
        }
        sb.end();

        // Draw hud
        sb.setProjectionMatrix(hudCam.combined);
        sb.getProjectionMatrix().set(hudCamera.combined);
        sb.begin();
        dubstepGun.update(sb);
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
        sb.end();
    }

    public void dispose() {

        sb.dispose();
        font.dispose();
        world.dispose();
        shape.dispose();
        b2dr.dispose();

    }

}