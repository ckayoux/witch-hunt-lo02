package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.DisplayObserver;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.StdView;

public final class DisplayController implements DisplayObserver {
	
	private StdView console;
	
	
	public void displayMenu(Menu m) {
		console.makeMenu(m);
		//T0D0 : makeMenu for GUI view
	}
	public void displayYesNoQuestion(String q) {
		console.yesNoQuestion(q);
		//T0D0 : makeMenu for GUI view
	}
	
	public void drawHardLine() {
		console.logHardLine();
	}
	public void drawDashedLine() {
		console.logDashedLine();
	}
	
	@Override
	public void passLog(String msg) {
		console.log(msg);
	}
	
	public void crlf() {
		console.crlf();
	}
	
	public void setConsole(StdView console) {
		this.console=console;
	}
}
