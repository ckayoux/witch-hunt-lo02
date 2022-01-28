package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public final class TheInquisition extends RumourCard {
	//done, must test
	private TheInquisition cardInstance;
	
	public TheInquisition () {
		this.isOffensive=true;
		cardInstance = this;
		
		this.witchEffect = new WitchEffect("Discard a card from your hand.\n"
				+ "/+/Take next turn.",1) {
			
			@Override
			public void perform() {
				Player me = getMyself();
				if(!me.getHand().getCards().stream().filter(c->c!=cardInstance).toList().isEmpty()) {
					me.discard(new RumourCardsPile(
							me.getHand().getCards().stream().filter(c->c!=cardInstance).toList(),me
							)); //select a card to discard from your hand, except this one itselfs
					//we assume that we can discard a revealed card
				}
				else {
					me.discard();
				}		
				takeNextTurn();
			}
		};
		
		
		this.huntEffect = new HuntEffect("(Only playable if you've been revealed as a Villager)\n"
				+ "/+/Choose next player.\n"
				+ "/+/Before their turn, secretly look at their identity.", 1) {
			Player me;
			@Override
			public void perform() {
				me=getMyself();
				Player target = chooseNextPlayer();
				Tabletop.getInstance().setHuntedPlayer(target);
				me.lookAtPlayersIdentity(target);
			}
			
			@Override
			public boolean isAllowed() {
				me=getMyself();
				//only playable if you have been revealed as a villager
				return (me.isRevealed() && me.getIdentity()==Identity.VILLAGER);
			}
			
		};
	}

}
