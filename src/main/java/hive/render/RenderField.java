package hive.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import hive.engine.Coordinate;
import hive.layers.BgLayer;
import hive.layers.HighlightLayer;
import hive.layers.RenderGrid;
import hive.pieces.Piece;
import hive.tile.RenderTile;
import hive.tile.RenderTilePseudo3D;
import hive.tile.RenderTileSprites;
import hive.view.Renderer;
import hive.view.Table;
import hive.view.TileGrid;
import playn.core.Platform;
import playn.scene.GroupLayer;
import playn.scene.SceneGame;

public class RenderField implements Renderer {

	final private Platform plat;
	final private GroupLayer masterLayer = new GroupLayer();
	final private GroupLayer interactiveLayer = new GroupLayer();
	
	final private BgLayer bgLayer;
	final private HighlightLayer highlightLayer;
	final private RenderGrid gridShow;

	final TileGrid grid;
	
	HashMap<Coordinate, ArrayList<RenderTile>> tileCache = new HashMap<>();
	
	final Selection selection = new Selection();
	
	private enum RendererType {
		SPRITES { RendererType next() {return PSEUDO_3D;} },
		PSEUDO_3D { RendererType next() {return SPRITES;} };
		
		abstract RendererType next();
	} 
	static RendererType rendererType = RendererType.PSEUDO_3D;

	public RenderField(SceneGame scene, Table view) {
		this.plat = scene.plat;
		
		float w = plat.graphics().viewSize.width();
		float h = plat.graphics().viewSize.height();
		
	    masterLayer.setSize(w, h);
	    masterLayer.setTranslation(w / 2.f, h / 2.f);
//	    masterLayer.setOrigin(ImageLayer.Origin.CENTER);
		

		// calculate the tile size in pixels, so the whole field is visible
		float tileSize = Math.min(w / view.field.width(), h / view.field.height() * 2 / (float)Math.sqrt(3)) / 2.f;
		// initialize tile-to-pixel coordinate conversion
		float margin = rendererType == RendererType.PSEUDO_3D ? 0.03f : 0f;
	    grid = new TileGrid(tileSize * (1 + margin));
	    
		view.field.setRenderer(this);
		
		// set size & translation (so that (0,0) is the center of the window) of the master layer
		interactiveLayer.setSize(w, h);
		masterLayer.add(interactiveLayer);
		interactiveLayer.setDepth(1);

	    // create, set size & translation of the background layer
		bgLayer = new BgLayer(plat, w, h);
		bgLayer.layer.setDepth(-1);
	    masterLayer.add(bgLayer.layer);
	    
	    // add semi-transparent grid with coordinates (for debugging) 
	    gridShow = new RenderGrid(plat, grid, w, h, -w/2, -h/2);
	    gridShow.layer.setDepth(10);
	    masterLayer.add(gridShow.layer);
	    
	    highlightLayer = new HighlightLayer(plat, grid);
	    highlightLayer.layer.setDepth(500);
	    masterLayer.add(highlightLayer.layer);
	    
		scene.rootLayer.add(masterLayer);
	}
	
	public static RenderField toggleMode(SceneGame scene, Table view, RenderField field) {
		rendererType = rendererType.next();
		RenderField newMode = new RenderField(scene, view);
		field.tileCache.entrySet().forEach((Entry<Coordinate, ArrayList<RenderTile>> entry) -> {
			for(RenderTile tile: entry.getValue()) {
				newMode.newPiece(tile.piece(), entry.getKey());
			}
		});
		field.close();
		return newMode;
	}
	
	public GroupLayer getMasterLayer() {
		return masterLayer;
	}

	public TileGrid getGrid() {
		return grid;
	}

	public RenderGrid getGridShow() {
		return gridShow;
	}

	public void newPiece(Piece p, Coordinate c) {
		insertAndMove(createTileRenderer(p), c);
	}

	public void insertAndMove(RenderTile rtile, Coordinate c) {
		tileCache.computeIfAbsent(c, (Coordinate x) -> new ArrayList<RenderTile>()).add(rtile);
		rtile.moveTo(grid.pixelX(c), grid.pixelY(c));
		if(tileCache.get(c).size() > 1) {
			rtile.layer().setDepth(tileCache.get(c).get(tileCache.get(c).size() - 2).layer().depth() + 1); // put it on top
		}
//		System.out.println("Added tile @ " + t.getX() + "," + t.getY() + " in field " + this + " tile size" + tileSize);
	}

	public RenderTile removePiece(Piece p, Coordinate c) {
		for(int i = tileCache.get(c).size() - 1; i >= 0; --i) {
			if(tileCache.get(c).get(i).piece().equals(p))
				return tileCache.get(c).remove(i);
		}
		return null;
	}

	public void movePiece(Piece p, Coordinate from, Coordinate to) {
		insertAndMove(removePiece(p, from), to);
	}

	@Override
	public void highlight(HashSet<Coordinate> coords, HighlightType type) {
		highlightLayer.highlight(coords, type);
	}

	RenderTile createTileRenderer(Piece p) {
		if(rendererType == RendererType.PSEUDO_3D) {
			return new RenderTilePseudo3D(plat, interactiveLayer, p, grid.tileSize() * 2);			
		} else {
			return new RenderTileSprites(plat, interactiveLayer, p, grid.tileSize() * 2);
		}
	}
	
	public void select(Coordinate c) {
		if(tileCache.get(c) != null) {
			selection.select(tileCache.get(c).get(tileCache.get(c).size() - 1));
		} else {
			selection.select(null);
		}
	}
	
	public void close() {
		masterLayer.close();
	}

}
