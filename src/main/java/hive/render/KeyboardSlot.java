package hive.render;

import hive.engine.Game;
import hive.layers.RenderGrid;
import playn.core.Input;
import playn.core.Keyboard.KeyEvent;
import playn.core.Keyboard.KeySlot;
import react.Slot;
import react.Try;

public class KeyboardSlot extends KeySlot {
	
	final RenderGrid grid;
	final Game game;
	final Input input;
	
	KeyboardSlot(Input input, RenderGrid grid, Game game) {
		this.input = input;
		this.grid = grid;
		this.game = game;
		
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
				input.getText(null, "Filename", "position.hv").onComplete(new Slot<Try<String>>() {
					@Override
					public void onEmit(Try<String> event) {
						game.savePosition(event.get());
					}
				});
				break;
			default:
			}
		}
	}

}
