package com.ivn.mijuego;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.ivn.mijuego.screens.GameScreen;


public class Aplication extends Game {

	@Override
	public void create () {
		((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}
}
