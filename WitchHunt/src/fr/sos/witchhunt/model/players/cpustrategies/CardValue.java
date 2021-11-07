package fr.sos.witchhunt.model.players.cpustrategies;

public final class CardValue {
	private int witchValue;
	private int huntValue;
	
	public CardValue(int witchValue, int huntValue) {
		this.witchValue=witchValue;
		this.huntValue=huntValue;
	}
	
	
	public int getOverallValue() {
		return this.witchValue+this.huntValue;
	}
	public int getWitchValue() {
		return this.witchValue;
	}
	public int getHuntValue() {
		return this.huntValue;
	}

	
	public void setHuntValue(int v) {
		this.huntValue=v;
	}
	public void setWitchValue(int v) {
		this.witchValue=v;
	}
}
