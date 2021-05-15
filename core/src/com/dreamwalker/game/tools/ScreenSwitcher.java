package com.dreamwalker.game.tools;

import java.nio.ByteBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.dreamwalker.game.DreamWalker;
import com.dreamwalker.game.screens.*;

public class ScreenSwitcher {
    private DreamWalker game;

    private static MainMenuScreen mainMenuScreen;
    private static GameScreen gameScreen;
    private static GameMenuScreen gameMenuScreen;

    public ScreenSwitcher(DreamWalker game) {
        this.game = game;

    }

    public void ToMainMenu() {
        if (this.mainMenuScreen == null) {
            this.mainMenuScreen = new MainMenuScreen(this.game);
        }
        this.game.setScreen(this.mainMenuScreen);
    }

    public void DisposeMainMenu() {
        if (this.mainMenuScreen != null) {
            this.mainMenuScreen.dispose();
            this.mainMenuScreen = null;
        }
    }

    public void ToGameMenu() {
        Pixmap pixmap = getScreenshot(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        this.gameMenuScreen = new GameMenuScreen(this.game, new Texture(flipPixmap(pixmap)));

        this.game.setScreen(this.gameMenuScreen);
    }

    public void DisposeGameMenu() {
        if (this.gameMenuScreen != null) {
            this.gameMenuScreen.dispose();
            this.gameMenuScreen = null;
        }
    }

    public void ToGame() {
        if (this.gameScreen == null) {
            this.gameScreen = new GameScreen(this.game);
        }
        this.game.setScreen(this.gameScreen);
    }

    public void DisposeGame() {
        if (this.gameScreen != null) {
            this.gameScreen.dispose();
            this.gameScreen = null;
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
