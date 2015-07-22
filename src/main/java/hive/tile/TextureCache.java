package hive.tile;

import java.util.HashMap;

import playn.core.Assets;
import playn.core.Image;
import playn.core.Texture;
import playn.scene.ImageLayer;
import react.RFuture;
import react.Slot;
import react.Try;

public class TextureCache {

	private static HashMap<String, RFuture<Texture> > textureCache = new HashMap<>();

	public static ImageLayer createImageLayerFromResource(Assets assets, String res_name, float exp_width, int resampling_method) {
		ImageLayer layer = new ImageLayer();
		RFuture<Texture> tx = textureCache.computeIfAbsent(res_name, 
				(String path) -> {
					Image im = assets.getImage(path);
					im.setConfig(new Texture.Config(true, false, false, resampling_method, resampling_method, true));
					return im.textureAsync();
				});
		tx.onComplete(new Slot<Try<Texture>>() {
			@Override
			public void onEmit(Try<Texture> texture) {
				layer.setScale(exp_width / texture.get().width());
			}
		});
		layer.setTile(tx);
		layer.setOrigin(ImageLayer.Origin.CENTER);
		return layer;
	}
	
	public static Image getImage(Assets assets, String path) {
		return assets.getImage(path);
	}
}
