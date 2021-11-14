package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public abstract class WitchEffect extends Effect {

	
	public WitchEffect (String desc,int value) {
		super(desc,value);
	}
	public WitchEffect () {
		super("Take next turn.",1);
	}
	
	protected Player getMyself() {
		return Tabletop.getInstance().getAccusedPlayer();
	}
}
