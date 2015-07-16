package hive.tile;

import java.util.HashMap;

import hive.pieces.Piece;
import playn.core.GL20;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;

public class RenderTilePseudo3D extends RenderTile {

	final private GroupLayer layer = new GroupLayer();
	
	public RenderTilePseudo3D(Platform plat, GroupLayer parent, Piece piece, float tile_width) {
		super(piece);
		
		String filename_icon = piece.getType().toString().toLowerCase() + ".png";
		String filename_bg = piece.color().toString().toLowerCase() + ".png";
		
		ImageLayer bg = TextureCache.createImageLayerFromResource(plat.assets(), 
				filename_bg, tile_width, GL20.GL_LINEAR_MIPMAP_LINEAR);
		layer.add(bg.setDepth(5));

		ImageLayer icon = TextureCache.createImageLayerFromResource(plat.assets(), 
				filename_icon, tile_width, GL20.GL_LINEAR_MIPMAP_NEAREST);
		layer.add(icon.setDepth(6));

		parent.add(layer);
	}
	
	
	@Override
	public RenderTile registerLayers(HashMap<Layer, RenderTile> layersMap) {
		// register all children layers to refer to us
		layer.forEach((Layer l) -> layersMap.put(l, this));
		return this;
	}
	
	@Override
	public Layer layer() {
		return layer;
	}
}
