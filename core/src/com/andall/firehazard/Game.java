package com.andall.firehazard;

import com.andall.firehazard.handlers.GameStateManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game implements ApplicationListener {

	public static final String TITLE = "FireHazard";
	public static final int V_WIDTH = 1366;
	public static final int V_HEIGHT = 768;
	public static final int SCALE = 1;

	public static final float STEP = 1 / 60f;
	public float accum;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCam;
	private OrthographicCamera tileCam;

	private GameStateManager gsm;

	@Override
	public void create() {
		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		hudCam = new OrthographicCamera();
		hudCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		tileCam = new OrthographicCamera();
		tileCam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		gsm = new GameStateManager(this);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void resume() {

	}

	@Override
	public void pause() {

	}


	@Override
	public void resize(int w, int h) {

	}


	@Override
	public void render() {


		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
		}
	}


	public SpriteBatch getSpriteBatch() {
		return sb;
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public OrthographicCamera getHUDCamera() {
		return hudCam;
	}

	public OrthographicCamera getTileCam() { return tileCam; }
}
