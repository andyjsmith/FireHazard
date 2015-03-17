package com.andall.firehazard.states;

import com.andall.firehazard.Debug;
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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.andall.firehazard.handlers.B2DVars.PPM;

public class Play extends GameState {
    private DubstepGun dubstepGun = new DubstepGun();
    private Debug debug = new Debug();
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
    private Vector3 screenCoordinates;
    private Vector3 worldCoordinates;

    private TiledMap tileMap;
    private int tileSize;
    private OrthogonalTiledMapRenderer tmr;

    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.8f), true);
        b2dr = new Box2DDebugRenderer();

        createTiles();

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
        cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        hudCamera = new OrthographicCamera(Game.V_WIDTH, Game.V_HEIGHT);
        hudCamera.position.set(Game.V_WIDTH / 2, Game.V_HEIGHT / 2, 0);

        screenCoordinates = new Vector3();
        worldCoordinates = new Vector3();


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
            tileCam.zoom -= 0.05;

        }

        if (Gdx.input.isKeyPressed(Input.Keys.G)) {
            b2dCam.zoom += 0.05;
            tileCam.zoom += 0.05;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            b2dCam.translate(0, 0.1f);
            tileCam.translate(0, 0.1f * PPM);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            b2dCam.translate(0, -0.1f);
            tileCam.translate(0, -0.1f * PPM);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            b2dCam.translate(-0.1f, 0);
            tileCam.translate(-0.1f * PPM, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            b2dCam.translate(0.1f, 0);
            tileCam.translate(0.1f * PPM, 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.Z)) {
            b2dCam.rotate(-0.5f, 0, 0, 1);
            tileCam.rotate(-0.5f, 0, 0, 1);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.X)) {
            b2dCam.rotate(0.5f, 0, 0, 1);
            tileCam.rotate(0.5f, 0, 0, 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setDisplayMode(Game.V_WIDTH, Game.V_HEIGHT, false);
            } else {
                Gdx.graphics.setDisplayMode(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            }
        }
    }

    public void update(float dt) {

        handleInput();

        b2dCam.update();
        hudCamera.update();
        cam.update();
        tileCam.update();
        world.step(dt, 6, 2);

    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        b2dr.render(world, b2dCam.combined);
        sb.setProjectionMatrix(b2dCam.combined);

        screenCoordinates.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        worldCoordinates.set(screenCoordinates);
        cam.unproject(worldCoordinates);



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

        // Draw hud
        sb.setProjectionMatrix(hudCamera.combined);

        // Update dubstep gun visuals
        dubstepGun.update(sb, (int) worldCoordinates.x, (int) worldCoordinates.y);

        // Draw debug info, must be last
        debug.drawDebug(sb, worldCoordinates, hudCamera, font);

        sb.setProjectionMatrix(tileCam.combined);
        tmr.setView(tileCam);
        tmr.render();
    }


    private void createTiles() {

        // load tile map
        tileMap = new TmxMapLoader().load("test.tmx");
        tmr = new OrthogonalTiledMapRenderer(tileMap);
        tileSize = 32;

        TiledMapTileLayer layer;

        layer = (TiledMapTileLayer) tileMap.getLayers().get("blocks");
        createLayer(layer);



    }

    private void createLayer(TiledMapTileLayer layer) {

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        fdef.friction = 2f;
        // go through all the cells in the layer
        for (int row = 0; row < layer.getHeight(); row++) {
            for (int col = 0; col < layer.getWidth(); col++) {

                // get cell
                TiledMapTileLayer.Cell cell = layer.getCell(col, row);

                // check if cell exists
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                // create a body + fixture from cell
                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((col + 0.5f) * tileSize / PPM, (row + 0.5f) * tileSize / PPM);

                ChainShape cs = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-tileSize / 2 / PPM, -tileSize / 2 / PPM);
                v[1] = new Vector2(-tileSize / 2 / PPM, tileSize / 2 / PPM);
                v[2] = new Vector2(tileSize / 2 / PPM, tileSize / 2 / PPM);
                cs.createChain(v);
                fdef.friction = 0;
                fdef.shape = cs;

                fdef.isSensor = false;
                world.createBody(bdef).createFixture(fdef);

            }
        }
    }


    public void dispose() {

        sb.dispose();
        font.dispose();
        world.dispose();
        shape.dispose();
        b2dr.dispose();
        debug.dispose();

    }

}