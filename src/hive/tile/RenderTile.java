package hive.tile;

import java.util.HashMap;

import hive.view.Table;
import hive.view.Tile;
import playn.core.Color;
import playn.scene.Layer;

abstract public class RenderTile {
	final protected Tile tile;
	final protected Table view;
	
	RenderTile(Table view, Tile tile) {
		this.tile = tile;
		this.view = view;
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
	
	public Tile tile() {
		return tile;
	}
	
	public void setSelected(boolean enable) {
		if(enable) {
			layer().setTint(Color.rgb(128, 128, 128));
			view.onTileSelected(tile);
		} else {
			layer().setTint(Color.rgb(255, 255, 255));
			view.onTileSelected(null);
		}
	}
}
