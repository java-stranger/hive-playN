package playn.app;

import common.Main;
import hive.main.MainApp;
import playn.java.LWJGLPlatform;

public class App {

	public static void main(String[] args) {
    	MainApp app = new MainApp();
    	
		LWJGLPlatform.Config config = new LWJGLPlatform.Config();
		config.width = 800;
		config.height = 600;
	    LWJGLPlatform plat = new LWJGLPlatform(config);
		
		Main main = new Main(plat, app);
		main.newGame();

		plat.setTitle("Hive");
		plat.start();
 	}

}
