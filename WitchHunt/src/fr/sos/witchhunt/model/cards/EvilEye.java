package fr.sos.witchhunt.model.cards;

public final class EvilEye extends RumourCard {
	//TODO : default value ?
	public EvilEye () {
		this.witchEffect = new WitchEffect() {
			
			@Override
			public void perform() {
				//on their turn, they must accuse a player other than you, if possible
				chooseNextPlayer();
			}
		};
		
		this.huntEffect = new HuntEffect() {
			@Override
			public void perform() {
				//on their turn, they must accuse a player other than you, if possible
				chooseNextPlayer();
			}
			
		};
	}

}
