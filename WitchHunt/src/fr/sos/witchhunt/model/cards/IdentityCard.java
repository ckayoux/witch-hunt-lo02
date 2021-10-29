package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.Identity;

public class IdentityCard extends Card {
	private Identity chosenIdentity;
	
	public void setChosenIdentity(Identity i) {
		this.chosenIdentity = i;
	}
}
