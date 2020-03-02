package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class Bala {

    public Vector2 position;
    public Texture texture;
    public int velocidad;
    public Rectangle rect;


    Vector2 direction;
    public Bala(Vector2 position, Texture texture, Vector3 target){
        this.position = position;
        this.texture = texture;
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());

        getDirectionBala(target);
    }

    public void getDirectionBala(Vector3 target){
        Vector2 cursor = new Vector2(target.x, target.y);

        direction= cursor.sub(position);
        direction.nor();

        direction.scl(5);
    }

    public void pintar(Batch batch){
        batch.draw(texture,position.x,position.y);
    }

    public void mover(){
        position.add(direction);
        rect.setPosition(position);
    }

}
