package com.ivn.mijuego.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ivn.mijuego.Aplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Jumper2DX";
		config.width = 1400;
		config.height = 800;
		config.fullscreen = false;

		new LwjglApplication(new Aplication(), config);
	}
}
