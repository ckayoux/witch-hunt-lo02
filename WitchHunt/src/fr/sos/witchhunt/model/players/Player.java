package fr.sos.witchhunt.model.players;

import java.util.List;

import fr.sos.witchhunt.model.Menu;
import fr.sos.witchhunt.PlayerDisplayObservable;
import fr.sos.witchhunt.PlayerDisplayObserver;
import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Resettable;
import fr.sos.witchhunt.model.cards.IdentityCard;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

public abstract class Player implements PlayerDisplayObservable, Resettable {
	
	//ATTRIBUTES
	protected String name;
	protected int id;
	protected int score;
	protected RumourCardsPile hand;
	protected Identity identity;
	protected IdentityCard identityCard;
	protected PlayerDisplayObserver displayObserver;
	private boolean immunized; //the Rumour Card EvilEye can give immunity to accusation to one player for one turn
	private boolean active = true;
	
	//CONSTRUCTORS
	public Player(String name, int id) {
		if(name=="") {
			this.name="Player "+Integer.toString(id);
		}
		else {
			this.name=name;
			this.id=id;
		}
		this.identityCard = new IdentityCard();
		this.hand = new RumourCardsPile();
	}
	public Player(int id) {
		this.id=id;
		this.identityCard = new IdentityCard();
		this.hand = new RumourCardsPile();
	}
	
	//GAME ACTIONS METHODS
	public abstract void chooseIdentity();;
	public Identity revealIdentity() {
		requestIdentityRevealScreen();
		this.identityCard.reveal();
		return this.identity;
	}
	
	public void takeRumourCard(RumourCard rc) {
		hand.addCard(rc);
	}
	
	public abstract TurnAction chooseTurnAction();
	
	public void playTurn() {
		requestPlayTurnScreen();
		switch(chooseTurnAction()) {
			case ACCUSE:
				accuse();
				break;
			case HUNT:
				hunt();
				break;
		}
		
	}
	
	protected void accuse() {
		accuse(choosePlayerToAccuse());
		clearImmunities();
	}
	
	protected abstract Player choosePlayerToAccuse();
	
	public void accuse(Player p) {
		requestAccusationScreen(p);
		switch(p.defend()) {
			case VILLAGER:
				this.addScore(1);
				break;
			case WITCH:
				eliminate(p);
				this.addScore(2);
				break;
		}
	}
	
	
	public abstract DefenseAction chooseDefenseAction(boolean canWitch);
	
	public Identity defend() {
		switch(chooseDefenseAction(this.canWitch())) {
			case WITCH:
				this.witch();
				return null; //every witch effect protects the accused player's identity. null is always returned
			case REVEAL:
				return this.revealIdentity(); //accused players reveal their identity and return it 
		}
		return null;
	}
	
	public abstract RumourCard selectWitchCard(RumourCardsPile rcp);
	
	protected void witch () {
		requestLog("\t\t!-- This functionnality is not avaliable yet --!");
		//TO UNCOMMENT WHEN PLAYERS ARE ABLE TO WITCH
		//selectWitchCardFrom(hand).witch();
	}
	
	protected abstract void hunt();
	
	public void reset() {
		this.identity = null;
		this.identityCard.reset();
		this.active = true;
	}
	
	//DISPLAY METHODS
	@Override
	public void requestLog(String msg) {
		displayObserver.passLog(msg);
	}
	
	@Override
	public void requestPlayTurnScreen() {
		displayObserver.displayPlayTurnScreen(name);
	}
	
	@Override
	public void requestAccusationScreen(Player p) {
		displayObserver.displayAccusationScreen(this,p);
	}	
	
	@Override
	public void requestDisplayPossibilities(Menu m) {
		displayObserver.displayPossibilities(m);
	}
	
	@Override
	public void requestForcedToRevealScreen() {
		displayObserver.displayForcedToRevealScreen();
	}
	
	@Override
	public void requestIdentityRevealScreen() {
		displayObserver.displayIdentityRevealScreen(this);
	}
	
	@Override
	public void requestScoreUpdateScreen(int scoreUpdatedBy) {
		displayObserver.displayScoreUpdateScreen(this,scoreUpdatedBy);
	}
	
	@Override
	public void requestEliminationScreen(Player victim) {
		displayObserver.displayEliminationScreen(this,victim);
	}
	
	//GETTERS
	public String getName() {
		return this.name;
	}
	public RumourCardsPile getHand() {
		return this.hand;
	}
	public Identity getIdentity() {
		return this.identity;
	}
	public IdentityCard getIdentityCard() {
		return this.identityCard;
	}
	public boolean isRevealed() {
		return this.identityCard.isRevealed();
	}
	public int getScore() {
		return this.score;
	}
	public boolean isImmunized() {
		return this.immunized;
	}
	public boolean isAccusable() {
		if(!this.isRevealed() && !this.immunized) return true;
		else return false;
	}
	public boolean isActive() {
		return this.active;
	}
	
	//SETTERS
	public void addScore(int pts) {
		this.score += pts;
		requestScoreUpdateScreen(pts);
	}
	
	public void immunize() {
		this.immunized = true;
	}
	
	public void looseImmunity() {
		this.immunized = false;
	}
	private void clearImmunities() {
		if (!this.immunized) Tabletop.getInstance().getPlayersList().forEach(p -> {if(p.isImmunized()) p.looseImmunity();});
		/*is called at the end of the accuse method.
		if the accusator is not the player who immunized himself,
		he will remove everyone's immunity afterwhile - because this bonus is only one-use.*/
		this.immunized = false;
	}
	
	public void eliminate() {
		this.active = false;
	}
	
	private void eliminate(Player victim) {
		victim.eliminate();
		requestEliminationScreen(victim);
	}
	
	@Override
	public void setDisplayObserver(PlayerDisplayObserver dO) {
		this.displayObserver=dO;
	}
	
	//UTILS
	public List<Player> getAccusablePlayers() {
		List <Player> l = Tabletop.getInstance().getAccusablePlayersList();
		l.remove(this);
		return l;
	}
	
	protected boolean canHunt() {
		return true; //TEMPORARY
	}
	
	public boolean canWitch() {
		return true; //TEMPORARY
	}
}
