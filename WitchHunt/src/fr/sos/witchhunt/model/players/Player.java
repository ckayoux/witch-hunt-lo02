package fr.sos.witchhunt.model.players;

import fr.sos.witchhunt.DisplayObservable;
import fr.sos.witchhunt.DisplayObserver;

public abstract class Player implements DisplayObservable {
	
	//ATTRIBUTES
	protected String name;
	protected int id;
	protected int score;
	protected DisplayObserver displayObserver;
	
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
	
	//DISPLAY METHODS
	@Override
	public void requestLog(String msg) {
		displayObserver.passLog(msg);
	}
	
	//SETTERS
	public void setDisplayObserver(DisplayObserver dO) {
		this.displayObserver=dO;
	}
	public String getName() {
		return this.name;
	}
	public int getScore() {
		return this.score;
	}
	
}
