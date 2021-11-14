package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
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
		this.identity = chosenStrategy.selectIdentity();
		this.identityCard.setChosenIdentity(this.identity);
		displayMediator.passLog("\t"+this.name+" has chosen its identity.");
	}

	@Override
	protected Player choosePlayerToAccuse() {
		return chosenStrategy.selectPlayerToAccuse(getAccusablePlayers());
	}
	
	@Override
	public Player chooseTarget(List<Player> eligiblePlayers) {
		return chosenStrategy.selectTarget(eligiblePlayers);
	}

	@Override
	public Player chooseNextPlayer() {
		return chosenStrategy.selectNextPlayer(Tabletop.getInstance().getActivePlayersList().stream().filter(p->p!=this).toList());
	}
	
	@Override
	public TurnAction chooseTurnAction() {
		return chosenStrategy.selectTurnAction();
	}

	@Override
	public DefenseAction chooseDefenseAction() {
		return chosenStrategy.selectDefenseAction(this.canWitch());
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
	public RumourCard selectWitchCard() {
		return this.chosenStrategy.selectWitchCard(this.hand.getPlayableWitchSubpile());
	}

	@Override
	protected RumourCard selectCardToDiscard() {
		if(this.hasUnrevealedRumourCards()) {
			return chosenStrategy.selectCardToDiscard(this.getUnrevealedSubhand());
		}
		else return chosenStrategy.selectCardToDiscard(this.hand);
	}

	@Override
	public RumourCard selectHuntCard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RumourCard chooseAnyCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		if(targetPileContainsCards(rcp)) {
			return chosenStrategy.selectBestCard(rcp,seeUnrevealedCards);
		}
		else return null;
	}
	
	@Override
	public RumourCard chooseRevealedCard(RumourCardsPile from) {
		if(targetPileContainsCards(from.getRevealedSubpile())) {
			return chosenStrategy.selectBestCard(from.getRevealedSubpile(),false);
		}
		else return null;
	}

	@Override
	public Identity lookAtPlayersIdentity(Player target) {
		Identity id = super.lookAtPlayersIdentity(target);
		/*Tout doux : remember the players identity and accuse him the next time
		 * not necessarely if it is a villager and there is another very suspicious and weak player,
		 * necessarely otherwise
		 */
		return id;
	}



}
