package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.players.Player;

public final class Cauldron extends RumourCard {
	
	//done, must test
	public Cauldron() {
		this.isRisked=true;
		this.isOffensive = true;
			
		this.witchEffect = new WitchEffect("The player who accused you discards a random card from their hand.\n"
				+ "/+/Take next turn.",2) {
			@Override
			public void perform() {
				Tabletop.getInstance().getAccusator().discardRandomCard();
				takeNextTurn();
			}
		};
			
		this.huntEffect = new HuntEffect("Reveal your identity\n" //value augments when identity is already revealed
				+ "/+/W -> Player to your left takes next turn.\n"
				+ "/+/V -> Choose next player.",-1) {
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
							Tabletop.getInstance().getCurrentRound().getNextPlayer().requestBeHuntedScreen();
							me.eliminate();
							me.requestEliminationScreen(me);
							break;
						
						case VILLAGER: 
							chooseNextPlayer();
							break;
					}
				}
				
			};
			
		}


}
