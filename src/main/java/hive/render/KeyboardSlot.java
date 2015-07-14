package hive.render;

import hive.layers.RenderGrid;
import main.java.hive.engine.Game;
import playn.core.Keyboard.KeyEvent;
import playn.core.Keyboard.KeySlot;

public class KeyboardSlot extends KeySlot {
	
	final RenderGrid grid;
	final Game game;
	
	KeyboardSlot(RenderGrid grid, Game game) {
		this.grid = grid;
		this.game = game;
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
			default:
			}
		}
	}

}
