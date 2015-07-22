package common;

import control.KeyboardSlot;
import control.MouseSlot;
import hive.main.MainApp;
import hive.render.RenderField;
import playn.core.Platform;
import playn.java.JavaAssets;
import playn.scene.SceneGame;

public class Main extends SceneGame {

	RenderField render;
	KeyboardSlot keyboard;
	MouseSlot mouse;
	MainApp app;

	public Main(Platform plat, MainApp app) {
		super(plat, 25);
		this.app = app;
	}
	
	public void newGame() {
		
		if(render != null)
			render.close();
		
		// this order is important: the renderer must be created before new game!
		render = new RenderField(this, app.view());

		app.createNewGame();
		createControllers();
	}
	
	public void toggleRenderingStyle() {
		render = RenderField.toggleMode(this, app.view(), render);
		createControllers();
	}

	private void createControllers() {
		if(keyboard != null)
			keyboard.close();
		
		keyboard = new KeyboardSlot(plat.input(), render, app.controller(), plat) {
			public void onNewGame() {
				newGame();
			};

			public void onToggleDisplay() {
				toggleRenderingStyle();
			}
		};
		
		mouse = new MouseSlot(plat, app.controller(), render);		
	}
}
