package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Coin {

    public Vector2 posicion;
    public Rectangle rect;
    public float stateTime;
    public static Animation animationCoin = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("coin/coin.atlas")).findRegions("coin"));

    public static Sound soundCoin = Gdx.audio.newSound(Gdx.files.internal("coin/coin.wav"));

    public Coin(Vector2 posicion) {
        this.posicion = posicion;
        this.rect = new Rectangle(posicion.x, posicion.y, 14 ,14 );
    }

    public void pintar(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();
        batch.draw((TextureRegion) (animationCoin.getKeyFrame(stateTime, true)),posicion.x, posicion.y);
    }
}
