package fr.sos.witchhunt.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.ActionButton;

public class ActionsPanelController implements InputSource {
	
	private InputMediator inputMediator;
	private List<ActionButton> bList;

	
	public ActionsPanelController(List<ActionButton> bList,InputMediator im) {
		this.inputMediator=im;
		this.bList=bList;
		for(int i=0;i<bList.size(); i++) {
			ActionButton b = bList.get(i);
			final int choice = i;
			b.addActionListener( new ActionListener (){
				@Override
				public void actionPerformed(ActionEvent e) {
					post(Integer.toString(choice+1));
				}
			});
		}
	}

	public void destroyMouseListeners() {
		bList.forEach(b->
		{
			for(ActionListener al : b.getActionListeners()) {
				b.removeActionListener(al);
			}
		});
	}
	
	@Override
	public void post(String str) {
		inputMediator.receive(str);
	}

	@Override
	public void post() {
		inputMediator.receive();
	}

	@Override
	public void post(Player p) {

	}
}
