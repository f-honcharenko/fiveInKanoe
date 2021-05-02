package com.dreamwalker.game.tools;

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
        if (this.gameMenuScreen == null) {
            this.gameMenuScreen = new GameMenuScreen(this.game);
        }
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
}
