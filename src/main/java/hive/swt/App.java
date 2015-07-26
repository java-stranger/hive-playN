package hive.swt;

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;

import common.Main;
import hive.main.MainApp;
import hive.pieces.Piece;
import hive.pieces.PieceType;
import hive.player.IPlayer;
import hive.tile.RenderTileSprites;
import playn.java.SWTPlatform;

public class App {

    public static void main( String [] args ) {
    	MainApp app = new MainApp();
    	
    	SWTPlatform.Config config = new SWTPlatform.Config();
		config.width = 800;
		config.height = 600;
    	
		SWTPlatform plat = new SWTPlatform(config) {
			protected void preInit() {
				super.preInit();

				new MainMenu(shell(), app.controller());
				new MoveList(shell(), app.controller());

				GridLayout layout = new GridLayout();
				layout.numColumns = 2;
				shell().setLayout(layout);
//				shell().setLayoutData(new RowData(100, 200));

			};
		};
		
		Main main = new Main(plat, app);
		main.newGame();

		String path = RenderTileSprites.getFilename(Piece.createNew(IPlayer.Color.WHITE, PieceType.QUEEN));
		try {
			ClassLoader loader = App.class.getClassLoader();
			InputStream is = loader.getResourceAsStream(plat.assets().getPathPrefix() + path);
			Image icon = new Image(plat.shell().getDisplay(), is);
			is.close();
			plat.shell().setImage(icon);
		} catch (Throwable e) {
			System.out.println("Failed to load " + path + ": " + e);
		}

		//plat.assets().
		//plat.shell().setImage(new Image(plat.shell().getDisplay(), path));

		plat.setTitle("Hive");
		plat.start();
    }
}
