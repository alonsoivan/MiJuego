package com.ivn.mijuego.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ivn.mijuego.model.Personaje;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ivn.mijuego.screens.ConfigurationScreen.prefs;
import static com.ivn.mijuego.util.Constantes.*;

public class MainScreen implements Screen {
    Stage stage;

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        VisTable table = new VisTable(true);
        table.setFillParent(true);
        stage.addActor(table);

        VisTextButton playButton = new VisTextButton("PLAY");
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                int vida = prefs.getInteger("vida");
                int monedas = prefs.getInteger("monedas");

                String selection = prefs.getString("dificultad");

                switch (selection){
                    case "FACIL":
                        VIDA_PERSONAJE = 10;
                        ENEMIGO2_VIDA = 5;
                        ENEMIGO_VIDA = 2;
                        break;
                    case "NORMAL":
                        VIDA_PERSONAJE = 5;
                        ENEMIGO2_VIDA = 10;
                        ENEMIGO_VIDA = 6;
                        break;
                    case "DIFICIL":
                        VIDA_PERSONAJE = 1;
                        ENEMIGO2_VIDA = 30;
                        ENEMIGO_VIDA = 18;
                        break;
                    default:
                        VIDA_PERSONAJE = 10;
                        ENEMIGO2_VIDA = 5;
                        ENEMIGO_VIDA = 2;
                        break;
                }

                switch (prefs.getString("nivel")) {
                    case "1":
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen1());
                        break;
                    case "2":
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen2(vida, monedas));
                        break;
                    case "3":
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen3(vida, monedas));

                        break;
                    case "4":
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen4(vida, monedas));
                        break;

                    default:
                        ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen1());
                        break;
                }


                dispose();
            }
        });

        VisTextButton configButton = new VisTextButton("CONFIGURATION");
        configButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new ConfigurationScreen());
                dispose();
            }
        });

        VisTextButton quitButton = new VisTextButton("QUIT");
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
                dispose();
                VisUI.dispose();
                // Salir del juego
            }
        });

        VisLabel aboutLabel = new VisLabel("MiJuego libGDX\n(c) IVÁN ALONSO 2020");

        // Añade filas a la tabla y añade los componentes
        table.row();
        table.add(playButton).center().width(600).height(200).pad(5);
        table.row();
        table.add(configButton).center().width(600).height(150).pad(5);
        table.row();
        table.add(quitButton).center().width(600).height(150).pad(5);
        table.row();
        table.add(aboutLabel).left().width(600).height(20).pad(5);

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Pinta la UI en la pantalla
        stage.act(dt);
        stage.draw();
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
