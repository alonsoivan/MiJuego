package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class BalaPj {

    public Vector2 position;
    public int velocidad;
    public Rectangle rect;

    public static Animation balaPjAnimation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("personaje/balasPj.atlas")).findRegions("bala"));

    private float stateTime;

    Vector2 direction;
    public BalaPj(Vector2 position,  Vector3 target){
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 8, 8);

        getDirectionBala(target);
    }

    public void getDirectionBala(Vector3 target){
        Vector2 cursor = new Vector2(target.x, target.y);

        direction= cursor.sub(position);
        direction.nor();

        direction.scl(5);
    }

    public void pintar(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();

        batch.draw((TextureRegion) (balaPjAnimation.getKeyFrame(stateTime, true)), position.x, position.y);
    }

    public void mover(){
        position.add(direction);
        rect.setPosition(position);
    }

}
