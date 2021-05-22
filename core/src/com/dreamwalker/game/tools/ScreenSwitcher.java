package com.dreamwalker.game.tools;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
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

    public void toMainMenu() {
        if (this.mainMenuScreen == null) {
            this.mainMenuScreen = new MainMenuScreen(this.game, this);
        }
        this.game.setScreen(this.mainMenuScreen);
    }

    public void disposeMainMenu() {
        if (this.mainMenuScreen != null) {
            this.mainMenuScreen.dispose();
            this.mainMenuScreen = null;
        }
    }

    public void toGameMenu() {
        if (this.gameMenuScreen == null) {
            this.gameMenuScreen = new GameMenuScreen(this.game, this);
        }
        this.game.setScreen(this.gameMenuScreen);
    }

    public void disposeGameMenu() {
        if (this.gameMenuScreen != null) {
            this.gameMenuScreen.dispose();
            this.gameMenuScreen = null;
        }
    }

    public void toGame() {
        if (this.gameScreen == null) {
            this.gameScreen = new GameScreen(this.game);
        }
        this.game.setScreen(this.gameScreen);
    }

    public void disposeGame() {
        if (this.gameScreen != null) {
            this.gameScreen.dispose();
            this.gameScreen = null;
        }
    }

    public static void setGameScreen(GameScreen gameScreen) {
        ScreenSwitcher.gameScreen = gameScreen;
    }

    public static GameScreen getGameScreen() {
        return gameScreen;
    }

}
