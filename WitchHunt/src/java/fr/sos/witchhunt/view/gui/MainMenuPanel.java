package fr.sos.witchhunt.view.gui;

import java.awt.Dimension;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;

public class MainMenuPanel extends GridBagPanel{
	private InputMediator inputMediator;

	private ChoicesPanel choicesPanel;
	
	public MainMenuPanel() {
		super(1,1);
		this.choicesPanel = new ChoicesPanel(0,0,1,1,defaultCellBorder,cellsList);
		
		buildCustomGridBag();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(Window.HEIGHT,Window.WIDTH);
	}

	public void makeChoice(Menu m) {
		this.choicesPanel.makeChoice(m, inputMediator);
	}
	

	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
		
	}

	public void displayMenu(Menu m) {
		choicesPanel.displayMenu(m);
	}

	public void resetChoicesPanel() {
		choicesPanel.resetPane();
	}
}

