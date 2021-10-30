package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.Identity;

public class IdentityCard extends Card {
	private Identity chosenIdentity;
	
	@Override
	public void reset() {
		super.reset();
		this.chosenIdentity=null;
	}
	
	public void setChosenIdentity(Identity i) {
		this.chosenIdentity = i;
	}
}
