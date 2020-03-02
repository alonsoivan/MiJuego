package com.ivn.mijuego.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.ivn.mijuego.util.Constantes.PPM;
import static com.ivn.mijuego.util.Constantes.SPEED;

public class Personaje extends Sprite {

    public Vector2 posicion;
    private Texture textura;
    private int vidas;
    public int velocidad;
    public Rectangle rect;
    public boolean isJumping = false;
    public Array<BalaPj> balas;

    public World world;
    public Body b2body;

    public Personaje(Vector2 posicion, Texture textura, int vidas, World world) {
        //super(textura);

        this.balas = new Array<>();

        this.posicion = posicion;
        this.textura = textura;
        this.vidas = vidas;
        this.velocidad = SPEED;
        this.rect = new Rectangle(posicion.x,posicion.y, textura.getWidth(), textura.getHeight());
        this.world = world;


        definePersonaje();
    }

    public void definePersonaje(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(100,100);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fixtureDef = new FixtureDef();

        /*
        CircleShape shape = new CircleShape();
        shape.setRadius(5);

        */

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

        fixtureDef.shape = shape;
        fixtureDef.friction = 0.1f;
        //fixtureDef.density = 0.1f;

        b2body.createFixture(fixtureDef);

        shape.dispose();
    }

    public void pintar(Batch batch){
        batch.draw(textura,b2body.getPosition().x -8,b2body.getPosition().y -14);

        for (BalaPj bala : balas)
            bala.pintar(batch);
    }

    public void mover(Vector2 movimiento){
        posicion.add(movimiento.scl(velocidad));
        rect.setPosition(posicion);
    }

    public void saltar(float dt){
        //mover(new Vector2(0,10));

        //b2body.applyLinearImpulse(new Vector2(0,10*PPM),b2body.getWorldCenter(), true);

        float impulse = b2body.getMass() * 1000;
        b2body.applyLinearImpulse( new Vector2(0,impulse),b2body.getWorldCenter(), true );
    }

    public void moverDerecha(){
        //mover(new Vector2(1,0));

        textura = new Texture("personaje/pj_der.png");
        b2body.applyLinearImpulse(new Vector2(0.1f*PPM,0),b2body.getWorldCenter(), true);
    }

    public void moverIzquierda(){
        //mover(new Vector2(-1,0));

        textura = new Texture("personaje/pj_izq.png");
        b2body.applyLinearImpulse(new Vector2(-0.1f*PPM,0),b2body.getWorldCenter(), true);
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public Texture getTextura() {
        return textura;
    }

    public void setTextura(Texture textura) {
        this.textura = textura;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setRect(Rectangle rect) {
        this.rect = rect;
    }

    public void disparar(Vector3 target){
        balas.add(new BalaPj(new Vector2(b2body.getPosition().x, b2body.getPosition().y),new Texture("balas/bala.png"), target));
    }
}