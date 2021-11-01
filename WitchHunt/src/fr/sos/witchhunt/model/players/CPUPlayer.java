package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.cpustrategies.*;

public final class CPUPlayer extends Player {
	
	private PlayStrategy chosenStrategy= new ExploringStrategy();
	
	public CPUPlayer(int id) {
		super(id);
		this.name="CPU "+Integer.toString(id);
	}
	
	@Override
	public final void chooseIdentity() {
		this.identity = chosenStrategy.chooseIdentity();
		this.identityCard.setChosenIdentity(this.identity);
		displayObserver.passLog("\t"+this.name+" has chosen its identity.");
	}

	@Override
	protected Player choosePlayerToAccuse() {
		return chosenStrategy.selectPlayerToAccuse();
	}
	
	@Override
	public Identity defend() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void hunt() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TurnAction chooseTurnAction() {
		return chosenStrategy.chooseTurnAction();
	}
	

}
