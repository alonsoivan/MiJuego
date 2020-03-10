package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import static com.ivn.mijuego.model.Personaje.hitSound2;
import static com.ivn.mijuego.screens.ConfigurationScreen.prefs;
import static com.ivn.mijuego.util.Constantes.ENEMIGO_VIDA;
import static com.ivn.mijuego.util.Constantes.ENEMIGO_VOLADOR_VELOCIDAD;

public class EnemigoVolador {
    public Vector2 position;
    public float velocidad;
    public Rectangle rect;
    public int vida;

    public static Array<BalaEnemigoVolador> balas = new Array<>();
    public static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("ovni/hit.mp3"));

    public static Animation animation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("enemigo/volador.atlas")).findRegions("volador"));
    public float stateTime;

    public long tiempoJuego;

    public EnemigoVolador(Vector2 position) {
        this.position = position;
        this.rect = new Rectangle(position.x, position.y, 16, 16);
        this.vida = ENEMIGO_VIDA;
        this.velocidad = ENEMIGO_VOLADOR_VELOCIDAD;
    }

    public void pintar(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        batch.draw((TextureRegion) (animation.getKeyFrame(stateTime, true)), position.x, position.y);

        for (BalaEnemigoVolador balaEnemigoVolador : balas)
            balaEnemigoVolador.pintar(batch);
    }

    public void mover(Vector2 personaje, Array<Rectangle> topeEnemigos) {
        Vector2 dir = null;

        if (personaje.x < position.x - 1)
            dir = new Vector2(-1, 0).scl(0.9f);

        if (personaje.x > position.x + 1)
            dir = new Vector2(1, 0).scl(0.9f);


        if (dir != null) {
            for (Rectangle r : topeEnemigos)
                if (r.overlaps(rect) && personaje.x > position.x + 1)
                    return;
            position.add(dir.scl(velocidad));
            rect.setPosition(position);
        } else if (TimeUtils.millis() - tiempoJuego > 1000) {
            disparar();
            tiempoJuego = TimeUtils.millis();
        }

    }

    public void disparar() {
        EnemigoVolador.balas.add(new BalaEnemigoVolador(new Vector2(position.x, position.y)));
    }

    public void quitarVida() {

        if (prefs.getBoolean("sound"))
            hitSound2.play(0.7f);

        vida--;
    }

    public static void playHitSound() {
        if (prefs.getBoolean("sound"))
            hitSound.play(0.25f);
    }

    public boolean estaMuerto() {
        return vida > 0 ? false : true;
    }
}