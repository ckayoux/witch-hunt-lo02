package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class Toad extends RumourCard {
	//TODO : default value ?
	
	public Toad () {
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect() {
			//same effect as Toad
			@Override
			public void perform() {
				/*TODO
				 * me = Tabletop.getInstance().getHunter();
				 *Reveal your identity switch(me.revealIdentity())
				 *case WITCH: player to your left takes next turn. //DO WE HAVE TO ELIMINATE OURSELVES OR DO WE REMAIN ACTIVE ?
				 *case VILLAGER: me.chooseNextPlayer();
				 */
			}
			
		};

	}

}
