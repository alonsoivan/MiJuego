package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class BalaEnemigoT2 {

    public Vector2 position;
    public int velocidad;
    public Rectangle rect;

    public Texture textura = new Texture("balas/balasEnemigoT2.png");

    Vector2 direction;
    boolean dir;
    public BalaEnemigoT2(Vector2 position, boolean dir){
        this.dir = dir;
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 8, 8);

        getDirectionBala();
    }

    public void getDirectionBala(){


        Vector2 cursor;
            if(dir)
                cursor = new Vector2(MathUtils.random(position.x + 600,Gdx.graphics.getWidth()), MathUtils.random(position.y -50,position.y + 100));
            else
                cursor = new Vector2(MathUtils.random(0,position.x - 600), MathUtils.random(position.y - 50, position.y + 100));

        direction= cursor.sub(position);
        direction.nor();

        direction.scl(5);
    }

    public void pintar(Batch batch){
        batch.draw(textura, position.x, position.y);
    }

    public void mover(){
        position.add(direction);
        rect.setPosition(position);
    }

}