package com.purecringegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.purecringegame.CringeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name","Mikhail");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 500;
		config.height = 800;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new CringeGame(), config);
	}
}
