package com.ivn.mijuego.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

import static com.ivn.mijuego.util.Constantes.VELOCIDAD_ESPADAS;

public class Espada {

    public Vector2 position;
    public int velocidad;
    public Rectangle rect;

    public Array<Texture> texturas = new Array<>();
    public Texture texture;

    public Espada(Vector2 position){

        texturas.add( new Texture("espadas/espada (1).png"));
        texturas.add( new Texture("espadas/espada (2).png"));
        texturas.add( new Texture("espadas/espada (3).png"));
        texturas.add( new Texture("espadas/espada (4).png"));
        texturas.add( new Texture("espadas/espada (5).png"));
        texturas.add( new Texture("espadas/espada (6).png"));
        texturas.add( new Texture("espadas/espada (7).png"));
        texturas.add( new Texture("espadas/espada (8).png"));
        texturas.add( new Texture("espadas/espada (9).png"));

        this.position = position;

        this.velocidad = VELOCIDAD_ESPADAS;

        this.texture = texturas.get(MathUtils.random(0,texturas.size-1));
        this.rect = new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void pintar(Batch batch){
        batch.draw(texture, position.x, position.y);
    }

    public void mover(){
        position.add(new Vector2(0,1).scl(velocidad));
        rect.setPosition(position);
    }

}
