package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.TimeUtils;

import static com.ivn.mijuego.screens.ConfigurationScreen.prefs;

public class HUD {
    public Texture textureVida = new Texture("HUD/heart.png");
    public Texture textureCoins = new Texture("HUD/coin.png");

    private BitmapFont fpsPantalla;
    private BitmapFont vidaPantalla;
    private BitmapFont coinsPantalla;

    // FPS
    long lastTimeCounted = 0;
    private float sinceChange = 0;
    private float frameRate = 0;

    private FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/LemonMilk.otf"));
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

    public HUD(){
        parameter.size = 30;
        parameter.color = Color.WHITE;

        // VIDA
        vidaPantalla = generator.generateFont(parameter);
        vidaPantalla.setUseIntegerPositions(false);

        // COINS
        coinsPantalla = generator.generateFont(parameter);
        coinsPantalla.setUseIntegerPositions(false);

        // FPS
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();

        fpsPantalla = generator.generateFont(parameter);
        fpsPantalla.setUseIntegerPositions(false);
    }

    public void pintar(Batch batch, int vida, int coins){
        update();

        // VIDA
        batch.draw(textureVida, 20,  Gdx.graphics.getHeight() - 75);
        vidaPantalla.draw(batch, "x " + vida, 105,  Gdx.graphics.getHeight() - 30);

        // COINS
        batch.draw(textureCoins, 20, Gdx.graphics.getHeight() - 150);
        coinsPantalla.draw(batch, "X " + coins, 105,  Gdx.graphics.getHeight() - 105);

        // FPS
        if(prefs.getBoolean("fps"))
            fpsPantalla.draw(batch, (int)frameRate + " FPS",  Gdx.graphics.getWidth() - 105, 785);


        // TIEMPO
        //vidaPantalla.draw(batch, "TIEMPO x "+(TimeUtils.millis()-tiempo)/1000 , camera.position.x-230, camera.position.y+90);


    }

    public void update() {

        // FPS
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 1000) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
    }
}
