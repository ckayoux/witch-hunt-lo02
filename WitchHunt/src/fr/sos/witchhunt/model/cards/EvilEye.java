package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public final class EvilEye extends RumourCard {
	/*TODO : this card becomes more valuable when you are unrevealed and there are exactly two other unrevealed players, 
	 * both being short of cards,
	 *and even more if you are a witch,
	 *as it allows you to choose the player right before you and thus let you accuse the last remaining player.*/
	public EvilEye () {
		this.witchEffect = new WitchEffect("Choose next player.\n"
				+ "/+/On their turn, they must accuse a player other than you, if possible.",2) {
			@Override
			public void perform() {
				Player me = getMyself();
				Player target = chooseNextPlayer();
				me.forceToAccuseNextTurn(target);
				
			}
		};
		
		this.huntEffect = new HuntEffect("Choose next player.\n"
				+ "/+/On their turn, they must accuse a player other than you, if possible.",1) {
			@Override
			public void perform() {
				Player me = getMyself();
				Player target = chooseNextPlayer();
				me.forceToAccuseNextTurn(target);
			}
			
		};
	}

}
