package hive.tile;

import hive.pieces.Piece;
import hive.player.IPlayer;
import playn.core.GL20;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;

public final class RenderTileSprites extends RenderTile {
	final private ImageLayer layer;
	
	final static private String prefix = "images/sprites/";

	public RenderTileSprites(Platform plat, GroupLayer parent, Piece piece, float tile_width) {
		super(piece);
		
		layer = TextureCache.createImageLayerFromResource(plat.assets(), 
				getFilename(piece), tile_width, GL20.GL_LINEAR_MIPMAP_NEAREST);
		layer.setDepth(5);
		
		parent.add(layer);
	}
	
	public static String getFilename(Piece piece) {
		return prefix + piece.getType().toString().toLowerCase() 
				+ (piece.color() == IPlayer.Color.WHITE ? "_w" : "_b") + ".png";
	}
	
	@Override
	public Layer layer() {
		return layer;
	}
}
