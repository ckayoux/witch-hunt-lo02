package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public abstract class RumourCard extends Card {
	protected int value;
	protected Effect witchEffect;
	protected Effect huntEffect;
	
	//public void discard(){}
	public boolean witch() {
		if(this.witchEffect.isAllowed()) {
			this.witchEffect.perform();
			return true;
		}
		else {
			return false;
		}
	};
	public boolean hunt() {
		if(this.huntEffect.isAllowed()) {
			this.huntEffect.perform();
			return true;
		}
		else {
			return false;
		}
	}
	public CardValue getDefaultValue() {
		return new CardValue(witchEffect.getValue(),huntEffect.getValue());
	};
}
