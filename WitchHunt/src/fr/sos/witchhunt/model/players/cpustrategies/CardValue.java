package fr.sos.witchhunt.model.players.cpustrategies;

public final class CardValue {
	private int witchValue; //value attributed by a CPU player to the witch effect of a card
	private int huntValue; //value attributed by a CPU player to the hunt effect of a card
	private int additionnalValue; //value attributed by a CPU player to the potential additionnal value of a card
	private boolean protects=false;
	private boolean isDecisive=false;
	private boolean isRisked=false;
	private boolean locked=false;
	private boolean givesCards=false;
	
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
	public boolean protects() {
		return this.protects;
	}
	public boolean isDecisive() {
		return this.isDecisive;
	}
	public boolean isRisked() {
		return this.isRisked;
	}
	public boolean givesCards() {
		return this.givesCards;
	}

	
	public void setHuntValue(int v) {
		this.huntValue=v;
	}
	public void setWitchValue(int v) {
		if(!locked) this.witchValue=v;
	}
	public void setAdditionnalValue(int v) {
		if(!locked) this.additionnalValue=v;
	}
	public void addHuntValue(int v) {
		if(!locked) this.huntValue+=v;
	}
	public void addWitchValue(int v) {
		if(!locked) this.witchValue+=v;
	}
	public void addAdditionnalValue(int v) {
		if(!locked) this.additionnalValue+=v;
	}
	public void setProtects(boolean protects) {
		if(!locked) {
			if(!this.protects&&protects==true) this.additionnalValue++;
			else if(this.protects&&protects==false) this.additionnalValue--;
			this.protects=protects;
		}
		
	}
	public void setRisked(boolean risked) {
		if(!locked) {
			if(!this.isRisked&&risked==true) this.additionnalValue++;
			else if(this.isRisked&&risked==false) this.additionnalValue--;
			this.isRisked=risked;
		}
		
	}
	public void setGivesCards(boolean does) {
		if(!locked) {
			this.givesCards=does;
		}
		
	}
	
	public void setDecisive(boolean decisive) {
		if (!locked) this.isDecisive=decisive;
	}
	public void lock () {
		this.locked=true;
	}
	
	public void unlock () {
		this.locked=false;
	}
}
