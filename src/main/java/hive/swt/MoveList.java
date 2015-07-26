package hive.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import hive.engine.IController;
import hive.engine.IMoveHistoryListener;
import hive.engine.Move;

public class MoveList {

	final List list;
	
	MoveList(Shell shell, IController ctl) {
		list = new List(shell, SWT.BORDER | SWT.V_SCROLL);
		GridData data = new GridData();
		data.horizontalAlignment = SWT.FILL;
		data.verticalAlignment = SWT.FILL;
		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;
		data.minimumWidth = 200;
		list.setLayoutData(data);

		ctl.addMoveHistoryListener(new IMoveHistoryListener() {
			@Override
			public void selectionChanged(int index) {
				list.select(index);
			}
			
			@Override
			public void moveAdded(Move move) {
				addToEnd(move);
			}
			
			@Override
			public void listUpdated(java.util.List<Move> moves) {
				populate(moves);
			}
		});

		list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ctl.onMoveSelectedFromHistory(list.getSelectionIndex());
			}
		});

	}
	
	void addToEnd(Move m) {
		list.add(m.piece.color() + ": " + m);
	}

	void populate(java.util.List<Move> moves) {
		list.removeAll();
		list.add("Start game");

		for (Move move : moves) {
			addToEnd(move);
		}
	}

}
