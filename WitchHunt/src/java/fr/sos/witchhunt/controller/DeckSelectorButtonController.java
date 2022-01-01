package fr.sos.witchhunt.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.sos.witchhunt.view.gui.scenes.game.DeckSelectorButton;
import fr.sos.witchhunt.view.gui.scenes.game.GamePanel;

public class DeckSelectorButtonController {

	public DeckSelectorButtonController(DeckSelectorButton b,GamePanel gp) {
		b.addActionListener( new ActionListener (){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(gp.getSelectedDeckButton()!=b) gp.setSelectedDeckButton(b);
				else gp.setSelectedDeckButton(null);
			}
		});
	}


}
