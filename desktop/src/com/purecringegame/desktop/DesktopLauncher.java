package com.purecringegame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.purecringegame.PureCringe;

public class DesktopLauncher {
	public static void main (String[] arg) {
		System.setProperty("user.name","Mikhail");
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 720;
		config.height = 1080;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new PureCringe(), config);
	}
}
