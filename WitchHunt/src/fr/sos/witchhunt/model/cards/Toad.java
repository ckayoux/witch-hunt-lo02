package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class Toad extends RumourCard {
	
	public Toad () {
		this.isRisked=true;
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect("Reveal your identity\n"
				+ "/+/W -> Player to your left takes next turn.\n"
				+ "/+/V -> Choose next player.",0) {
			//same effect as Toad
			/*this card must gain value if you are : 
			 * an unrevealed villager with other hunt cards that can be played only by villagers,
			 * if this is your last card and the other players have averagely more than 1 card - so at least you dont give them points when they accuse you
			 * if you are already revealed 
			 */
			@Override
			public void perform() {
				Player me = getMyself();
				switch(me.revealIdentity()) {
					case WITCH:
						Tabletop.getInstance().getCurrentRound().setNextPlayerCounterclockwise();
						me.eliminate();
						me.requestEliminationScreen(me);
						break;
					
					case VILLAGER: 
						me.chooseNextPlayer();
						break;
				}
			}
			
		};

	}

}
