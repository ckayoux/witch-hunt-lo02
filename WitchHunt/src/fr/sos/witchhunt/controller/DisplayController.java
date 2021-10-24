package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.DisplayObserver;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements DisplayObserver {
	
	private StdView console;
	
	public DisplayController(StdView stdv) {
		this.console=stdv;
	}
	
	public void displayMenu(Menu m) {
		console.makeMenu(m);
		//T0D0 : makeMenu for GUI view
	}
	
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}
		
}
