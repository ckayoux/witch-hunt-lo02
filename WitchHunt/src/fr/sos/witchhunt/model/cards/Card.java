package fr.sos.witchhunt.model.cards;

import java.awt.image.BufferedImage;

import fr.sos.witchhunt.model.Resettable;

public abstract class Card implements Resettable {
	protected boolean revealed=false;
	
	public void reveal() {
		this.revealed=true;
	}
	
	@Override
	public void reset() {
		this.revealed=false;
	}
	
	//GETTERS
	public boolean isRevealed() {
		return this.revealed;
	}
	
	
	public static final BufferedImage getUnrevealedCardImage () {
		//TODO
		return null;
	}
}
