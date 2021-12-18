package fr.sos.witchhunt.view.gui;

import fr.sos.witchhunt.InputMediator;

public final class GUIView {

	private InputMediator inputMediator;
	private Window w; 
	
	//CONSTRUCTOR
	public GUIView (InputMediator im) {
		this.inputMediator=im;
		this.w = new Window ();
		//w.setContentPane(new GamePanel(im));
	}
}
