package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.DisplayObservable;
import fr.sos.witchhunt.PlayerDisplayObserver;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.Resettable;
import fr.sos.witchhunt.model.cards.IdentityCard;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;

public abstract class Player implements DisplayObservable, Resettable {
	
	//ATTRIBUTES
	protected String name;
	protected int id;
	protected int score;
	protected RumourCardsPile hand;
	protected Identity identity;
	protected IdentityCard identityCard;
	protected PlayerDisplayObserver displayObserver;
	
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
	}
	public Player(int id) {
		this.id=id;
	}
	
	//GAME ACTIONS METHODS
	public void playTurn() {
		requestLog("\t"+this.name + " : it's my turn !");
	}
	public abstract void chooseIdentity();;
	public Identity revealIdentity() {
		this.identityCard.reveal();
		return this.identity;
	}
	public void takeRumourCard(RumourCard rc) {
		hand.addCard(rc);
	}
	
	public void reset() {
		this.identity = null;
		this.identityCard.reset();
	}
	
	//DISPLAY METHODS
	@Override
	public void requestLog(String msg) {
		displayObserver.passLog(msg);
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
	public boolean canHunt() {
		return true; //TEMPORARY
	}
	
	//SETTERS
	@Override
	public void setDisplayObserver(PlayerDisplayObserver dO) {
		this.displayObserver=dO;
	}
}
