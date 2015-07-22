package hive.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;

import hive.engine.Game;
import hive.render.RenderField;
import playn.core.Platform;
import playn.java.JavaAssets;
import playn.java.SWTPlatform;
import playn.scene.SceneGame;

public class Main extends SceneGame {

	static final Game game = new Game();
	final RenderField render;

	public Main(Platform plat) {
		super(plat, 25);

		((JavaAssets) plat.assets()).setPathPrefix("images");

		render = new RenderField(this, game.view);

		game.start();
	}

	public static void main(String[] args) {

		SWTPlatform plat = new SWTPlatform(new SWTPlatform.Config()) {
			protected void preInit() {
				super.preInit();
				shell().setLayout(new RowLayout());
				shell().setLayoutData(new RowData(100, 20));

				Button b = new Button(shell(), SWT.PUSH);
				b.setText("Push me!");
				b.setSize(100, 20);
				
				new MainMenu(shell(), game);
			};
		};

		plat.setTitle("Hive");
		new Main(plat);
		plat.start();
	}

}
