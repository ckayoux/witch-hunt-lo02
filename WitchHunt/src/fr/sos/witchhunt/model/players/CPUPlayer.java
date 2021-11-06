package fr.sos.witchhunt.model.players;

import java.util.List;

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
	protected Player choosePlayerToAccuse() {
		return chosenStrategy.selectPlayerToAccuse(getAccusablePlayers());
	}
	
	@Override
	public Player chooseTarget(List<Player> eligiblePlayers) {
		return chosenStrategy.chooseTarget(eligiblePlayers);
	}

	@Override
	public Player chooseNextPlayer() {
		// TODO
		return null;
	}
	
	@Override
	public TurnAction chooseTurnAction() {
		return chosenStrategy.chooseTurnAction();
	}

	@Override
	public DefenseAction chooseDefenseAction() {
		return chosenStrategy.chooseDefenseAction(this.canWitch());
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
