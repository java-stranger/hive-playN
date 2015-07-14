package hive.render;

import main.java.hive.engine.Game;
import playn.core.Platform;
import playn.java.JavaAssets;
import playn.java.LWJGLPlatform;
import playn.scene.SceneGame;

public class Main extends SceneGame {

	final Game game = new Game();
	final RenderField render;
	
	public Main(Platform plat) {
		super(plat, 25);
		
		plat.assets().getImage("images/black.png");
		
		((JavaAssets) plat.assets()).setPathPrefix("images");
		
		render = new RenderField(this, game.view);
		
		game.start();
	}

	public static void main(String[] args) {
			LWJGLPlatform.Config config = new LWJGLPlatform.Config();
			config.width = 800;
			config.height = 600;
		    LWJGLPlatform plat = new LWJGLPlatform(config);
		    plat.setTitle("Hive");
		    new Main(plat);
		    plat.start();
		  }

}
