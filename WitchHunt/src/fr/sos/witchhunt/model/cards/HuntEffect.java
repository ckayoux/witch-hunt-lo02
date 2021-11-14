package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public abstract class HuntEffect extends Effect {
	public HuntEffect () {
		super("Choose next player.",1);
	}
	public HuntEffect (String desc,int value) {
		super(desc,value);
	}
	
	protected Player getMyself() {
		return Tabletop.getInstance().getHunter();
	}
	private Player getTarget() {
		return Tabletop.getInstance().getHuntedPlayer();
	}
	
}
