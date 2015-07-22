package control;

import hive.engine.Coordinate;
import hive.engine.IController;
import hive.render.RenderField;
import playn.core.Event.XY;
import playn.core.Platform;
import playn.scene.Layer;
import playn.scene.LayerUtil;
import playn.scene.Pointer;
import pythagoras.f.Vector;
import react.Slot;

public class MouseSlot {

	private final Pointer pointer;

	private XY drag_start;
	private Vector current_translation = new Vector();
	
	public MouseSlot(Platform plat, IController controller, RenderField render) {
		
		// combine mouse and touch into pointer events
		pointer = new Pointer(plat, render.getMasterLayer(), false);

	    // when the pointer is tapped/clicked, add a new pea
	    pointer.events.connect(new Slot<Pointer.Event>() {
	      @Override public void onEmit (Pointer.Event event) {
	        if (event.kind.isStart) { 
	        	drag_start = event;
	        	render.getMasterLayer().translation(current_translation);
	        	if(null != LayerUtil.layerUnderPoint(render.getMasterLayer(), 
	        			event.x - current_translation.x, event.y - current_translation.y)) {
	        		Coordinate ingrid = render.getGrid().grid(event.x - current_translation.x, event.y - current_translation.y);
	        		System.out.println("Clicked at (" + event + ") translated to " + ingrid);
	        		controller.onClick(ingrid);
	        	}
	        } else if (event.kind == Pointer.Event.Kind.DRAG) {
	        	float dx = event.x() - drag_start.x() + current_translation.x;
	        	float dy = event.y() - drag_start.y() + current_translation.y;
	        	
	        	render.getMasterLayer().setTranslation(dx, dy);
	        }
	      }
	    });
	}
	
}