package hive.tile;

import java.util.HashMap;

import hive.view.Table;
import hive.view.Tile;
import playn.core.GL20;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;

public class RenderTilePseudo3D extends RenderTile {

	final private GroupLayer layer = new GroupLayer();
	
	public RenderTilePseudo3D(Platform plat, Table view, GroupLayer parent, Tile tile, float tile_width) {
		super(view, tile);
		
		String filename_icon = tile.getPiece().getType().toString().toLowerCase() + ".png";
		String filename_bg = tile.getPiece().color().toString().toLowerCase() + ".png";
		
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
