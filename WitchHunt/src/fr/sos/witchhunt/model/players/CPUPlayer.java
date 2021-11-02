package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.cpustrategies.*;

public final class CPUPlayer extends Player {
	
	private PlayStrategy chosenStrategy= new ExploringStrategy();
	
	public CPUPlayer(int id, int cpuNumberHowMuch) {
		super(id);
		this.name="CPU "+Integer.toString(cpuNumberHowMuch);
	}
	
	@Override
	public final void chooseIdentity() {
		this.identity = chosenStrategy.chooseIdentity();
		this.identityCard.setChosenIdentity(this.identity);
		displayObserver.passLog("\t"+this.name+" has chosen its identity.");
	}

	@Override
	public void accuse(Player p) {
		requestLog("\t\t!-- CPUPlayers can't choose a target to accuse yet. To be continued... --!");
	}
	@Override
	protected Player choosePlayerToAccuse() {
		return chosenStrategy.selectPlayerToAccuse();
	}
	
	@Override
	public TurnAction chooseTurnAction() {
		return chosenStrategy.chooseTurnAction();
	}

	@Override
	public DefenseAction chooseDefenseAction(boolean canWitch) {
		return chosenStrategy.chooseDefenseAction(canWitch);
	}

	
	@Override
	public void hunt() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected boolean canHunt() {
		return false; //TEMPORARY
	}
	
	@Override
	public boolean canWitch() {
		return false; //TEMPORARY
	}

	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		// TODO Auto-generated method stub
		return null;
	}


}
