package com.ivn.mijuego.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.utils.Timer;
import com.ivn.mijuego.model.*;

import static com.ivn.mijuego.util.Constantes.*;

public class GameScreen2 implements Screen {

    private Personaje personaje;
    private Array<EnemigoVolador> enemigosVoladores;
    private Array<Coin> coins;
    private Array<EnemigoTerrestre2> enemigosTerrestres;
    private Array<Rectangle> topeEnemigos;
    private Array<Vector2> vectoresEnemigosVoladores;


    // HUD
    private HUD hud = new HUD();
    private SpriteBatch batch2 = new SpriteBatch();


    // CURSOR
    private Pixmap cursorPixmap = new Pixmap(Gdx.files.internal("cursor/cursor1.png"));

    // TiledMap
    private Batch batch;
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2d
    private World world;
    //private Box2DDebugRenderer b2dr;

    public GameScreen2(Personaje personaje){

        world = new World(new Vector2(0,-10f),true);

        camera = new OrthographicCamera();

        camera.setToOrtho(false, TILES_IN_CAMERA_WIDTH * TILE_WIDTH, TILES_IN_CAMERA_HEIGHT * TILE_WIDTH);
        camera.update();

        map = new TmxMapLoader().load("levels/nivel2.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1);
        batch = renderer.getBatch();


        // Colisiones suelo
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

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

        Vector2 pos = getPrincipio();


        coins = new Array<>();
        this.personaje = new Personaje(new Vector2(pos.x,pos.y), personaje.getVidas(), world);
        this.personaje.setCoins(personaje.getCoins());

        enemigosVoladores = new Array<>();
        enemigosTerrestres = new Array<>();
        topeEnemigos = new Array<>();
        vectoresEnemigosVoladores = new Array<>();
    }

    private Vector2 getPrincipio(){
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("principio");

        RectangleMapObject rectangleObject = (RectangleMapObject) collisionsLayer.getObjects().get(0);;

        return new Vector2(rectangleObject.getRectangle().x, rectangleObject.getRectangle().y);
    }

    long tiempo;
    @Override
    public void show() {
        // CURSOR
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(cursorPixmap, 0, 0));

        tiempo = TimeUtils.millis();

        getTopeEnemigos();
        generarEnemigosTerrestres();
        generarCoins();

        getVectoresEnemigosVoladores();
        generarEnemigosVoladores();



        // DISPSROS ENEMIGOS T2 A REVISAR
        generarDisparoEnemigosT2();

        Timer.schedule(new Timer.Task() {
            public void run() {
                generarDisparoEnemigosT2();
            }
        }, 0.05f);

        Timer.schedule(new Timer.Task() {
            public void run() {
                generarDisparoEnemigosT2();
            }
        }, 0.10f);
    }

    private void getVectoresEnemigosVoladores(){
        MapLayer collisionsLayer = map.getLayers().get("enemigosVoladores");

        for (MapObject object : collisionsLayer.getObjects()) {
            RectangleMapObject rectangleObject = (RectangleMapObject) object;

            Rectangle rect = rectangleObject.getRectangle();

            vectoresEnemigosVoladores.add(new Vector2(rect.x, rect.y));
        }
    }

