package hive.layers;

import java.util.HashMap;
import java.util.HashSet;

import hive.engine.Coordinate;
import hive.tile.HighlightCellFactory;
import hive.view.Renderer.HighlightType;
import hive.view.TileGrid;
import playn.core.Color;
import playn.core.Platform;
import playn.scene.GroupLayer;

public class HighlightLayer {

	final public GroupLayer layer = new GroupLayer();
	final TileGrid grid;
	
	final HighlightCellFactory highlightCellFactory;
	
	final static HashMap<HighlightType, Integer > highlightColors = new HashMap<>();
	static {
		highlightColors.put(HighlightType.MY_MOVES, Color.argb(255, 0, 255, 0));
		highlightColors.put(HighlightType.ADVERSARY_MOVES, Color.argb(255, 255, 0, 255));
		highlightColors.put(HighlightType.BORDER, Color.argb(255, 0, 0, 255));
	}
	
	public HighlightLayer(Platform plat, TileGrid grid) {
	    highlightCellFactory = new HighlightCellFactory(plat, grid.tileSize());
	    this.grid = grid;
	}
	
	public void highlight(HashSet<Coordinate> coords, HighlightType type) {
		layer.removeAll();
		if(coords == null || coords.isEmpty()) return;
		int tint = highlightColors.get(type);
		coords.forEach((Coordinate c) -> {
			layer.addAt(highlightCellFactory.createLayer(tint), grid.pixelX(c), grid.pixelY(c));
		});
	}


}
