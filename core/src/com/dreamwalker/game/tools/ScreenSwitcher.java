package com.dreamwalker.game.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.screens.*;

public class ScreenSwitcher {
    private static DreamWalker game;

    private static MainMenuScreen mainMenuScreen;
    private static GameScreen gameScreen;
    private static GameMenuScreen gameMenuScreen;

    public static void setGame(DreamWalker _game) {
        game = _game;
    }

    public static void ToMainMenu() {
        if (mainMenuScreen == null) {
            mainMenuScreen = new MainMenuScreen(game);
        }
        game.setScreen(mainMenuScreen);
    }

    public static void DisposeMainMenu() {
        if (mainMenuScreen != null) {
            mainMenuScreen.dispose();
            mainMenuScreen = null;
        }
    }

    public static void ToGameMenu() {
        Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        gameMenuScreen = new GameMenuScreen(game, new Texture(flipPixmap(pixmap)));

        game.setScreen(gameMenuScreen);
    }

    public static void DisposeGameMenu() {
        if (gameMenuScreen != null) {
            gameMenuScreen.dispose();
            gameMenuScreen = null;
        }
    }

    public static void ToGame() {
        if (gameScreen == null) {
            gameScreen = new GameScreen(game);
        }
        game.setScreen(gameScreen);
    }

    public static void DisposeGame() {
        if (gameScreen != null) {
            gameScreen.dispose();
            gameScreen = null;
        }
    }

    private static Pixmap getScreenshot(int x, int y, int w, int h, boolean yDown) {
        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
        return pixmap;
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

}