    private void generarEnemigosVoladores(){
        for(Vector2 pos : vectoresEnemigosVoladores)
            enemigosVoladores.add(new EnemigoVolador(pos.cpy()));

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

    private void generarEnemigosTerrestres(){
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("enemigos");

        for (MapObject object : collisionsLayer.getObjects()) {
            RectangleMapObject rectangleObject = (RectangleMapObject) object;

            Rectangle rect = rectangleObject.getRectangle();

            enemigosTerrestres.add(new EnemigoTerrestre2(new Vector2(rect.x, rect.y)));
        }
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
        float mapRight = TILES_IN_CAMERA_WIDTH * TILE_WIDTH * 3f  ;
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

    private void comprobarColisiones(){

        if(personaje.b2body.getPosition().y < 20) {
            personaje.quitarVida();
            personaje.b2body.setTransform(getPrincipio(), personaje.b2body.getAngle());
        }

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

            for(BalaEnemigoVolador bala : EnemigoVolador.balas) {
                if (bala.rect.overlaps(rect)) {
                    EnemigoVolador.balas.removeValue(bala, true);
                }
            }

            for(EnemigoTerrestre2 enemigoTerrestre2: enemigosTerrestres)
                for(BalaEnemigoT2 balaEnemigoT2 : enemigoTerrestre2.balas) {
                    if (balaEnemigoT2.rect.overlaps(rect)) {
                        enemigoTerrestre2.balas.removeValue(balaEnemigoT2, true);
                    }
                }
        }

        for(EnemigoVolador enemigoVolador : enemigosVoladores)
            for(BalaPj bala : personaje.balas)
                if (bala.rect.overlaps(enemigoVolador.rect)){
                    personaje.balas.removeValue(bala, true);
                    enemigoVolador.quitarVida();
                    if(enemigoVolador.estaMuerto()) {
                        enemigosVoladores.removeValue(enemigoVolador, true);
                        if(enemigosVoladores.size == 0)
                            generarEnemigosVoladores();
                    }
                }

        for(Coin coin: coins)
            if(personaje.rect.overlaps(coin.rect)){
                Coin.playCoinSound();
                coins.removeValue(coin, true);
                personaje.coins++;
            }

        for(BalaEnemigoVolador balaEnemigoVolador :  EnemigoVolador.balas)
            if(balaEnemigoVolador.rect.overlaps(new Rectangle(personaje.b2body.getPosition().x -8 ,personaje.b2body.getPosition().y -14,personaje.getTextura().getRegionWidth(),personaje.getTextura().getRegionHeight()))){
                EnemigoVolador.balas.removeValue(balaEnemigoVolador, true);
                personaje.quitarVida();
            }


        for(EnemigoTerrestre2 enemigoTerrestre: enemigosTerrestres) {
            if (enemigoTerrestre.rect.overlaps(personaje.rect))
                personaje.quitarVida();

            for(BalaEnemigoT2 balaEnemigoT2: enemigoTerrestre.balas)
                if(balaEnemigoT2.rect.overlaps(personaje.rect))
                    personaje.quitarVida();

            for (BalaPj balaPj : personaje.balas)
                if (enemigoTerrestre.rect.overlaps(balaPj.rect)) {
                    personaje.balas.removeValue(balaPj, true);
                    enemigoTerrestre.quitarVida();
                    if (enemigoTerrestre.estaMuerto()) {
                        enemigosTerrestres.removeValue(enemigoTerrestre, true);
                        coins.add(new Coin(new Vector2(enemigoTerrestre.rect.x, enemigoTerrestre.rect.y)));
                    }
                }
        }


        MapLayer finLayer = map.getLayers().get("fin");
        for (MapObject object : finLayer.getObjects())
            if (((RectangleMapObject) object).getRectangle().overlaps(personaje.rect))
                ((Game) Gdx.app.getApplicationListener()).setScreen( new GameScreen3(personaje));
    }

    private void comprobarTeclado(float dt) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.W))
            personaje.saltar(dt);

        personaje.b2body.applyForce(new Vector2( personaje.b2body.getLinearVelocity().x,-600),personaje.b2body.getWorldCenter(),false);

        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE))
            ((Game) Gdx.app.getApplicationListener()).setScreen(new MainScreen());

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            personaje.moverDerecha();

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            personaje.moverIzquierda();

        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
            personaje.disparar(getMousePosInGameWorld());

        if(Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            personaje.disparar(getMousePosInGameWorld());
        }

        if(Gdx.input.isKeyPressed(Input.Keys.R)){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameScreen1());
        }
    }

    private void getTopeEnemigos(){
        // Obtiene todos los objetos de la capa 'colision'
        MapLayer collisionsLayer = map.getLayers().get("topeEnemigos");

        for (MapObject object : collisionsLayer.getObjects())
            topeEnemigos.add(((RectangleMapObject) object).getRectangle());

    }

    private void moverEnemigos(){
        for(EnemigoVolador enemigoVolador : enemigosVoladores)
            enemigoVolador.mover(personaje.b2body.getPosition(), topeEnemigos);

        for(EnemigoTerrestre2 ene : enemigosTerrestres)
            ene.mover(topeEnemigos);
    }


    Vector3 getMousePosInGameWorld() {
        return camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
    }

    private void actualizar(float dt) {
        comprobarTeclado(dt);

        comprobarColisiones();

        moverBalas();

        moverEnemigos();

        world.step(1/60f,6,2);

    }

    public void generarDisparoEnemigosT2(){
        Timer.schedule(new Timer.Task() {
            public void run() {
                for(EnemigoTerrestre2 ene: enemigosTerrestres)
                    ene.disparar(getMousePosInGameWorld());
            }
        }, 3,2);
    }


    private void moverBalas(){
        for(BalaPj bala : personaje.balas)
            bala.mover();

        for(BalaEnemigoVolador balaEnemigoVolador : EnemigoVolador.balas)
            balaEnemigoVolador.mover();

        for(EnemigoTerrestre2 ene : enemigosTerrestres)
            for(BalaEnemigoT2 bala : ene.balas)
                bala.mover();
    }

    private void pintar(float dt) {
        handleCamera();

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        renderer.render();
        //b2dr.render(world,camera.combined);


        batch2.begin();

        hud.pintar(batch2,personaje.getVidas(),personaje.getCoins());

        batch2.end();


        batch.begin();

        personaje.pintar(batch);

        for(EnemigoVolador enemigoVolador : enemigosVoladores)
            enemigoVolador.pintar(batch);

        for(Coin coin : coins)
            coin.pintar(batch);

        for(EnemigoTerrestre2 terrestre : enemigosTerrestres)
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
