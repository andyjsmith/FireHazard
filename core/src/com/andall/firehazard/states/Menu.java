package com.andall.firehazard.states;

import com.andall.firehazard.handlers.GameStateManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Menu extends GameState {

    private BitmapFont font = new BitmapFont();

    public Menu(GameStateManager gsm) {
        super(gsm);
    }

    public void handleInput() {
    }

    public void update(float dt) {
        if (Gdx.input.isButtonPressed(0)) {
            gsm.setState(0);
        }
    }

    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        font.draw(sb, "Game Menu", 200, 200);
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), 20, 20);
        sb.end();
    }

    public void dispose() {
    }
}
