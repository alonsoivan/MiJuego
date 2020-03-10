package com.ivn.mijuego.util;

import static com.ivn.mijuego.screens.MainScreen.musicaFondo;

public class Constantes {

    public static String APP_NAME = "MiJuego";

    public static float ENEMIGO_SPEED = 1.5F;
    public static int ENEMIGO_VIDA = 6;
    public static int ENEMIGO2_VIDA = 10;
    public static int VIDA_PERSONAJE = 5;
    public static int PERSONAJE_SPEED = 4;
    public static int VELOCIDAD_ESPADAS = 2;

    public static float ENEMIGO_VOLADOR_VELOCIDAD_DISPARO = 2;
    public static float ENEMIGO_VOLADOR_VELOCIDAD = 1.3f;

    public static int TILES_IN_CAMERA_WIDTH = 30;
    public static int TILES_IN_CAMERA_HEIGHT = 15;
    public static int TILE_WIDTH = 16;

    public static void playMusicaFondo(){
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(0.2f);
        musicaFondo.play();
    }

    public static void stopMusicaFondo(){
        musicaFondo.setLooping(false);
        musicaFondo.stop();
    }
}
