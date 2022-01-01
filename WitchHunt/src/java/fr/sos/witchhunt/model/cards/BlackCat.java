package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;

public final class BlackCat extends RumourCard {
	
	public BlackCat() {
		RumourCard cardInstance=this;
		this.givesCards=true;
		
		this.witchEffect = new WitchEffect() {
			@Override
			public void perform() {
				takeNextTurn();
			}
		};
		
		this.huntEffect = new HuntEffect("Add one discarded card to your hand, and then discard this card.\n"
				+ "/+/Take next turn.",0) { //value augments when there is at least 1 card in the pile, even more if there is one revealed card with overall value>=3 in the pile
			

			@Override
			public void perform() {
				RumourCardsPile pile=Tabletop.getInstance().getPile();
				Player me = getMyself();
				RumourCard chosen = me.chooseAnyCard(pile,false);
				if(chosen != null) { //the pile may contain no cards
					chosen.reset();
					me.takeRumourCard(chosen, pile);
					me.requestHasChosenCardScreen(chosen,pile,false);
				}
				me.discard(cardInstance);
				takeNextTurn();
				
			}
			
		};
		
	}

}
