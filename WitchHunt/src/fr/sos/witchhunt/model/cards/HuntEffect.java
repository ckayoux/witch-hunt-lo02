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
	
	@Override
	protected Player getMyself() {
		return Tabletop.getInstance().getHunter();
	}
	private Player getTarget() {
		return Tabletop.getInstance().getHuntedPlayer();
	}
	
	@Override
	public Player chooseNextPlayer() {
		Player nextPlayer = getMyself().chooseNextPlayer();
		nextPlayer.beHunted();
		nextPlayer.takeNextTurn();
		return nextPlayer;
	}
	
}
