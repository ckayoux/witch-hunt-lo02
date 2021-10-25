package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.DisplayObservable;
import fr.sos.witchhunt.PlayerDisplayObserver;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.IdentityCard;

public abstract class Player implements DisplayObservable {
	
	//ATTRIBUTES
	protected String name;
	protected int id;
	protected int score;
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
	}
	public Player(int id) {
		this.id=id;
	}
	
	//GAME ACTIONS METHODS
	public void playTurn() {
		requestLog("\t"+this.name + " : it's my turn !");
	}
	public void chooseIdentity() {
		this.identityCard = new IdentityCard();
	};
	
	//DISPLAY METHODS
	@Override
	public void requestLog(String msg) {
		displayObserver.passLog(msg);
	}
	
	//GETTERS
	public String getName() {
		return this.name;
	}
	public int getScore() {
		return this.score;
	}
	public boolean canHunt() {
		return true; //TEMPORARY
	}
	
	//SETTERS
	public void setDisplayObserver(PlayerDisplayObserver dO) {
		this.displayObserver=dO;
	}
	
}
