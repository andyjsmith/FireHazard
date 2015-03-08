package com.andall.firehazard.desktop;

import com.andall.firehazard.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = Game.V_HEIGHT * Game.SCALE;
		config.width = Game.V_WIDTH * Game.SCALE;
		config.title = Game.TITLE;
		new LwjglApplication(new Game(), config);
	}
}
