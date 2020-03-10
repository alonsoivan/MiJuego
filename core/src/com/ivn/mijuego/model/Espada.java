package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.ivn.mijuego.screens.ConfigurationScreen.prefs;
import static com.ivn.mijuego.util.Constantes.VELOCIDAD_ESPADAS;

public class Espada {

    public Vector2 position;
    public int velocidad;
    public Rectangle rect;

    public Array<Texture> texturas = new Array<>();

    public static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("espadas/hit.wav"));

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
        texturas.add( new Texture("espadas/espada (10).png"));
        texturas.add( new Texture("espadas/espada (11).png"));
        texturas.add( new Texture("espadas/espada (12).png"));
        texturas.add( new Texture("espadas/espada (13).png"));
        texturas.add( new Texture("espadas/espada (14).png"));
        texturas.add( new Texture("espadas/espada (15).png"));
        texturas.add( new Texture("espadas/espada (16).png"));
        texturas.add( new Texture("espadas/espada (17).png"));
        texturas.add( new Texture("espadas/espada (18).png"));
        texturas.add( new Texture("espadas/espada (19).png"));
        texturas.add( new Texture("espadas/espada (20).png"));
        texturas.add( new Texture("espadas/espada (21).png"));
        texturas.add( new Texture("espadas/espada (22).png"));
        texturas.add( new Texture("espadas/espada (23).png"));
        texturas.add( new Texture("espadas/espada (24).png"));
        texturas.add( new Texture("espadas/espada (25).png"));
        texturas.add( new Texture("espadas/espada (26).png"));
        texturas.add( new Texture("espadas/espada (27).png"));
        texturas.add( new Texture("espadas/espada (28).png"));
        texturas.add( new Texture("espadas/espada (29).png"));
        texturas.add( new Texture("espadas/espada (30).png"));

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

    public static void playHit(){
        if(prefs.getBoolean("sound"))
            hitSound.play(0.5f);
    }
}
