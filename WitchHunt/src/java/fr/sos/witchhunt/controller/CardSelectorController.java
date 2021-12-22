package fr.sos.witchhunt.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.view.InputSource;
import fr.sos.witchhunt.view.gui.GamePanel;
import fr.sos.witchhunt.view.gui.RenderedCard;

public class CardSelectorController implements InputSource {
	
	private InputMediator inputMediator=null;
	private List<RenderedCard> jList;
	private Map<RenderedCard,MouseListener> thisControllersMLMap = new HashMap<RenderedCard,MouseListener>();
	
	public CardSelectorController(List<RenderedCard> jList,GamePanel gp) {
		this.jList=jList;
		for(int i=0; i<jList.size(); i++) {
			RenderedCard j = jList.get(i);
			final int choice = i;
			j.addMouseListener(new MouseListener() {
	
				@Override
				public void mouseClicked(MouseEvent e) {
					gp.setSelectedCard(j);
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
				}
	
				@Override
				public void mouseReleased(MouseEvent e) {
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
			});
		}
	}
	
	public CardSelectorController(List<RenderedCard> jList,GamePanel gp,InputMediator im) {
		this.jList=jList;
		this.inputMediator=im;
		for(int i=0; i<jList.size(); i++) {
			RenderedCard j = jList.get(i);
			final int choice = i;
			
			MouseListener ml = new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//if(gp.getSelectedCard()!=j) {
						post(Integer.toString(choice+1));
						gp.cardHasBeenChosen(j);
						destroyThisControllersMouseListeners();
				//	}
				}
	
				@Override
				public void mousePressed(MouseEvent e) {
				}
	
				@Override
				public void mouseReleased(MouseEvent e) {
				}
	
				@Override
				public void mouseEntered(MouseEvent e) {
				}
	
				@Override
				public void mouseExited(MouseEvent e) {
				}
				
			};
			
			this.thisControllersMLMap.put(j, ml);
			j.addMouseListener(ml);
			
		}
	}
	
	public void destroyAllMouseListeners() {
		this.jList.forEach(j->{
			for(MouseListener ml : j.getMouseListeners()) {
				j.removeMouseListener(ml);
			}
		});
	}

	private void destroyThisControllersMouseListeners() {
		this.thisControllersMLMap.keySet().forEach(j->{
			j.removeMouseListener(thisControllersMLMap.get(j));
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
	
}
