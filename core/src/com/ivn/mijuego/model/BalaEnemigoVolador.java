package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import static com.ivn.mijuego.util.Constantes.ENEMIGO_VOLADOR_VELOCIDAD_DISPARO;

public class BalaEnemigoVolador {
    public Vector2 position;
    public float velocidad;
    public Rectangle rect;
    private float stateTime;
    public static Animation animationBala = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("balas/bala.atlas")).findRegions("bala"));

    public BalaEnemigoVolador(Vector2 position){
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 9, 24);
        this.velocidad = ENEMIGO_VOLADOR_VELOCIDAD_DISPARO;
    }

    public void pintar(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw((TextureRegion) (animationBala.getKeyFrame(stateTime, true)),position.x, position.y);
    }

    public void mover(){
        position.add(new Vector2(0,-1).scl(velocidad));
        rect.setPosition(position);
    }
}
