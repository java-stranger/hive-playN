package control;

import hive.engine.IController;
import hive.render.RenderField;
import playn.core.Input;
import playn.core.Keyboard.KeyEvent;
import playn.core.Keyboard.KeySlot;
import playn.core.Platform;

public class KeyboardSlot extends KeySlot {
	
	final RenderField renderer;
	final IController control;
	final Platform plat;
	final Input input;
	
	public KeyboardSlot(Input input, RenderField renderer, IController control, Platform plat) {
		this.renderer = renderer;
		this.control = control;
		this.plat = plat;
		this.input = input;
		
		input.keyboardEvents.connect(this);
	}
	
	public void close() {
		input.keyboardEvents.disconnect(this);
	}
	
	@Override
	protected void finalize() {
		System.out.println("Forgot to close object " + this);
		close();
	}
	
	@Override
	public void onEmit(KeyEvent event) {
		if(event.down) {
			switch(event.key) {
			case G:
				renderer.getGridShow().toggleVisible();
				break;
			case SPACE:
				onNextMove();
				break;
			case N:
				onNewGame();
				break;
			case U:
				onUndoLastMove();
				break;
			case B:
				control.displayBorder();
				break;
			case Y:
				onToggleDisplay();
				break;
			default:
			}
		}
	}
	
	public void onNewGame() {
		control.newGame();
	}

	public void onNextMove() {
		control.nextMove();
	}

	public void onUndoLastMove() {
		control.undoLastMove();
	}

	public void onToggleDisplay() {
	}


}
