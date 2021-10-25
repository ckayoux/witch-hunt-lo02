package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.model.cards.IdentityCard;

import fr.sos.witchhunt.model.players.cpustrategies.*;

public final class CPUPlayer extends Player {
	
	private PlayStrategy chosenStrategy= new ExploringStrategy();
	
	public CPUPlayer(int id) {
		super(id);
		this.name="CPU "+Integer.toString(id);
	}
	
	public final void chooseIdentity() {
		super.chooseIdentity();
		this.identity = chosenStrategy.chooseIdentity();
		displayObserver.passLog("\t"+this.name+" has chosen its identity.");
	}
}
