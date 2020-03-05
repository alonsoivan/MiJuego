package com.ivn.mijuego.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ivn.mijuego.Aplication;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = "Jumper2DX";
		config.width = 1480;
		config.height = 800;
		config.fullscreen = false;
		//config.foregroundFPS = 60; // <- limit when focused
		//config.backgroundFPS = 60; // <- limit when minimized
		//config.samples =2; // AA for shape renderer.. not textures!

		new LwjglApplication(new Aplication(), config);
	}
}
