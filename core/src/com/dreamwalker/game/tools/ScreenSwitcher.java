package com.dreamwalker.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.entities.player.Inventory;
import com.dreamwalker.game.screens.*;

public class ScreenSwitcher {
    private static DreamWalker game;
    private static MainMenuScreen mainMenuScreen;
    private static GameScreen gameScreen;
    private static GameMenuScreen gameMenuScreen;
    private static InventoryScreen inventoryScreen;
    private static NewGameScreen newGameScreen;

    public static void setGame(DreamWalker game) {
        ScreenSwitcher.game = game;
    }

    public static void toMainMenu() {
        if (mainMenuScreen == null) {
            mainMenuScreen = new MainMenuScreen(game);
        }
        game.setScreen(mainMenuScreen);
        gameScreen = null; // !!!
    }

    public static void disposeMainMenu() {
        if (mainMenuScreen != null) {
            mainMenuScreen.dispose();
            mainMenuScreen = null;
        }
    }

    public static void toGameMenu() {
        Pixmap pixmap = getScreenshot(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameMenuScreen = new GameMenuScreen(game, new Texture(flipPixmap(pixmap)));

        game.setScreen(gameMenuScreen);
    }

    public static void toNewGameMenu() {
        Pixmap pixmap = getScreenshot(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        newGameScreen = new NewGameScreen(game, new Texture(flipPixmap(pixmap)));

        game.setScreen(newGameScreen);
    }

    public static void disposeNewGameMenu() {
        if (newGameScreen != null) {
            newGameScreen.dispose();
            newGameScreen = null;
        }
    }

    public static void toInventory(Inventory inv) {
        Pixmap pixmap = getScreenshot(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        inventoryScreen = new InventoryScreen(game, new Texture(flipPixmap(pixmap)), inv);

        game.setScreen(inventoryScreen);
    }

    public static void disposeInventory() {
        if (inventoryScreen != null) {
            inventoryScreen.dispose();
            inventoryScreen = null;
        }
    }

    public static void disposeGameMenu() {
        if (gameMenuScreen != null) {
            gameMenuScreen.dispose();
            gameMenuScreen = null;
        }
    }

    public static void toGame() {
        if (gameScreen == null) {
            gameScreen = new GameScreen(game);
        }
        game.setScreen(gameScreen);
    }

    public static void disposeGame() {
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    private static Pixmap getScreenshot(int w, int h) {
        return Pixmap.createFromFrameBuffer(0, 0, w, h);
    }

    private static Pixmap flipPixmap(Pixmap src) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        Pixmap flipped = new Pixmap(width, height, src.getFormat());

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                flipped.drawPixel(x, y, src.getPixel(x, height - y - 1));
            }
        }
        return flipped;
    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }
}
