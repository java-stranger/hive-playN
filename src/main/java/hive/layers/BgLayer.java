package hive.layers;

import playn.core.Image;
import playn.core.Platform;
import playn.core.Texture;
import playn.scene.ImageLayer;
import react.Slot;

public class BgLayer {
	
	public final ImageLayer layer = new ImageLayer();
	private static final String texture = "wood.jpg";

	public BgLayer(Platform plat, float w, float h) {
		
	    Image image = plat.assets().getImage(texture);
	    image.setConfig(Texture.Config.DEFAULT.repeat(true, true));
	    layer.setTile(image.textureAsync());

	    image.state.bindComplete(new Slot<Boolean> () {
	    	@Override
	    	public void onEmit(Boolean event) {
	    		if(event) {
	    			float scale = h / (image.height() * 10); 
	    		    layer.setScale(scale);
	    		    layer.setSize(2*w / scale, 2*h / scale);
	    		    layer.setOrigin(ImageLayer.Origin.CENTER);
//	    		    layer.setTranslation( -w, -h);
	    		}
	    	}
	    ;});
	}
}
