package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class Cauldron extends RumourCard {
	//TODO : default value ?
	public Cauldron() {
			
			this.witchEffect = new Effect() {
				@Override
				public void perform() {
					Tabletop.getInstance().getAccusator().discardRandomCard();
					takeNextTurn();
				}
			};
			
			this.huntEffect = new Effect() {
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
