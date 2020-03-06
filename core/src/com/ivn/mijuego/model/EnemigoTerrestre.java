package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.ivn.mijuego.util.Constantes.ENEMIGO_SPEED;
import static com.ivn.mijuego.util.Constantes.ENEMIGO_VIDA;

public class EnemigoTerrestre {

    public Vector2 position;
    public float velocidad;
    public Rectangle rect;
    public float stateTime;
    public boolean dir = false;
    private int vida;

    public static Animation animationEnemigoRight = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("enemigo/enemigoRight.atlas")).findRegions("enemigo"));
    public static Animation animationEnemigoLeft = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("enemigo/enemigoLeft.atlas")).findRegions("enemigo"));

    public EnemigoTerrestre(Vector2 position) {
        this.velocidad = ENEMIGO_SPEED;
        this.vida = ENEMIGO_VIDA;
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 16,16);
        this.vida = 3;
    }

    public void pintar(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();

        if (dir)
            batch.draw((TextureRegion) (animationEnemigoRight.getKeyFrame(stateTime, true)),position.x, position.y);
        else
            batch.draw((TextureRegion) (animationEnemigoLeft.getKeyFrame(stateTime, true)),position.x, position.y);
    }

    public void mover(Array<Rectangle> topeEnemigos){

        for(Rectangle r : topeEnemigos)
            if(r.overlaps(rect))
                dir = !dir;


        if (dir)
            position.add(new Vector2(1,0).scl(velocidad));
        else
            position.add(new Vector2(-1,0).scl(velocidad));

        rect.setPosition(position);
    }
}
