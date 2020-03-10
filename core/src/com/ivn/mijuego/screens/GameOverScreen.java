package com.ivn.mijuego.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class GameOverScreen implements Screen {

    private Stage stage;

    private Texture texture;

    private SpriteBatch batch = new SpriteBatch();

    public GameOverScreen(boolean win){
        if(win)
            texture = new Texture("espadas/game_won.png");
        else
            texture = new Texture("espadas/game_over.png");

    }

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();


        VisTextButton volver_a_intentarlo = new VisTextButton("VOLVER A INTENTARLO");
        volver_a_intentarlo.setBounds(Gdx.graphics.getWidth()/2 - texture.getWidth()/2  +10,100,200,50);
        volver_a_intentarlo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());
                dispose();
            }
        });

        VisTextButton btSalit = new VisTextButton("SALIR");
        btSalit.setBounds(volver_a_intentarlo.getX() + volver_a_intentarlo.getWidth() + 120,100,200,50);
        btSalit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
                dispose();
                VisUI.dispose();
            }
        });

        VisLabel aboutLabel = new VisLabel("MiJuego libGDX\n(c) IVÁN ALONSO 2020");

        stage.addActor(btSalit);
        stage.addActor(volver_a_intentarlo);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();

        batch.begin();

        batch.draw(texture,Gdx.graphics.getWidth()/2 - texture.getWidth()/2,Gdx.graphics.getHeight()/2 - texture.getHeight()/2);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        // Redimensiona la escena al redimensionar la ventana del juego
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Libera los recursos de la escena
        stage.dispose();
    }
}