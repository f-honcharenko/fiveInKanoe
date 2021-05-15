package com.dreamwalker.game.desktop;

import com.badlogic.gdx.Files.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dreamwalker.game.DreamWalker;

public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 800;
		config.title = "DreamWalker v.0.1";
		config.fullscreen = true;
		config.addIcon("game_logo32.png", FileType.Internal);
		new LwjglApplication(new DreamWalker(), config);
	}

}
