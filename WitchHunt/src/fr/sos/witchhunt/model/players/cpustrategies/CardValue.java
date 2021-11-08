package fr.sos.witchhunt.model.players.cpustrategies;

public final class CardValue {
	private int witchValue; //value attributed by a CPU player to the witch effect of a card
	private int huntValue; //value attributed by a CPU player to the hunt effect of a card
	private int additionnalValue; //value attributed by a CPU player to the potential additionnal value of a card
	
	public CardValue(int witchValue, int huntValue) {
		this.witchValue=witchValue;
		this.huntValue=huntValue;
		additionnalValue=0;
	}
	
	public CardValue(int witchValue, int huntValue, int additionnalValue) {
		this.witchValue=witchValue;
		this.huntValue=huntValue;
		this.additionnalValue=additionnalValue;
	}
	
	public int getOverallValue() {
		return this.witchValue + this.huntValue + this.additionnalValue;
	}
	public int getWitchValue() {
		return this.witchValue;
	}
	public int getAdditionnalValue() {
		return this.additionnalValue;
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
	public void setAdditionnalValue(int v) {
		this.additionnalValue=v;
	}
}
