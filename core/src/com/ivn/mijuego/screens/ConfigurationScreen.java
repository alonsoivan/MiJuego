package com.ivn.mijuego.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static com.ivn.mijuego.util.Constantes.APP_NAME;

public class ConfigurationScreen implements Screen {

    public static Preferences prefs = Gdx.app.getPreferences(APP_NAME);

    Stage stage;

    VisLabel lbTitulo;
    VisCheckBox ccbSonido;
    VisCheckBox ccbFps;

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load();

        stage = new Stage();

        lbTitulo = new VisLabel("CONFIGURACIÓN", Color.RED);
        lbTitulo.setFontScale(5);
        lbTitulo.setBounds(100,700,20,20);

        ccbSonido = new VisCheckBox("SONIDO");
        ccbSonido.setBounds(100,600,200,50);

        if(prefs.getBoolean("sound"))
            ccbSonido.setChecked(true);

        ccbSonido.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(ccbSonido.isChecked())
                    prefs.putBoolean("sound",true);
                else
                    prefs.putBoolean("sound",false);
            }
        });


        ccbFps = new VisCheckBox("MOSTRAR FPS");
        ccbFps.setBounds(100,500,200,50);
        if(prefs.getBoolean("fps"))
            ccbFps.setChecked(true);

        ccbFps.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(ccbFps.isChecked())
                    prefs.putBoolean("fps",true);
                else
                    prefs.putBoolean("fps",true);
            }
        });

        VisTextButton btVolver = new VisTextButton("VOLVER");
        btVolver.setBounds(100,400,200,100);
        btVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());
            }
        });

        VisSelectBox<String> cbb = new VisSelectBox<>();


        stage.addActor(lbTitulo);
        stage.addActor(ccbSonido);
        stage.addActor(ccbFps);
        stage.addActor(btVolver);


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
        stage.dispose();
    }
}
