package com.andall.firehazard.states;

import com.andall.firehazard.DubstepGun;
import com.andall.firehazard.Game;
import com.andall.firehazard.handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static com.andall.firehazard.handlers.B2DVars.PPM;

public class Play extends GameState {
    DubstepGun dubstepGun = new DubstepGun();
    private World world;
    private Box2DDebugRenderer b2dr;
    private OrthographicCamera b2dCam;
    private BitmapFont font = new BitmapFont();

    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        // Create Platform
        BodyDef bdef = new BodyDef();
        bdef.position.set(100 / PPM, 100 / PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bdef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / PPM, 5 / PPM);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = shape;
        body.createFixture(fdef);

        bdef.position.set(100 / PPM, 200 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        shape.setAsBox(5 / PPM, 5 / PPM);
        fdef.shape = shape;
        fdef.restitution = 0.5f;
        body.createFixture(fdef);

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

        dubstepGun.loadGun();
    }

    public void handleInput() {
    }

    public void update(float dt) {

        world.step(dt, 6, 2);

    }

    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, b2dCam.combined);
        sb.setProjectionMatrix(b2dCam.combined);
        sb.begin();
        dubstepGun.update(sb);
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
        sb.end();
    }

    public void dispose() {
    }
}
