package fr.sos.witchhunt.model.players.cpustrategies;

/**
 * <p><b>This class represents the value attributed to a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} by a SINGLE
 * {@link fr.sos.witchhunt.model.players.CPUPlayer CPU Player's} {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy strategy}.</b></p>
 * <p>An instance of CardValue is compound of plural integer values : its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch effect's} value, its
 * {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt effect's} value, its additional value,
 * and of plural booleans categorizing the card (risked, offensive, decisive ...).</p>
 * <p>It is dynamically and regularly {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) updated by a CPU Player's strategy},
 * which associates to each existing Rumour card a value thanks to its {@link CardValueMap}.</p>
 * <p>This value helps each {@link fr.sos.witchhunt.model.players.CPUPlayer CPU players} to choose their actions as well as the cards to steal, discard or play, based on their chosen strategy.</p>
 * <p>It can be locked to prevent modification, so it is updated only the first time a particular situation is met.</p>
 * <p>Each Rumour card has a default value, which can be modified using a parameterized constructor and by modifying fields directly in the card's constructor.
 * A card's default value, associated with its class, is given by {@link fr.sos.witchhunt.model.cards.RumourCard#getDefaultValue() RumourCard::getDefaultValue()}.
 * When instanciated, </p>
 * 
 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
 * @see CardValueMap
 * @see CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) CPUStrategy::updateBehavior(boolean,Identity,RumourCardsPile)
 * @see fr.sos.witchhunt.model.players.CPUPlayer CPUPlayer
 * @see fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy PlayStrategy
 */
public final class CardValue {
	/**
	 * <b>Integer value attributed by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU player} to a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}'s 
	 * {@link fr.sos.witchhunt.model.cards.WitchEffect witch effect}.</b>
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.WitchEffect WitchEffect
	 */
	private int witchValue;
	/**
	 * <b>Integer value attributed by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU player} to a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}'s 
	 * {@link fr.sos.witchhunt.model.cards.HuntEffect hunt effect}.</b>
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.HuntEffect HuntEffect
	 */
	private int huntValue;
	/**
	 * <p><b>Integer value attributed by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU player} to a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card}'s 
	 * additional value.</b></p>
	 * <p>Cards like {@link fr.sos.witchhunt.model.cards.Broomstick Broomstick} have an additional value as they
	 * {@link fr.sos.witchhunt.model.cards.RumourCard#grantsImmunityAgainst(fr.sos.witchhunt.model.cards.RumourCard) grant immunity against} another Rumour card.</p>
	 * <p>Most cards don't have, by default, an additional value.</p>
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.Broomstick Broomstick
	 * @see fr.sos.witchhunt.model.cards.RumourCard#grantsImmunityAgainst(fr.sos.witchhunt.model.cards.RumourCard) RumourCard::grantsImmunityAgainst(RumourCard)
	 */
	private int additionalValue;
	/**
	 * <p><b>This boolean field is <i>true</i> for Rumour cards which grant a protection against others.</b></p>
	 * <p>Cards like {@link fr.sos.witchhunt.model.cards.Broomstick Broomstick} have an additional value as they
	 * {@link fr.sos.witchhunt.model.cards.RumourCard#grantsImmunityAgainst(fr.sos.witchhunt.model.cards.RumourCard) grant immunity against} another Rumour card.</p>
	 * @see fr.sos.witchhunt.model.cards.RumourCard RumourCard
	 * @see fr.sos.witchhunt.model.cards.Broomstick Broomstick
	 * @see fr.sos.witchhunt.model.cards.RumourCard#grantsImmunityAgainst(fr.sos.witchhunt.model.cards.RumourCard) RumourCard::grantsImmunityAgainst(RumourCard)
	 */
	private boolean protects=false;
	/**
	 * <p><b>This boolean field is <i>true</i> for Rumour cards that become decisive from the point of view of a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU player}.</b></p>
	 * <p>For example, {@link fr.sos.witchhunt.model.cards.EvilEye Evil Eye} becomes decisive
	 * {@link CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) for all CPU strategies} 
	 * when the average unrevealed cards number is low.</p>
	 * @see fr.sos.witchhunt.model.cards.EvilEye Evil Eye
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) CPUStrategy::updateBehavior(boolean,Identity,RumourCardsPile)
	 */
	private boolean isDecisive=false;
	/**
	 * <p><b>This boolean field is <i>true</i> for a Rumour Card that is considered as risked by a {@link fr.sos.witchhunt.model.players.CPUPlayer CPU player}.</b></p>
	 * <p>For example, {@link fr.sos.witchhunt.model.cards.Toad Toad} is initially considered as risked - and looses this characteristic 
	 * {@link CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) in very particuliar situations}.</p>
	 * <p>A risked card has very low chances of being played by a {@link DefensiveStrategy Defensive strategy} and by witches in general.</p>
	 * @see fr.sos.witchhunt.model.cards.Toad Toad
	 * @see fr.sos.witchhunt.model.players.cpustrategies.DefensiveStrategy DefensiveStrategy
	 * @see CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) CPUStrategy::updateBehavior(boolean,Identity,RumourCardsPile)
	 */
	private boolean isRisked=false;
	/**
	 * <p><b>This boolean field is <i>true</i> for a Rumour Card that has an offensive effect.</b></p>
	 * <p>For example, {@link fr.sos.witchhunt.model.cards.DuckingStool the Ducking Stool} is considered as an offensive Rumour Card.</p>
	 * <p>An offensive RumourCard is likely to be valued by an {@link OffensiveStrategy offensive strategy}.</p>
	 * @see fr.sos.witchhunt.model.cards.DuckingStool DuckingStool
	 * @see fr.sos.witchhunt.model.players.cpustrategies.OffensiveStrategy OffensiveStrategy
	 */
	private boolean isOffensive=false;
	
