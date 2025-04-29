package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    private static boolean showMainMenu = true;
    private static TERenderer ter;
    private static String currentSeed = "";
    private static World newWorld;
    private static boolean isSpanish = false;

    public static void main(String[] args) {
        ter = new TERenderer();
        ter.initialize(World.WIDTH, World.LENGTH);
        while (true) {
            if (showMainMenu) {
                showMainMenu();
                handleMain();
            } else {
                gameLoop();
            }
        }
    }
    private static void renderWorldWithFOV(World world) {
        TETile[][] renderWorld = new TETile[World.WIDTH][World.LENGTH];
        for (int x = 0; x < World.WIDTH; x++) {
            for (int y = 0; y < World.LENGTH; y++) {
                renderWorld[x][y] = world.getTileForRendering(x, y);
            }
        }
        ter.renderFrame(renderWorld);
    }
    private static void showMainMenu() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.setFont(new java.awt.Font("Monaco", java.awt.Font.BOLD, 30));
        StdDraw.text(20, 30, isSpanish ? "CS61B:CONSTRUYE TU PROPIO MUNDO" : "CS61B:BYOW");
        StdDraw.setFont(new java.awt.Font("Monaco", java.awt.Font.BOLD, 20));
        if (isSpanish) {
            StdDraw.text(20, 26, "(N) Nuevo Juego");
            StdDraw.text(20, 22, "(C) Cargar Juego");
            StdDraw.text(20, 18, "(S) Salir");
            StdDraw.text(20, 14, "(S) Cambiar a Inglés");
            StdDraw.text(20, 10, "(F) Mostrar Parcialmente");


        } else {
            StdDraw.text(20, 26, "(N) New Game");
            StdDraw.text(20, 22, "(L) Load Game");
            StdDraw.text(20, 18, "(Q) Quit");
            StdDraw.text(20, 14, "(S) Change to Spanish");
            StdDraw.text(20, 10, "(F) Show Partially");
        }
        StdDraw.show();
    }

    public static void handleMain() {
        if (StdDraw.hasNextKeyTyped()) {
            char key = Character.toLowerCase(StdDraw.nextKeyTyped());
            if (key == 's') {
                if (isSpanish) {
                    isSpanish = false;
                } else {
                    isSpanish = true;
                }
                return;
            }
            if (isSpanish) {
                if (key == 'n') {
                    showMainMenu = false;
                    enterSeed();
                } else if (key == 'c') {
                    try {
                        Scanner reader = new Scanner(new File("save.txt"));
                        long savedSeed = Long.parseLong(reader.nextLine());
                        int avatarX = Integer.parseInt(reader.nextLine());
                        int avatarY = Integer.parseInt(reader.nextLine());
                        newWorld = new World(savedSeed);
                        newWorld.produceWholeWorld();
                        newWorld.setAvatarPosition(avatarX, avatarY);
                        ter.renderFrame(newWorld.world);
                        showMainMenu = false;
                        reader.close();
                    } catch (IOException e) {
                        System.out.println("ERROR AL CARGAR");
                    }
                } else if (key == 's') {
                    System.exit(0);
                }
            } else {
                if (key == 'n') {
                    showMainMenu = false;
                    enterSeed();
                } else if (key == 'l') {
                    try {
                        Scanner reader = new Scanner(new File("save.txt"));
                        long savedSeed = Long.parseLong(reader.nextLine());
                        int avatarX = Integer.parseInt(reader.nextLine());
                        int avatarY = Integer.parseInt(reader.nextLine());
                        newWorld = new World(savedSeed);
                        newWorld.produceWholeWorld();
                        newWorld.setAvatarPosition(avatarX, avatarY);
                        ter.renderFrame(newWorld.world);
                        showMainMenu = false;
                        reader.close();
                    } catch (IOException e) {
                        System.out.println("WRONG HAHA");
                    }
                } else if (key == 'q') {
                    System.exit(0);
                }
            }
        }
    }
    public static void enterSeed() {
        currentSeed = "";
        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            StdDraw.setPenColor(StdDraw.WHITE);
            StdDraw.setFont(new java.awt.Font("Monaco", java.awt.Font.BOLD, 30));
            StdDraw.text(20, 30, isSpanish ? "CS61B:CONSTRUYE TU PROPIO MUNDO" : "CS61B:BYOW");
            StdDraw.setFont(new java.awt.Font("Monaco", java.awt.Font.BOLD, 20));
            StdDraw.text(20, 22, isSpanish ? "Ingrese Semilla:" : "Enter Seed:");
            StdDraw.text(20, 18, currentSeed);
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if (Character.isDigit(key)) {
                    currentSeed += key;
                } else if (Character.toLowerCase(key) == 's') {
                    long seed = Long.parseLong(currentSeed);
                    newWorld = new World(seed);
                    newWorld.produceWholeWorld();
                    newWorld.produceAvatar();
                    ter.renderFrame(newWorld.world);
                    return;
                }
            }
        }
    }

    public static void gameLoop() {
        while (true) {
            renderWorldWithFOV(newWorld);
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if ((key == 'w') || (key == 'a') || (key == 's') || (key == 'd') ||
                        (key == 'W') || (key == 'A') || (key == 'S') || (key == 'D') ||
                        (key == 'f') || (key == 'F')) {
                    newWorld.moveAvatar(key);
                }
                if (key == ':') {
                    while (!StdDraw.hasNextKeyTyped()) {
                    }
                    key = StdDraw.nextKeyTyped();
                    if (key == 'q' || key == 'Q') {
                        try {
                            PrintWriter writer = new PrintWriter("save.txt");
                            writer.println(newWorld.getSeed());
                            writer.println(newWorld.getAvatarX());
                            writer.println(newWorld.getAvatarY());
                            writer.close();
                            System.out.println(isSpanish ? "Juego guardado exitosamente." : "Game saved successfully.");
                        } catch (IOException e) {
                            System.out.println(isSpanish ? "Error al guardar el juego: " + e.getMessage() : "Failed to save game: " + e.getMessage());
                        }
                        System.exit(0);
                    }
                }
            }
        }
    }
}