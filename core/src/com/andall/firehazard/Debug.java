package com.andall.firehazard;

import com.andall.firehazard.states.Functions;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

public class Debug {

    private int mb = 1024 * 1024;

    private ShapeRenderer sr = new ShapeRenderer();

    public void drawDebug(SpriteBatch sb, Vector3 worldCoordinates, OrthographicCamera hudCamera, BitmapFont font) {
        sr.setAutoShapeType(true);
        sr.setProjectionMatrix(hudCamera.combined);
        font.draw(sb, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20);
        font.draw(sb, "Platform: " + Functions.getPlatform(), 10, 40);
        font.draw(sb, "Used Memory: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / mb) + "MB (" + (int) (((float) Runtime.getRuntime().totalMemory() - (float) Runtime.getRuntime().freeMemory()) / (float) Runtime.getRuntime().totalMemory() * 100) + "%)", 10, 60);
        font.draw(sb, "Total Memory: " + (Runtime.getRuntime().totalMemory() / mb) + "MB", 10, 80);
        font.draw(sb, "Max Memory: " + (Runtime.getRuntime().maxMemory() / mb) + "MB", 10, 100);
        font.draw(sb, "Available Processors: " + Runtime.getRuntime().availableProcessors(), 10, 120);
        sr.begin();
        sr.setColor(1, 1, 0, 1);
        sr.line(worldCoordinates.x, 0, worldCoordinates.x, Game.V_HEIGHT); // Vertical line
        font.draw(sb, "x: " + Integer.toString((int) worldCoordinates.x), worldCoordinates.x + 10, 20); // X text
        sr.line(0, worldCoordinates.y, Game.V_WIDTH, worldCoordinates.y); // Horizontal line
        font.draw(sb, "y: " + Integer.toString((int) worldCoordinates.y), 10, worldCoordinates.y + 20); // Y text
        sb.end();
        sr.end();
    }

    public void dispose() {
        sr.dispose();
    }
}
