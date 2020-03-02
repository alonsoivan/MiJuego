package com.ivn.mijuego.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Ovni {
    public Vector2 position;
    public Texture texture;
    public int velocidad;
    public Rectangle rect;

    public static Array<BalaOvni> balas = new Array<>();

    public long tiempoJuego;

    public Ovni(Vector2 position, Texture texture){
        this.position = position;
        this.texture = texture;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());

    }

    public void pintar(Batch batch){
        batch.draw(texture,position.x,position.y);

        for(BalaOvni balaOvni : balas)
            balaOvni.pintar(batch);
    }

    public void mover(Vector2 personaje){
        Vector2 dir = null;

        if (personaje.x < position.x -1)
            dir = new Vector2(-1, 0).scl(0.7f);

        if (personaje.x > position.x +1)
            dir = new Vector2(1, 0).scl(0.7f);


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
        Ovni.balas.add(new BalaOvni(new Vector2(position.x,position.y),new Texture("balas/bala.png")));
    }
}
