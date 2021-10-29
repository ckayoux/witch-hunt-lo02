package fr.sos.witchhunt.model.cards;

public abstract class RumourCard extends Card {
	int value;
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
}
