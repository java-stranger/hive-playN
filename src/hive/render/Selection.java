package hive.render;

import java.util.HashMap;

import hive.tile.RenderTile;
import playn.scene.Layer;

public class Selection {
	final private HashMap<Layer, RenderTile> layerToTile;
	
	private RenderTile selected = null;
	
	Selection(HashMap<Layer, RenderTile> layerToTile) {
		this.layerToTile = layerToTile;
	}
	
	void select(Layer layer) {
		if(selected != null) {
    		selected.setSelected(false);
		}
		if(layer != null && layerToTile.containsKey(layer)) {
			selected = layerToTile.get(layer);
			selected.setSelected(true);
		} else {
			selected = null;
		}
	}
}
