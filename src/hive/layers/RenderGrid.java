package hive.layers;

import hive.engine.Coordinate;
import hive.view.TileGrid;
import playn.core.Canvas;
import playn.core.Color;
import playn.core.Font;
import playn.core.Platform;
import playn.core.TextFormat;
import playn.core.TextLayout;
import playn.scene.CanvasLayer;

public class RenderGrid {
	public final CanvasLayer layer;
	
	public RenderGrid(Platform plat, TileGrid grid, float w, float h, float tx, float ty) {
		layer = new CanvasLayer(plat.graphics(), w, h);
		
		final float dx = (grid.pixelX(Coordinate.axial(1, 0)) - grid.pixelX(Coordinate.axial(0, 0))) * 2 / 3 / 2; 
		final float dy = (grid.pixelY(Coordinate.axial(1, 0)) - grid.pixelY(Coordinate.axial(0, 0)));

		TextFormat fmt = new TextFormat(new Font("Helvetica", 12), true);
		Canvas c = layer.begin();
		c.setStrokeColor(Color.rgb(0, 0, 0));
//		c.setStrokeWidth(5);
		for(int i = -10; i < 10; ++i) {
			for(int j = -10; j < 10; ++j) {
				TextLayout layout = plat.graphics().layoutText("(" + j + ","+ i +")", fmt);
				Coordinate pos = Coordinate.axial(j, i);
				c.setFillColor(Color.rgb(255, 255, 255));
				c.fillRect(grid.pixelX(pos) - layout.bounds.width()/2 - tx, grid.pixelY(pos) - layout.bounds.height()/2 - ty, 
						layout.bounds.width(), layout.bounds.height());

				c.setFillColor(Color.rgb(0, 0, 0));
				c.fillText(layout, grid.pixelX(pos) - layout.bounds.width()/2 - tx, grid.pixelY(pos) - layout.bounds.height()/2 - ty);
				
				float x0 = grid.pixelX(pos) + dx - tx, y0 = grid.pixelY(pos) + dy - ty;
				float x1 = x0 + dx, y1 = y0 - dy;
				c.drawLine(x0, y0, x1, y1);
				y0 = y1 - dy;
				c.drawLine(x1, y1, x0, y0);
				c.drawLine(x0, y0, x0 - dx * 2, y0);
				
			}
		}
		layer.end();
		layer.setTranslation(tx, ty);
		layer.setTint(Color.argb(192, 255, 255, 255));
//		layer.setDepth(1);
	}
	
	void setVisible(boolean visible) {
		layer.setVisible(visible);
	}

	public boolean toggleVisible() {
		boolean visible = !layer.visible();
		setVisible(visible);
		return visible;
	}


}
