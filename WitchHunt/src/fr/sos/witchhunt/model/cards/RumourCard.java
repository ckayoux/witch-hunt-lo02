package fr.sos.witchhunt.model.cards;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

public abstract class RumourCard extends Card {
	protected int value;
	protected Effect witchEffect;
	protected Effect huntEffect;
	protected String additionnalEffectDescription="";
	
	//public void discard(){}
	public boolean witch() {
		if(this.witchEffect.isAllowed()) {
			this.reveal();
			this.witchEffect.perform();
			return true;
		}
		else {
			return false;
		}
	};
	public boolean hunt() {
		if(this.huntEffect.isAllowed()) {
			this.reveal();
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
	
	public boolean grantsImmunityAgainst(RumourCard rc) {
		return false;
	}
	
	public boolean canWitch() {
		return (this.witchEffect.isAllowed() && !this.revealed);
	}
	
	public boolean canHunt() {
		return (this.huntEffect.isAllowed() && !this.revealed);
	}
	
	public String getName() {
		//converts CardName into Card Name automatically
		String cardClassName = this.getClass().getSimpleName();
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<cardClassName.length(); i++) {
			if (i!=0) {
				if(Character.isUpperCase(cardClassName.charAt(i))) {
					sb.append(' ');
				}
			}
			sb.append(cardClassName.charAt(i));
		}
		return sb.toString();
	}
	
	public String getWitchEffectDescription() {
		return this.witchEffect.getDescription();
	}
	
	public String getHuntEffectDescription() {
		return this.huntEffect.getDescription();
	}
	
	public String getAdditionnalEffectDescription() {
		return this.additionnalEffectDescription;
	}
	
	
}
