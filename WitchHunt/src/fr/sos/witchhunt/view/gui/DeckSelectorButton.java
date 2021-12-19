package fr.sos.witchhunt.view.gui;

import java.awt.Insets;

import javax.swing.JButton;

import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;

public class DeckSelectorButton extends JButton {
	
	private Player associatedPlayer;
	private RumourCardsPile associatedDeck;
	private Insets defaultMargin = new Insets(10, 10, 10, 10);
	
	public DeckSelectorButton(Player p) {
		super(p.getName());
		this.associatedPlayer=p;
		this.associatedDeck=p.getHand();
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setMargin(defaultMargin);
	}
	
	public DeckSelectorButton(RumourCardsPile pile) {
		super("Discarded cards");
		this.associatedPlayer=null;
		this.associatedDeck = pile;
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setMargin(defaultMargin);
	}
	
	
	public Player getAssociatedPlayer () {
		return this.associatedPlayer;
	}
	
	public RumourCardsPile getDeck () {
		return this.associatedDeck;
	}
}