	/**
	 * <p><b>This boolean field is <i>true</i> for a Rumour Card, the effects of which allow to take other Rumour cards.</b></p>
	 * <p>For example, {@link fr.sos.witchhunt.model.cards.HookedNose HookedNose} potentially gives cards.</p>
	 * <p>{@link DefensiveStrategy Defensive strategy} grant a high value to this kind of cards.</p>
	 * @see fr.sos.witchhunt.model.cards.HookedNose HookedNose
	 */
	private boolean givesCards=false;
	
	/**
	 * <p><b>Once this field is set to <i>true</i>, no field in this class can be modified excepted this one.</b></p>
	 * @see #setHuntValue(int)
	 * @see #setWitchValue(int)
	 * 
	 * @see #lock()
	 * @see #unlock()
	 * @see CPUStrategy#updateBehavior(boolean, fr.sos.witchhunt.model.Identity, fr.sos.witchhunt.model.cards.RumourCardsPile) CPUStrategy::updateBehavior(boolean,Identity,RumourCardsPile)
	 */
	private boolean locked=false;
	
	//CONSTRUCTORS
	public CardValue(int witchValue, int huntValue) {
		this.witchValue=witchValue;
		this.huntValue=huntValue;
		additionalValue=0;
	}
	
	public CardValue(int witchValue, int huntValue, int additionnalValue) {
		this.witchValue=witchValue;
		this.huntValue=huntValue;
		this.additionalValue=additionnalValue;
	}
	
	//GETTERS
	public int getOverallValue() {
		return this.witchValue + this.huntValue + this.additionalValue;
	}
	public int getWitchValue() {
		return this.witchValue;
	}
	public int getAdditionalValue() {
		return this.additionalValue;
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
	public boolean isOffensive() {
		return this.isOffensive;
	}
	public boolean givesCards() {
		return this.givesCards;
	}

	//SETTERS (protected by locking boolean)
	public void setHuntValue(int v) {
		if(!locked) this.huntValue=v;
	}
	public void setWitchValue(int v) {
		if(!locked) this.witchValue=v;
	}
	public void setAdditionalValue(int v) {
		if(!locked) this.additionalValue=v;
	}
	public void addHuntValue(int v) {
		if(!locked) this.huntValue+=v;
	}
	public void addWitchValue(int v) {
		if(!locked) this.witchValue+=v;
	}
	public void addAdditionnalValue(int v) {
		if(!locked) this.additionalValue+=v;
	}
	public void setProtects(boolean protects) {
		if(!locked) {
			if(!this.protects&&protects==true) this.additionalValue++;
			else if(this.protects&&protects==false) this.additionalValue--;
			this.protects=protects;
		}
		
	}
	public void setRisked(boolean risked) {
		if(!locked) {
			if(!this.isRisked&&risked==true) this.additionalValue++;
			else if(this.isRisked&&risked==false) this.additionalValue--;
			this.isRisked=risked;
		}
		
	}
	public void setOffensive(boolean is) {
		if(!locked) {
			this.isOffensive=is;
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
