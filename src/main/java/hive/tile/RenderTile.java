package hive.tile;

import java.util.HashMap;

import hive.pieces.Piece;
import playn.core.Color;
import playn.scene.Layer;

abstract public class RenderTile {
	final protected Piece piece;
	
	RenderTile(Piece piece) {
		this.piece = piece;
	}

	public abstract RenderTile registerLayers(HashMap<Layer, RenderTile> layersMap);
	public abstract Layer layer();
	
	public void moveTo(float x, float y) {
		System.out.println("Moving tile to " + x + "," + y);
		layer().setTranslation(x, y);
	}
	
	public void remove() {
		layer().close();
	}

	public Piece piece() {
		return piece;
	}
	
	public void setSelected(boolean enable) {
		if(enable) {
			layer().setTint(Color.rgb(128, 128, 128));
		} else {
			layer().setTint(Color.rgb(255, 255, 255));
		}
	}
}
