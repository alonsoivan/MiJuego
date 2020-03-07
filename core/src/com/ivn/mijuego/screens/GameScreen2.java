package com.ivn.mijuego.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.ivn.mijuego.model.Coin;
import com.ivn.mijuego.model.EnemigoTerrestre;
import com.ivn.mijuego.model.EnemigoVolador;
import com.ivn.mijuego.model.Personaje;

import static com.ivn.mijuego.util.Constantes.*;
import static com.ivn.mijuego.util.Constantes.TILE_WIDTH;

public class GameScreen2 implements Screen {

    private Personaje personaje;
    private Array<EnemigoVolador> ovnis;
    private Array<Coin> coins;
    private Array<EnemigoTerrestre> enemigosTerrestres;
    private Array<Rectangle> topeEnemigos;

    // SOUNDS
    public static Sound hitSound = Gdx.audio.newSound(Gdx.files.internal("personaje/hit3.mp3"));

    // CURSOR
    private Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("cursor/cursor1.png"));

    // FPS
    long lastTimeCounted;
    private float sinceChange;
    private float frameRate;
    private BitmapFont fps;
    private BitmapFont vidaPantalla;
    private BitmapFont coinsPantalla;

    // TiledMap
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d
    private World world;
    //private Box2DDebugRenderer b2dr;

    public GameScreen2(){
        // FPS
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        fps = new BitmapFont();
        fps.setColor(Color.WHITE);
        fps.getData().setScale(0.5f);

        // Vida
        vidaPantalla = new BitmapFont();
        vidaPantalla.setColor(Color.WHITE);
        vidaPantalla.getData().setScale(0.5f);

        // Coins
        coinsPantalla= new BitmapFont();
        coinsPantalla.setColor(Color.WHITE);
        coinsPantalla.getData().setScale(0.5f);

        world = new World(new Vector2(0,-10f),true);

        camera = new OrthographicCamera();

        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        map = new TmxMapLoader().load("levels/nivel2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        batch = renderer.getBatch();




        coins = new Array<>();
        personaje = new Personaje(new Vector2(40,90), VIDA_PERSONAJE, world);
        ovnis = new Array<>();
        enemigosTerrestres = new Array<>();
        topeEnemigos = new Array<>();
    }

    long tiempo;
    @Override
    public void show() {
        // CURSOR
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));

        tiempo = TimeUtils.millis();
    }

    @Override
    public void render(float dt) {
        actualizar(dt);

        pintar(dt);
    }

    private void handleCamera() {

        if (personaje.b2body.getPosition().x < TILES_IN_CAMERA_WIDTH * TILE_WIDTH / 2)
            camera.position.set(TILES_IN_CAMERA_WIDTH * TILE_WIDTH / 2, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH / 2 , 0);
        else
            camera.position.set(personaje.b2body.getPosition().x, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH / 2 , 0);
/*
        camera.update();
        renderer.setView(camera);
*/

// These values likely need to be scaled according to your world coordinates.
// The left boundary of the map (x)
        int mapLeft = 0;
// The right boundary of the map (x + width)
        float mapRight = TILES_IN_CAMERA_WIDTH * TILE_WIDTH * 5.33f ;
// The camera dimensions, halved
        float cameraHalfWidth = camera.viewportWidth * .5f;

// Move camera after player as normal

        float cameraLeft = camera.position.x - cameraHalfWidth;
        float cameraRight = camera.position.x + cameraHalfWidth;


// Horizontal axis
        if(mapRight < camera.viewportWidth)
        {
            camera.position.x = mapRight / 2;
        }
        else if(cameraLeft <= mapLeft)
        {
            camera.position.x = mapLeft + cameraHalfWidth;
        }
        else if(cameraRight >= mapRight)
        {
            camera.position.x = mapRight - cameraHalfWidth;
        }
        camera.update();
        renderer.setView(camera);

/*
        camera.position.x = personaje.b2body.getPosition().x;

        //update our gamecam with correct coordinates after changes
        camera.update();
        //tell our renderer to draw only what our camera can see in our game world.
        renderer.setView(camera);
        renderer.render();
*/
    }

    private void actualizar(float dt) {

        update();

        world.step(1/60f,6,2);

    }

    public void update() {

        // FPS
        long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
        lastTimeCounted = TimeUtils.millis();

        sinceChange += delta;
        if(sinceChange >= 1000) {
            sinceChange = 0;
            frameRate = Gdx.graphics.getFramesPerSecond();
        }
    }


    private void pintar(float dt) {
        handleCamera();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);


        renderer.render();
        //b2dr.render(world,camera.combined);

        batch.begin();

        fps.draw(batch, (int)frameRate + " FPS", camera.position.x-230, camera.position.y + 110);
        vidaPantalla.draw(batch, "Vida x "+personaje.getVidas() , camera.position.x-230, camera.position.y + 100);
        coinsPantalla.draw(batch, "Coins x "+personaje.coins, camera.position.x -230, camera.position.y + 80);
        //vidaPantalla.draw(batch, "TIEMPO x "+(TimeUtils.millis()-tiempo)/1000 , camera.position.x-230, camera.position.y+100);


        personaje.pintar(batch);

        for(EnemigoVolador enemigoVolador : ovnis)
            enemigoVolador.pintar(batch);


        for(Coin coin : coins)
            coin.pintar(batch);


        for(EnemigoTerrestre terrestre : enemigosTerrestres)
            terrestre.pintar(batch);


        batch.end();
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

    }
}
