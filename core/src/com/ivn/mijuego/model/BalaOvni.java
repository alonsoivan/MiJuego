package com.ivn.mijuego.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class BalaOvni {
    public Vector2 position;
    public Texture texture;
    public int velocidad;
    public Rectangle rect;


    public BalaOvni(Vector2 position, Texture texture){
        this.position = position;
        this.texture = texture;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void pintar(Batch batch){
        batch.draw(texture,position.x,position.y);
    }

    public void mover(){
        position.add(new Vector2(0,-1));
        rect.setPosition(position);
    }
}
