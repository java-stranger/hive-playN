package hive.tile;

import java.util.HashMap;

import hive.pieces.Piece;
import hive.player.IPlayer;
import hive.player.IPlayer.Color;
import playn.core.GL20;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;

public class RenderTileSprites extends RenderTile {
	final ImageLayer layer;
	
	public RenderTileSprites(Platform plat, GroupLayer parent, Piece piece, float tile_width) {
		super(piece);
		
		String filename = piece.toString().toLowerCase() 
				+ (piece.color() == IPlayer.Color.WHITE ? "_w" : "_b") + ".png";
		
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
