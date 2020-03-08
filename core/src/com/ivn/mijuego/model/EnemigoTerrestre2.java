package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.ivn.mijuego.screens.ConfigurationScreen.prefs;
import static com.ivn.mijuego.util.Constantes.*;

public class EnemigoTerrestre2 {

    public Vector2 position;
    public float velocidad;
    public Rectangle rect;
    public float stateTime;
    public boolean dir = false;
    private int vida;

    public Array<BalaEnemigoT2> balas;


    public static Sound shotSound = Gdx.audio.newSound(Gdx.files.internal("enemigoT2/disparar.mp3"));
    public static Sound damagedSound = Gdx.audio.newSound(Gdx.files.internal("enemigoT2/damaged.mp3"));


    public static Animation animationEnemigoRight = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("enemigoT2/runRight.atlas")).findRegions("run"));
    public static Animation animationEnemigoLeft = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("enemigoT2/runLeft.atlas")).findRegions("run"));

    public EnemigoTerrestre2(Vector2 position) {
        balas = new Array<>();


        this.velocidad = ENEMIGO_SPEED;
        this.vida = ENEMIGO2_VIDA;
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 32,34);
    }

    public void pintar(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();

        if (dir)
            batch.draw((TextureRegion) (animationEnemigoRight.getKeyFrame(stateTime, true)),position.x, position.y);
        else
            batch.draw((TextureRegion) (animationEnemigoLeft.getKeyFrame(stateTime, true)),position.x, position.y);


        for(BalaEnemigoT2 bala : balas)
            bala.pintar(batch);
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

    public void quitarVida(){
        vida--;
    }

    public boolean estaMuerto(){
        return vida > 0 ? false : true;
    }

    public void disparar(Vector3 target){
        if(prefs.getBoolean("sound"))
            shotSound.play(0.5f);

        int balasADisparar = MathUtils.random(5,8)+1;

        for(int i = 0; i < balasADisparar ; i++)
            balas.add(new BalaEnemigoT2(new Vector2(position.x + rect.getWidth()/2, position.y + 12),dir));

    }
}
