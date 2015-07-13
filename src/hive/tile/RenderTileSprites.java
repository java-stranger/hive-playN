package hive.tile;

import java.util.HashMap;

import hive.engine.Player.Color;
import hive.view.Table;
import hive.view.Tile;
import playn.core.GL20;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;

public class RenderTileSprites extends RenderTile {
	final ImageLayer layer;
	
	public RenderTileSprites(Platform plat, Table view, GroupLayer parent, Tile tile, float tile_width) {
		super(view, tile);
		
		String filename = tile.getPiece().toString().toLowerCase() 
				+ (tile.getPiece().color() == Color.WHITE ? "_w" : "_b") + ".png";
		
		layer = TextureCache.createImageLayerFromResource(plat.assets(), 
				filename, tile_width, GL20.GL_LINEAR_MIPMAP_NEAREST);
		layer.setDepth(5);
		
		parent.add(layer);
	}
	
	@Override
	public RenderTile registerLayers(HashMap<Layer, RenderTile> layersMap) {
		layersMap.put(layer, this);
		return this;
	}
	
	@Override
	public Layer layer() {
		return layer;
	}
}
