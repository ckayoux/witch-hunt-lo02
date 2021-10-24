package fr.sos.witchhunt.controller;

import java.util.Scanner;

import fr.sos.witchhunt.InputObserver;
import fr.sos.witchhunt.view.Menu;
import fr.sos.witchhunt.view.std.StdView;

public final class InputController implements InputObserver {

	//ATTRIBUTES
	StdView console;
	
	//CONSTRUCTORS
	public InputController(StdView stdv) {
		this.console=stdv;
	}
	
	public int makeChoice(Menu m) {
		int choice = console.selectOption(m);
		//TODO : interruptable console input in separate thread.
		return choice;
	}
}
