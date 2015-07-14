package hive.tile;

import playn.core.Canvas;
import playn.core.Color;
import playn.core.Graphics;
import playn.core.Path;
import playn.core.Platform;
import playn.scene.CanvasLayer;
import playn.scene.Layer.Origin;

public class HighlightCellFactory {
	final Canvas contour;
	final Graphics gfx;
	
	public HighlightCellFactory(Platform plat, float rad) {
		gfx = plat.graphics();
		float dx = rad / 2;
		float dy = rad * (float)Math.sqrt(3.) / 2;
		contour = plat.graphics().createCanvas(rad * 2, dy * 2);
		Path path = contour.createPath();
		path.moveTo(dx, 0);
		path.lineTo(3*dx, 0);
		path.lineTo(4*dx, dy);
		path.lineTo(3*dx, 2*dy);
		path.lineTo(dx, 2*dy);
		path.lineTo(0, dy);
		path.close();
//		contour.setStrokeColor(Color.argb(128, 0, 255, 0));
//		contour.setFillColor(Color.argb(64, 128, 255, 128));
		contour.setStrokeColor(Color.argb(128, 255, 255, 255));
		contour.setFillColor(Color.argb(64, 255, 255, 255));
		contour.setStrokeWidth(2);
		contour.drawLine(dx, 0, 3*dx, 0);
		contour.drawLine(3*dx, 0, 4*dx, dy);
		contour.drawLine(4*dx, dy, 3*dx, 2*dy);
		contour.drawLine(3*dx, 2*dy, dx, 2*dy);
		contour.drawLine(dx, 2*dy, 0, dy);
		contour.drawLine(0, dy, dx, 0);
		contour.fillPath(path);
	}
	
	public CanvasLayer createLayer(int tint) {
		CanvasLayer layer = new CanvasLayer(gfx, contour);
		layer.setOrigin(Origin.CENTER);
		layer.setTint(tint);
		return layer;
	}
}
