package com.dreamwalker.game;

import com.badlogic.gdx.Files.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dreamwalker.game.DreamWalker;

public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 80 * 16;
		config.height = 80 * 9;
		config.title = "DreamWalker v.0.1";
		config.fullscreen = false; // PROD TRUE
		// config.vSyncEnabled = true;
		config.addIcon("game_logo32.png", FileType.Internal);
		new LwjglApplication(new DreamWalker(), config);
	}

}
