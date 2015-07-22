package hive.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import hive.engine.IController;

public class MainMenu {

	MainMenu(Shell shell, IController control) {
		Menu menuBar = new Menu(shell, SWT.BAR);
		
		MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
	    fileMenuHeader.setText("&File");

	    Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
	    fileMenuHeader.setMenu(fileMenu);

	    MenuItem loadGameItem = new MenuItem(fileMenu, SWT.PUSH);
	    loadGameItem.setText("&Load game");

	    MenuItem saveGameItem = new MenuItem(fileMenu, SWT.PUSH);
	    saveGameItem.setText("&Save game");

	    shell.setMenuBar(menuBar);
	    
	    loadGameItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell, SWT.OPEN);
				String filename = fd.open(); 
				if(filename != null) {
					control.loadPosition(filename);
				}
			}
		});

	    saveGameItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(shell, SWT.SAVE);
				String filename = fd.open(); 
				if(filename != null) {
					control.savePosition(filename);
				}
			}
		});
}
}
