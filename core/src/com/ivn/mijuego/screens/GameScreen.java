package com.ivn.mijuego.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.ivn.mijuego.model.*;

import static com.ivn.mijuego.util.Constantes.*;

public class GameScreen implements Screen {

    private Personaje personaje;
    private Array<Ovni> ovnis;
    private Array<Coin> coins;
    private Array<EnemigoTerrestre> enemigosTerrestres;
    private Array<Rectangle> topeEnemigos;

    // CURSOR
    Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("cursor/cursor1.png"));

    // FPS
    long lastTimeCounted;
    private float sinceChange;
    private float frameRate;
    private BitmapFont font;

    // TiledMap
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d
    private World world;
    //private Box2DDebugRenderer b2dr;

    public GameScreen(){

        // FPS
        lastTimeCounted = TimeUtils.millis();
        sinceChange = 0;
        frameRate = Gdx.graphics.getFramesPerSecond();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(0.5f);


        world = new World(new Vector2(0,-10f),true);

        camera = new OrthographicCamera();

        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        map = new TmxMapLoader().load("levels/mimapa.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        batch = renderer.getBatch();

        // create renderer with default values
        //b2dr = new Box2DDebugRenderer();

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
            fdef.friction = 0.5f;


            body.createFixture(fdef);

            shape.dispose();
        }

        coins = new Array<>();
        personaje = new Personaje(new Vector2(40,90), 5, world);
        ovnis = new Array<>();
        enemigosTerrestres = new Array<>();
        topeEnemigos = new Array<>();

        generarCoins();
        generarEnemigosTerrestres();
    }

    @Override
    public void show() {
        // CURSOR
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));

        getTopeEnemigos();
    }

    @Override
    public void render(float dt) {
        actualizar(dt);

        pintar(dt);
    }

    private void getTopeEnemigos(){
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("topeEnemigos");

        for (MapObject object : collisionsLayer.getObjects())
            topeEnemigos.add(((RectangleMapObject) object).getRectangle());

    }

    private void pintar(float dt) {
        handleCamera();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);


        renderer.render();
        //b2dr.render(world,camera.combined);

        batch.begin();

        font.draw(batch, (int)frameRate + " FPS", camera.position.x-230, camera.position.y+110);

        personaje.pintar(batch);

        for(Ovni ovni : ovnis)
            ovni.pintar(batch);


        for(Coin coin : coins)
            coin.pintar(batch);


        for(EnemigoTerrestre terrestre : enemigosTerrestres)
            terrestre.pintar(batch);

        batch.end();


    }

    private void handleCamera() {

        if (personaje.b2body.getPosition().x < TILES_IN_CAMERA_WIDTH * TILE_WIDTH / 2)
            camera.position.set(TILES_IN_CAMERA_WIDTH * TILE_WIDTH / 2, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH / 2 , 0);
        else
            camera.position.set(personaje.b2body.getPosition().x, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH / 2 , 0);

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
        comprobarTeclado(dt);

        //generarEnemigos();

        moverEnemigos();

        comprobarColisiones();

        moverBalas();

        update();

        world.step(1/60f,6,2);

    }

    private void generarEnemigosVoladores(){

        /*
            Timer.schedule(new Timer.Task() {
                public void run(){
                    rocas.add(new Roca(new Vector2(Gdx.graphics.getWidth() - 20, MathUtils.random(0, Gdx.graphics.getHeight())), new Texture("enemy/stone1.png"), 3, VELOCIDAD_ROCAS));
                }
            }, 1, 1);

         */
        ovnis.add(new Ovni(new Vector2(-50,210), new Texture("ovni/ovni.png")));
    }

    private void generarEnemigosTerrestres(){
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("enemigos");

        for (MapObject object : collisionsLayer.getObjects()) {
            RectangleMapObject rectangleObject = (RectangleMapObject) object;

            Rectangle rect = rectangleObject.getRectangle();

            enemigosTerrestres.add(new EnemigoTerrestre(new Vector2(rect.x, rect.y)));
        }
    }


    private void moverBalas(){
        for(BalaPj bala : personaje.balas)
            bala.mover();

        for(Ovni ovni : ovnis)
            for(BalaOvni balaOvni : ovni.balas)
                balaOvni.mover();
    }

    private void moverEnemigos(){
        for(Ovni ovni : ovnis)
            ovni.mover(personaje.b2body.getPosition());

        for(EnemigoTerrestre ene : enemigosTerrestres)
            ene.mover(topeEnemigos);
    }


    private void generarCoins(){
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("coins");

        for (MapObject object : collisionsLayer.getObjects()) {
            //RectangleMapObject rectangleObject = (RectangleMapObject) object;

            EllipseMapObject circleMapObject = (EllipseMapObject) object;

            // Caso 3: Obtiene el rectangulo ocupado por el objeto
            //Rectangle rect = circleMapObject.getRectangle();

            coins.add(new Coin(new Vector2(circleMapObject.getEllipse().x, circleMapObject.getEllipse().y)));
        }
    }

    private void comprobarColisiones(){

        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("colisiones");

        for (MapObject object : collisionsLayer.getObjects()) {
            RectangleMapObject rectangleObject = (RectangleMapObject) object;

            // Caso 3: Obtiene el rectangulo ocupado por el objeto
            Rectangle rect = rectangleObject.getRectangle();

            if(rect.overlaps(personaje.rect))
                personaje.isJumping = false;

            for(BalaPj bala : personaje.balas)
                if (bala.rect.overlaps(rect)){
                    personaje.balas.removeValue(bala, true);
                }

            for(BalaOvni bala : Ovni.balas) {
                if (bala.rect.overlaps(rect)) {
                    Ovni.balas.removeValue(bala, true);
                }
            }
        }

        for(Ovni ovni : ovnis)
            for(BalaPj bala : personaje.balas)
                if (bala.rect.overlaps(ovni.rect)){
                    ovnis.removeValue(ovni, true);
                    personaje.balas.removeValue(bala, true);
                    generarEnemigosVoladores();
                }

        for(Coin coin: coins)
            if(personaje.rect.overlaps(coin.rect)){
                Coin.soundCoin.play(0.7f);
                coins.removeValue(coin, true);
            }

        for(BalaOvni  balaOvni :  Ovni.balas)
            if(balaOvni.rect.overlaps(new Rectangle(personaje.b2body.getPosition().x -8 ,personaje.b2body.getPosition().y -14,personaje.getTextura().getRegionWidth(),personaje.getTextura().getRegionHeight())))
                Ovni.balas.removeValue(balaOvni, true);

    }

    private void comprobarTeclado(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            personaje.saltar(dt);

            personaje.b2body.applyForce(new Vector2( personaje.b2body.getLinearVelocity().x,-600),personaje.b2body.getWorldCenter(),false);

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            personaje.moverDerecha();

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            personaje.moverIzquierda();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            //personaje.disparar(getMousePosInGameWorld());
            generarEnemigosVoladores();

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            personaje.disparar(getMousePosInGameWorld());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
        }

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

        personaje.texturaBala.dispose();

        font.dispose();

        for(Ovni ovni : ovnis)
            ovni.texture.dispose();

    }
}