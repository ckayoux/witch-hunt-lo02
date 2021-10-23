package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.DisplayObserver;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements DisplayObserver {
	
	private StdView console;
	
	public DisplayController(StdView manages) {
		this.console=manages;
	}
	
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}	
}
