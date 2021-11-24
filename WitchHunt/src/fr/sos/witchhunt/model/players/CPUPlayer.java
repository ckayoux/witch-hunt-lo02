package fr.sos.witchhunt.model.players;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.cpustrategies.*;

public final class CPUPlayer extends Player {
	
	private PlayStrategy chosenStrategy=new DefensiveStrategy();// new GropingStrategy();
	private Player knownWitch=null;
	
	public CPUPlayer(int id, int cpuNumberHowMuch) {
		super(id);
		this.name="CPU "+Integer.toString(cpuNumberHowMuch);
	}
	
	@Override
	public void playTurn() {
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		super.playTurn();		
	}
	
	@Override
	public final void chooseIdentity() {
		this.identity = chosenStrategy.selectIdentity();
		this.identityCard.setChosenIdentity(this.identity);
		displayMediator.passLog("\t"+this.name+" has chosen its identity.");
	}

	@Override
	protected Player choosePlayerToAccuse() {
		if(knownWitch==null) return chosenStrategy.selectPlayerToAccuse(getAccusablePlayers());
		else {
			Player output = knownWitch;
			this.knownWitch=null;
			return output;
		}
	}
	
	@Override
	public Player chooseTarget(List<Player> eligiblePlayers) {
		if(knownWitch==null||!eligiblePlayers.contains(knownWitch)) return chosenStrategy.selectTarget(eligiblePlayers);
		else return knownWitch;
		
	}

	@Override
	public Player chooseNextPlayer() {
		return chosenStrategy.selectNextPlayer(Tabletop.getInstance().getActivePlayersList().stream().filter(p->p!=this).toList());
	}
	
	@Override
	public TurnAction chooseTurnAction() {
		return chosenStrategy.selectTurnAction(this.identity,this.hand,this.canHunt());
	}

	@Override
	public DefenseAction chooseDefenseAction() {
		//must initialize Tabletop's hunter with canHunt, as the player will look at its playable hunt cards to make their decision
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		return chosenStrategy.selectDefenseAction(this.canWitch(),this.hand,this.getIdentity());
	}

	@Override
	public void beHunted() {
		super.beHunted();
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
	}
	@Override
	public RumourCard selectWitchCard() {
		return this.chosenStrategy.selectWitchCard(this.hand.getPlayableWitchSubpile());
	}

	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile in) {
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(this.hasUnrevealedRumourCards()) {
			return chosenStrategy.selectCardToDiscard(in.getUnrevealedSubpile());
		}
		else return chosenStrategy.selectCardToDiscard(in);
	}

	@Override
	public RumourCard selectHuntCard() {
		return this.chosenStrategy.selectHuntCard(this.hand.getPlayableHuntSubpile());
	}

	@Override
	public RumourCard chooseAnyCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(targetPileContainsCards(rcp)) {
			return chosenStrategy.selectBestCard(rcp,seeUnrevealedCards);
		}
		else return null;
	}
	
	@Override
	public RumourCard chooseRevealedCard(RumourCardsPile from) {
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(targetPileContainsCards(from.getRevealedSubpile())) {
			return chosenStrategy.selectBestCard(from.getRevealedSubpile(),false);
		}
		else return null;
	}

	@Override
	public Identity lookAtPlayersIdentity(Player target) {
		Identity id = super.lookAtPlayersIdentity(target);
		if(id==Identity.WITCH) this.knownWitch=target;
		return id;
	}

	@Override
	public DefenseAction revealOrDiscard() {
		chosenStrategy.updateBehavior(this.isRevealed(),this.identity,this.hand);
		if(this.hasRumourCards()&&!this.isRevealed()) {
			return chosenStrategy.revealOrDiscard(this.getIdentity(),this.getHand());
		}
		else if(!this.hasRumourCards()&&!this.isRevealed()) {
			requestNoCardsScreen();
			requestForcedToRevealScreen();
			return DefenseAction.REVEAL;
		}
		else { //cannot be chosen by ducking stool if is revealed and has no rumour cards
			return DefenseAction.DISCARD;
		}
	}


	public void chooseStrategy() {
		
	}
}
