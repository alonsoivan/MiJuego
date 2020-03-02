package com.ivn.mijuego.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ivn.mijuego.model.Bala;
import com.ivn.mijuego.model.Personaje;

import static com.ivn.mijuego.util.Constantes.*;

public class GameScreen implements Screen {

    private Personaje personaje;

    // TiledMap
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    public static final float TILE_SIZE = 16;

    // Box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    private TextureAtlas atlas ;

    public GameScreen(){
       // atlas = new TextureAtlas("personaje/jump.atlas");
        //Texture t = atlas.findRegion("adventurer-run-00").getTexture();

        world = new World(new Vector2(0,-10 * PPM),true);

        camera = new OrthographicCamera();

        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        map = new TmxMapLoader().load("levels/mimapa.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        batch = renderer.getBatch();

        // create renderer with default values
        b2dr = new Box2DDebugRenderer(
                /*drawBodies*/         false,
                /*drawJoints*/         false,
                /*drawAABBs*/          false,
                /*drawInactiveBodies*/ false,
                /*drawVelocities*/     false,
                /*drawContacts*/       false);


        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {


            Rectangle rect = ((RectangleMapObject)object).getRectangle();


            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            body = world.createBody(bdef);

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);

            fdef.shape = shape;
            fdef.friction = 1f;

            body.createFixture(fdef);

            shape.dispose();
        }

        personaje = new Personaje(new Vector2(40,90), new Texture("personaje/pj_der.png"), 5, world);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float dt) {
        actualizar(dt);
        pintar();
    }

    private void pintar() {
        handleCamera();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        renderer.render();

        //b2dr.render(world,camera.combined);

        batch.begin();
        personaje.pintar(batch);
        batch.end();

    }

    private void handleCamera() {
/*
        if (personaje.b2body.getPosition().x < TILES_IN_CAMERA_WIDTH * TILE_WIDTH / 2)
            camera.position.set(TILES_IN_CAMERA_WIDTH * TILE_WIDTH / 2, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH / 2 , 0);
        else
            camera.position.set(personaje.b2body.getPosition().x, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH / 2 , 0);

        camera.update();
        renderer.setView(camera);

*/


        camera.position.x = personaje.b2body.getPosition().x;

        //update our gamecam with correct coordinates after changes
        camera.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(camera);
        renderer.render();

    }

    private void actualizar(float dt) {
        comprobarTeclado(dt);

        //generarEnemigos();

        //generarRocas();

        //moverEnemigos();

        //moverRocas();

        comprobarColisiones();

        moverBalas();

        world.step(1/60f,6,2);
    }

    private void moverBalas(){
        for(Bala bala : personaje.balas)
            bala.mover();
    }

    private void comprobarColisiones(){

        /*
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("colisiones");

        for (MapObject object : collisionsLayer.getObjects()) {
            RectangleMapObject rectangleObject = (RectangleMapObject) object;


            // Caso 3: Obtiene el rectangulo ocupado por el objeto
            Rectangle rect = rectangleObject.getRectangle();
            if (personaje.rect.overlaps(rect)) {
                System.out.println("CHOCA");
            }
        }

        */
    }

    private void comprobarTeclado(float dt) {


        /*
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            personaje.moverIzquierda();

         if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            personaje.moverAbajo();

         if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            personaje.moverDerecha();
         */

        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            personaje.saltar(dt);

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            personaje.moverDerecha();

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            personaje.moverIzquierda();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            personaje.disparar(getMousePosInGameWorld());

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            personaje.disparar(getMousePosInGameWorld());
        }


    }
    Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        renderer.dispose();
        personaje.getTextura().dispose();
    }
}