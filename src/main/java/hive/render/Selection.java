package hive.render;

import hive.tile.RenderTile;

public class Selection {
	private RenderTile selected = null;
	
	void select(RenderTile tile) {
		if(selected != null) {
    		selected.setSelected(false);
		}
		selected = tile;
		if(tile != null) {
			selected.setSelected(true);
		}
	}
}
