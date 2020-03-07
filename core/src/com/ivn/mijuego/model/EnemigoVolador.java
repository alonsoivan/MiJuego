package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class EnemigoVolador {
    public Vector2 position;
    public int velocidad;
    public Rectangle rect;
    public int vida;

    public static Array<BalaOvni> balas = new Array<>();

    public static Animation animation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("enemigo/volador.atlas")).findRegions("volador"));
    public float stateTime;

    public long tiempoJuego;

    public EnemigoVolador(Vector2 position){
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 16, 16);
        this.vida = 2;
    }

    public void pintar(Batch batch){
            stateTime += Gdx.graphics.getDeltaTime();
            batch.draw((TextureRegion) (animation.getKeyFrame(stateTime, true)),position.x, position.y);

        for(BalaOvni balaOvni : balas)
            balaOvni.pintar(batch);
    }

    public void mover(Vector2 personaje){
        Vector2 dir = null;

        if (personaje.x < position.x -1)
            dir = new Vector2(-1, 0).scl(0.9f);

        if (personaje.x > position.x +1)
            dir = new Vector2(1, 0).scl(0.9f);


        if (dir!=null) {
            position.add(dir);
            rect.setPosition(position);
        }else
            if (TimeUtils.millis() - tiempoJuego > 1000) {
                disparar();
                tiempoJuego = TimeUtils.millis();
            }

    }

    public void disparar(){
        EnemigoVolador.balas.add(new BalaOvni(new Vector2(position.x,position.y),new Texture("balas/bala.png")));
    }

    public void quitarVida(){
        vida--;
    }

    public boolean estaMuerto(){
        return vida > 0 ? false : true;
    }
}
