package hive.render;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import java.nio.file.Paths;

import hive.engine.Game;
import hive.layers.RenderGrid;
import playn.core.Input;
import playn.core.Keyboard.KeyEvent;
import playn.core.Keyboard.KeySlot;
import playn.core.Platform;
import playn.java.SWTPlatform;

public class KeyboardSlot extends KeySlot {
	
	final RenderGrid grid;
	final Game game;
	final Input input;
	final Platform plat;
	
	KeyboardSlot(Input input, RenderGrid grid, Game game, Platform plat) {
		this.input = input;
		this.grid = grid;
		this.game = game;
		this.plat = plat;
		
		input.keyboardEvents.connect(this);
	}
	
	@Override
	public void onEmit(KeyEvent event) {
		if(event.down) {
			switch(event.key) {
			case G:
				grid.toggleVisible();
				break;
			case SPACE:
				game.nextMove();
				break;
			case R:
				game.reset();
				break;
			case U:
				game.undoLastMove();
				break;
			case P:
				FileDialog fd = new FileDialog(((SWTPlatform)plat).shell(), SWT.SAVE);
				fd.open();
				System.out.println("Saving to " + Paths.get(fd.getFilterPath(), fd.getFileName()));
//				input.getText(null, "Filename", "position.hv").onComplete(new Slot<Try<String>>() {
//					@Override
//					public void onEmit(Try<String> event) {
//						game.savePosition(event.get());
//					}
//				});
				break;
			case B:
				game.displayBorder();
				break;
			default:
			}
		}
	}

}
