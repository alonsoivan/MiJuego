package com.ivn.mijuego.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.ivn.mijuego.util.Constantes.PERSONAJE_SPEED;


public class Personaje extends Sprite {

    private enum Estado{
        IDLE_LEFT, IDLE_RIGHT, RUN_RIGHT, RUN_LEFT
    }

    public Vector2 posicion;
    private TextureRegion textura;
    private int vidas;
    public int velocidad;
    public Rectangle rect;
    public boolean isJumping = false;
    public Array<BalaPj> balas;
    private Estado estado;

    public Texture texturaBala;

    // Box2d
    public World world;
    public Body b2body;


    // TextureAtlas y animaciones
    private Animation idleRightAnimation;
    private Animation idleLeftAnimation;
    private Animation runRightAnimation;
    private Animation runLeftAnimation;
    float stateTime;

    // Sonido disparo
    public static Sound soundDisparo = Gdx.audio.newSound(Gdx.files.internal("personaje/disparo.wav"));

    public Personaje(Vector2 posicion, int vidas, World world) {

        this.balas = new Array<>();

        this.posicion = posicion;
        this.vidas = vidas;
        this.velocidad = PERSONAJE_SPEED;
        this.textura = new TextureAtlas(Gdx.files.internal("personaje/runLeft.atlas")).findRegions("run").get(0);
        this.rect = new Rectangle(posicion.x,posicion.y, textura.getRegionWidth(), textura.getRegionHeight());
        this.world = world;

        this.texturaBala = new Texture("balas/bala.png");

        definePersonaje();

        estado = Estado.RUN_RIGHT;
        cargarAnimaciones();
    }

    public void cargarAnimaciones(){
        idleRightAnimation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("personaje/idleRight.atlas")).findRegions("idle"));
        idleLeftAnimation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("personaje/idleLeft.atlas")).findRegions("idle"));
        runRightAnimation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("personaje/runRight.atlas")).findRegions("run"));
        runLeftAnimation = new Animation(0.15f, new TextureAtlas(Gdx.files.internal("personaje/runLeft.atlas")).findRegions("run"));

    }

    public void definePersonaje(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(90 , 50 );
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

        fdef.shape = shape;

        b2body.createFixture(fdef).setUserData(this);

    }

    public void pintar(Batch batch){
        stateTime += Gdx.graphics.getDeltaTime();

        for (BalaPj bala : balas)
            bala.pintar(batch);

        TextureRegion currentFrame = null;

        if(b2body.isAwake()) {
            switch (estado) {
                case RUN_RIGHT:
                    currentFrame = (TextureRegion) (runRightAnimation.getKeyFrame(stateTime, true));
                    break;
                case RUN_LEFT:
                    currentFrame = (TextureRegion) (runLeftAnimation.getKeyFrame(stateTime, true));
                    break;
            }
        }
        else {
            if (estado == Estado.RUN_RIGHT)
                currentFrame = (TextureRegion) (idleRightAnimation.getKeyFrame(stateTime, true));
            else{
                currentFrame = (TextureRegion) (idleLeftAnimation.getKeyFrame(stateTime, true));
            }
        }

        batch.draw(currentFrame, b2body.getPosition().x -8, b2body.getPosition().y -14);
        rect.setPosition(new Vector2 (b2body.getPosition().x -8, b2body.getPosition().y -15));
    }

    public void saltar(float dt){
        if ( !isJumping ) {
            //b2body.applyLinearImpulse(new Vector2(0, 4*100f), b2body.getWorldCenter(), true);
            b2body.setLinearVelocity(new Vector2(b2body.getLinearVelocity().x, 20600).scl(velocidad));
            isJumping = true;
        }

        //b2body.applyLinearImpulse(new Vector2(0,500),b2body.getWorldCenter(), true);
        //b2body.setLinearVelocity(b2body.getLinearVelocity().x, 1000);
    }

    public void moverDerecha(){

        estado = Estado.RUN_RIGHT;
        //b2body.applyLinearImpulse(new Vector2(100,0),b2body.getWorldCenter(), true);
        //b2body.setLinearVelocity(100,b2body.getLinearVelocity().y);
        b2body.applyForce(new Vector2(200f,b2body.getLinearVelocity().y).scl(velocidad),b2body.getWorldCenter(), true);
    }

    public void moverIzquierda(){

        estado = Estado.RUN_LEFT;
        //b2body.applyLinearImpulse(new Vector2(-100,0),b2body.getWorldCenter(), true);
        //b2body.setLinearVelocity(-100,b2body.getLinearVelocity().y);
        b2body.applyForce(new Vector2(-200f,b2body.getLinearVelocity().y).scl(velocidad),b2body.getWorldCenter(), true);
    }

    public Vector2 getPosicion() {
        return posicion;
    }

    public void setPosicion(Vector2 posicion) {
        this.posicion = posicion;
    }

    public TextureRegion getTextura() {
        return textura;
    }

    public void setTextura(TextureRegion textura) {
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
        soundDisparo.play(0.7f);
        balas.add(new BalaPj(new Vector2(b2body.getPosition().x, b2body.getPosition().y),texturaBala, target));
    }
}