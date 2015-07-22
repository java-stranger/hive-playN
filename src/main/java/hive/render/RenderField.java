package hive.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import hive.engine.Coordinate;
import hive.layers.BgLayer;
import hive.layers.RenderGrid;
import hive.pieces.Piece;
import hive.tile.HighlightCellFactory;
import hive.tile.RenderTile;
import hive.tile.RenderTilePseudo3D;
import hive.tile.RenderTileSprites;
import hive.view.Renderer;
import hive.view.Table;
import hive.view.Tile;
import hive.view.TileGrid;
import playn.core.Color;
import playn.core.Event.XY;
import playn.scene.GroupLayer;
import playn.scene.ImageLayer;
import playn.scene.Layer;
import playn.scene.LayerUtil;
import playn.scene.Pointer;
import playn.scene.SceneGame;
import pythagoras.f.Vector;
import react.Slot;

public class RenderField implements Renderer {

	final Table view;
	final SceneGame scene;
	final GroupLayer masterLayer = new GroupLayer();
	final GroupLayer interactiveLayer = new GroupLayer();
	final GroupLayer highlightLayer = new GroupLayer();
	final private BgLayer bgLayer;

	public final Pointer pointer;

	float tileSize;
	
	final TileGrid grid;
	
	HashMap<Coordinate, ArrayList<RenderTile>> tileCache = new HashMap<>();
	HashMap<Layer, RenderTile> layerToTile = new HashMap<>();
	
	final HighlightCellFactory highlightCellFactory;
	
	final Selection selection = new Selection();
	final KeyboardSlot keyboard;
	
	XY drag_start;
	Vector current_translation = new Vector();
	
	private enum RendererType {
		SPRITES,
		PSEUDO_3D
	} 
	RendererType rendererType = RendererType.PSEUDO_3D;
	
	public RenderField(SceneGame scene, Table table) {
		this.view = table;
		this.scene = scene;
		
		float w = scene.plat.graphics().viewSize.width();
		float h = scene.plat.graphics().viewSize.height();
		
	    masterLayer.setSize(w, h);
	    masterLayer.setTranslation(w / 2.f, h / 2.f);
//	    masterLayer.setOrigin(ImageLayer.Origin.CENTER);
		
		masterLayer.add(interactiveLayer);
		interactiveLayer.setDepth(1);
		masterLayer.add(highlightLayer);
		highlightLayer.setDepth(500);
		scene.rootLayer.add(masterLayer);

		// calculate the tile size in pixels, so the whole field is visible
		tileSize = Math.min(w / view.field.width(), h / view.field.height() * 2 / (float)Math.sqrt(3));
		// initialize tile-to-pixel coordinate conversion
		float margin = rendererType == RendererType.PSEUDO_3D ? 0.03f : 0f;
	    grid = new TileGrid(tileSize / 2.f * (1 + margin));
	    
		view.field.setRenderer(this);
	    
	    highlightCellFactory = new HighlightCellFactory(scene.plat, tileSize / 2);
		
		// set size & translation (so that (0,0) is the center of the window) of the master layer
		interactiveLayer.setSize(w, h);
	    //interactiveLayer.setTranslation(w / 2.f, h / 2.f);

	    // create, set size & translation of the background layer
		bgLayer = new BgLayer(scene.plat, w, h);
		bgLayer.layer.setDepth(-1);
	    masterLayer.add(bgLayer.layer);
	    
	    // add semi-transparent grid with coordinates (for debugging) 
	    RenderGrid gridShow = new RenderGrid(scene.plat, grid, w, h, -w/2, -h/2);
	    gridShow.layer.setDepth(10);
	    masterLayer.add(gridShow.layer);
	    
	    keyboard = new KeyboardSlot(scene.plat.input(), gridShow, table.game, scene.plat);
	    
		// combine mouse and touch into pointer events
		pointer = new Pointer(scene.plat, interactiveLayer, false);

	    // when the pointer is tapped/clicked, add a new pea
	    pointer.events.connect(new Slot<Pointer.Event>() {
	      @Override public void onEmit (Pointer.Event event) {
	        if (event.kind.isStart) { 
	        	drag_start = event;
	        	masterLayer.translation(current_translation);
	        	if(null != LayerUtil.layerUnderPoint(masterLayer, event.x - current_translation.x, event.y - current_translation.y)) {
	        		System.out.println("Clicked at (" + event + ") translated to " + grid.grid(event.x - current_translation.x, event.y - current_translation.y));
	        		view.onClick(grid.grid(event.x - current_translation.x, event.y - current_translation.y));
	        	}
//	        	Layer layer = LayerUtil.layerUnderPoint(interactiveLayer, event.x - current_translation.x, event.y - current_translation.y);
//	        	selection.select(layer == interactiveLayer ? null : layer);
//	        	((Main)scene).game.nextMove();
	        } else if (event.kind.isEnd) {
	        	masterLayer.translation(current_translation);
	        	Layer layer = LayerUtil.layerUnderPoint(interactiveLayer, event.x - current_translation.x, event.y - current_translation.y);
	        	if(layer != null && layer != interactiveLayer 
	        			&& (event.x != drag_start.x || event.y != drag_start.y))
	        		selection.select(null);
	        } else if (event.kind == Pointer.Event.Kind.DRAG) {
	        	float dx = event.x() - drag_start.x() + current_translation.x;
	        	float dy = event.y() - drag_start.y() + current_translation.y;
	        	if(Math.abs(dx) > masterLayer.width()) {
	        		dx = masterLayer.width() * Math.signum(dx);
	        	}
	        	if(Math.abs(dy) > masterLayer.height()) {
	        		dy = masterLayer.height() * Math.signum(dy);
	        	}
	        	
//	        	System.out.println(dx + "," + dy + ":" + masterLayer.width() + " " + masterLayer.height());
	        	masterLayer.setTranslation(dx, dy);
	        }
	      }
	    });
	}
	
	public void newPiece(Piece p, Coordinate c) {
		RenderTile rtile = createTileRenderer(p).registerLayers(layerToTile);
		insertAndMove(rtile, c);
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

	final static HashMap<HighlightType, Integer > highlightColors = new HashMap<>();
	static {
		highlightColors.put(HighlightType.MY_MOVES, Color.argb(255, 0, 255, 0));
		highlightColors.put(HighlightType.ADVERSARY_MOVES, Color.argb(255, 255, 0, 255));
		highlightColors.put(HighlightType.BORDER, Color.argb(255, 0, 0, 255));
	}

	@Override
	public void highlight(HashSet<Coordinate> coords, HighlightType type) {
		highlightLayer.removeAll();
		if(coords == null || coords.isEmpty()) return;
		int tint = highlightColors.get(type);
		coords.forEach((Coordinate c) -> {
			highlightLayer.addAt(highlightCellFactory.createLayer(tint), grid.pixelX(c), grid.pixelY(c));
		});
	}

	RenderTile createTileRenderer(Piece p) {
		if(rendererType == RendererType.PSEUDO_3D) {
			return new RenderTilePseudo3D(scene.plat, interactiveLayer, p, tileSize);			
		} else {
			return new RenderTileSprites(scene.plat, interactiveLayer, p, tileSize);
		}
	}
	
	public void select(Coordinate c) {
		if(tileCache.get(c) != null) {
			selection.select(tileCache.get(c).get(tileCache.get(c).size() - 1));
		} else {
			selection.select(null);
		}
	}
	
	public void reset() {
		tileCache.values().forEach((ArrayList<RenderTile> array) 
				-> array.forEach((RenderTile tile) 
						-> tile.remove()));;
	}

}
