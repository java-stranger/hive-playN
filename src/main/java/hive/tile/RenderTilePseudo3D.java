package hive.tile;

import hive.pieces.Piece;
import playn.core.GL20;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;

public class RenderTilePseudo3D extends RenderTile {

	final private GroupLayer layer = new GroupLayer();
	
	final static private String prefix = "images/pseudo3d/";
	
	public RenderTilePseudo3D(Platform plat, GroupLayer parent, Piece piece, float tile_width) {
		super(piece);
		
		ImageLayer bg = TextureCache.createImageLayerFromResource(plat.assets(), 
				getBgFilename(piece), tile_width, GL20.GL_LINEAR_MIPMAP_LINEAR);
		layer.add(bg.setDepth(5));

		ImageLayer icon = TextureCache.createImageLayerFromResource(plat.assets(), 
				getIconFilename(piece), tile_width, GL20.GL_LINEAR_MIPMAP_NEAREST);
		layer.add(icon.setDepth(5.1f));

		parent.add(layer);
	}
	
	public static String getIconFilename(Piece piece) {
		return prefix + piece.getType().toString().toLowerCase() + ".png";
	}
		
	public static String getBgFilename(Piece piece) {
		return prefix + piece.color().toString().toLowerCase() + ".png";
	}
		
	@Override
	public Layer layer() {
		return layer;
	}
}
