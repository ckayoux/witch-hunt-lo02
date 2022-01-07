package fr.sos.witchhunt.model.cards;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import fr.sos.witchhunt.model.players.cpustrategies.CardValue;

/**
 * <p><b>Represents a Rumour card.</b></p>
 * <p>A Rumour card has a {@link WitchEffect Witch? effect}, a {@link HuntEffect Hunt! effect} 
 * and sometimes an {@link #grantsImmunityAgainst(RumourCard) additionnal effect} which grants protection against other Rumour cards.</p>
 * <p>Is <i>abstract</i>. A class extending this one must be created for each Rumour card : see for example {@link PetNewt Pet Newt}.
 * <p>When creating a Rumour card's class, you must always instantiate its Witch? and Hunt! effects' with a definition for their {@link Effect#perform()} and {@link Effect#isAllowed()} methods.
 * You must also provide a description and a default value for these effects using their {@link Effect#Effect(String, int) constructor}.</p>
 * <p>You can create a card with no additional effects using the {@link #RumourCard() unparameterized constructor}, or a card with additional effects using
 * the {@link #RumourCard(String, int) parameterized constructor}.
 * The card's characteristics ({@link #isRisked risked}, {@link #givesCards able to give cards}, and/or {@link #isOffensive offensive}) can be modified when extending.
 * By default, they are all set to false.</p>
 * <p>A Rumour card's picture is {@link #loadImage() automatically loaded} {@link #getImagePath() based on its class name}.
 * When adding a new Rumour card with a class named "className" to the game, <b>make sure you add its picture</b> at <i>"/images/cards/"+className+".png"</i>.</p> 
 * <p>A Rumour card is associated with integer values : its additional effect's value, and its Witch? and Hunt! effects' values.</p> 
 * <p>These characteristics, as well as these integer values, are used for computing the default {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue card's value} for
 * {@link fr.sos.witchhunt.model.players.CPUPlayer CPU players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies}.</p>
 * <p>A Rumour card is directly associated with its {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue#getAdditionalValue() additional value} and its characteristics,
 * whereas its {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue#getWitchValue() Witch?} and {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue#getHuntValue() Hunt!} effects'
 * values are associated with the card's effects.</p>
 * <p>A Rumour card can be used by {@link fr.sos.witchhunt.model.players.Player players} in the following ways :
 * + {@link #witch() triggering its Witch? effect} when {@link fr.sos.witchhunt.model.players.Player#defend() defending against an accusation} (has to be unrevealed, becomes revealed)
 * + {@link #hunt() triggering its Hunt! effect} on {@link fr.sos.witchhunt.model.players.Player#playTurn() the player's turn} (has to be unrevealed, becomes revealed)
 * + reset by a player who has already used it (has to be revealed)
 * + discarded to the {@link fr.sos.witchhunt.model.flow.Tabletop#getPile() common pile of Rumour cards}, or taken from the pile (reset when taken)
 * + exchanged between players' hands (reset when taken)</p>
 * 
 * @see Card
 * @see WitchEffect
 * @see HuntEffect
 * @see #additionalEffectDescription
 * @see RumourCardsPile
 * @see #witch()
 * @see #hunt()
 * 
 * @see fr.sos.witchhunt.model.Resettable Resettable
 * @see fr.sos.witchhunt.model.Resettable#reset() Resettable::reset()
 * @see #reveal()
 * @see #reset()
 * 
 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue CardValue
 */
public abstract class RumourCard extends Card {
	
	/**
	 * This Rumour card's Witch? effect.
	 * Must be instantiated when this class is extended.
	 * @see WitchEffect
	 */
	protected Effect witchEffect;
	/**
	 * This Rumour card's Hunt! effect
	 * Must be instantiated when this class is extended.
	 * @see HuntEffect
	 */
	protected Effect huntEffect;
	
	/**
	 * This Rumour card's additional effect's description.
	 * For now, all additional effects {@link #grantsImmunityAgainst(RumourCard) grant a protection against other specific Rumour cards}.
	 * Excepted for the first line, you can use the <b>"/+/" special sequence</b> in order to make a <b>multiline paragraph with a view-managed indentation</b>.
	 * Empty string by default, redefinition is optional.
	 */
	protected String additionalEffectDescription="";
	/**
	 * This Rumour card's default {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue#getAdditionalValue() additional effect's value}.
	 * 0 by default, redefinition is optional.
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue CardValue
	 * @see #grantsImmunityAgainst(RumourCard)
	 */
	protected int defaultAdditionalValue=0;
	
	/**
	 * This characteristic is <i>true</i> if this card is able to make you take other cards from the pile or from other players' hands.
	 * <i>false</i> by default, redefinition is optional.
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue#givesCards() CardValue::givesCards()
	 */
	protected boolean givesCards=false;
	/**
	 * This characteristic is <i>true</i> if this card is, in its initial state, risked.
	 * <i>false</i> by default, redefinition is optional.
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue#isRisked() CardValue::isRisked()
	 */
	protected boolean isRisked=false;
	/**
	 * This characteristic is <i>true</i> if this card has offensive effects (forcing other players to discard a card...).
	 * <i>false</i> by default, redefinition is optional.
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue#isOffensive() CardValue::isOffensive()
	 */
	protected boolean isOffensive=false;
	
	/**
	 * The Rumour card's picture.
	 * <b>{@link #loadImage() Loaded automatically} {@link #getImagePath() based on its class name}.</b>
	 * When adding a new Rumour card with a class named "className" to the game, <b>make sure you add its picture</b> at <i>"/images/cards/"+className+".png"</i>.</p> 
	 */
	protected BufferedImage image;
	
	/**
	 * <b>Default, unparameterized, constructor.</b>
	 * Only {@link #loadImage() loads the card's picture}.
	 * The card will have no additional effect.
	 */
	public RumourCard() {
		this.loadImage();
	}
	
	/**
	 * <p><b>Parameterized constructor. for cards with an additional effect.</b></p>
	 * <p>{@link #loadImage() loads the card's picture by calling the {@link #RumourCard super constructor}.</p>
	 * <p>Define the additional effect at instanciation by overriding method {@link #grantsImmunityAgainst(RumourCard)} (because, for now, all additional effects 
	 * only give a protection against another specific Rumour card.</p>
	 * @param additionalEffectDescription The card's additional effect's description.
	 * @param defaultAdditionalValue This Rumour card's default {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue#getAdditionalValue() additional effect's value}.
	 * @see #additionalEffectDescription
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue#getAdditionalValue()
	 */
	public RumourCard(String additionalEffectDescription,int defaultAdditionalValue) {
		this();
		this.additionalEffectDescription=additionalEffectDescription;
		this.defaultAdditionalValue=defaultAdditionalValue;
	}
	
	/**
	 * <p><b>{@link WitchEffect#perform() Performs} this Rumour card's {@link #witchEffect Witch? effect}, if it {@link WitchEffect#isAllowed() is allowed}.</b></p>
	 * </p>This verification is optional as it is already performed by players before choosing an action, but it could be useful if new Rumour cards that would force
	 * players to trigger a Witch? effect were to come.</p>
	 * @return <i>true</i> if the card's witch effect {@link WitchEffect#isAllowed() was allowed}, <i>false</i> otherwise.
	 */
	public boolean witch() {
		if(this.witchEffect.isAllowed()) {
			this.witchEffect.perform(); //Could keep only this line with this method's current usage.
			return true;
		}
		else {
			return false;
		}
	};
	/**
	 * <p><b>{@link HuntEffect#perform() Performs} this Rumour card's {@link #huntEffect Hunt! effect}, if it {@link HuntEffect#isAllowed() is allowed}.</b></p>
	 * <p>This verification is optional as it is already performed by players before choosing an action, but it could be useful if new Rumour cards that would force
	 * players to trigger a a Hunt! effect were to come.</p>
	 * @return <i>true</i> if the card's witch effect {@link WitchEffect#isAllowed() was allowed}, <i>false</i> otherwise.
	 */
	public boolean hunt() {
		if(this.huntEffect.isAllowed()) {
			this.huntEffect.perform();
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * <p>Creates a {@link fr.sos.witchhunt.model.players.cpustrategies.CardValue CardValue} (used by {@link fr.sos.witchhunt.model.players.CPUPlayer CPU players'} 
	 * {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies} to choose their actions) based on this Rumour card's values for its 
	 * {@link #witchEffect Witch? effect}, for its {@link #huntEffect Hunt! effect} and for its {@link #defaultAdditionalValue additional effect},
	 * and on its boolean characteristics ({@link #isRisked risked}, {@link #givesCards able to give cards}, and/or {@link #isOffensive offensive}).</p>
	 * 
	 * @return The Card's default value, used by {@link fr.sos.witchhunt.model.players.CPUPlayer CPU players'} {@link fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy strategies}.
	 * 
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CardValue CardValue
	 * @see fr.sos.witchhunt.model.players.CPUPlayer CPUPlayer
	 * @see fr.sos.witchhunt.model.players.cpustrategies.CPUStrategy CPUStrategy
	 * 
	 * @see WitchEffect#getValue()
	 * @see HuntEffect#getValue()
	 * @see #getAdditionalValue();
	 * @see #givesCards
	 * @see #isRisked
	 * @see #isOffensive
	 */
	public CardValue getDefaultValue() {
		CardValue cv;
		if(this.defaultAdditionalValue==0) {
			cv= new CardValue(witchEffect.getValue(),huntEffect.getValue());
		}
		else {
			cv= new CardValue(witchEffect.getValue(),huntEffect.getValue(),this.defaultAdditionalValue);
		}
		if(this.isOffensive) cv.setOffensive(true);
		if(this.isRisked) cv.setRisked(true);
		if(this.givesCards) cv.setGivesCards(true);
		return cv;
	};
	
	/**
	 * <p><b>Defines whether a Rumour card grants immunity against another specific one when one of its effects is {@link Effect#perform() performed}.</b></p>
	 * <p>By default, <i>false</i>. Can be redefined to return true <b>when this one card is revealed</b>.</p>
	 * <p>If redefined, don't forget to give this card a {@link #additionalEffectDescription description for its additional effecŧ} as well as a {@link #defaultAdditionalValue default additional value}.
	 * You can use for this purpose the {@link #RumourCard(String, int) parameterized constructor}.</p>
	 * <p>See for example {@link Broomstick the Broomstick Rumour card}, which protects against {@link AngryMob the AngryMob Rumour card}.</p>
	 * @see Broomstick#grantsImmunityAgainst(RumourCard) Broomstick protects against Angry Mob
	 * @see AngryMob
	 * @see #additionalEffectDescription
	 * @see #defaultAdditionalValue
	 * @param rc The Rumour card against which immunity is granted. 
	 * @return By default,<i>false</i>. Can be redefined to return true <b>when this one card is revealed</b>.
	 */
	public boolean grantsImmunityAgainst(RumourCard rc) {
		return false;
	}
	
	/**
	 * <b>Whether this card's {@link #witchEffect Witch? effect} can be {@link #witch()} performed or not.</b>
	 * @return <i>true</i> only if its Witch? effect {@link WitchEffect#isAllowed() is allowed in the current situation} <b>and</b> if this card is {@link #isRevealed()} not revealed.
	 * @see fr.sos.witchhunt.model.players.Player#canWitch() Player::canWitch()
	 * @see fr.sos.witchhunt.model.players.Player#chooseDefenseAction() Player::chooseDefenseAction()
	 */
	public boolean canWitch() {
		return (this.witchEffect.isAllowed() && !this.revealed);
	}
	/**
	 * <b>Whether this card's {@link #huntEffect Hunt! effect} can be {@link #hunt()} performed or not.</b>
	 * @return <i>true</i> only if its Hunt! effect {@link HuntEffect#isAllowed() is allowed in the current situation} <b>and</b> if this card is {@link #isRevealed()} not revealed.
	 * @see fr.sos.witchhunt.model.players.Player#canHunt() Player::canHunt)
	 * @see fr.sos.witchhunt.model.players.Player#chooseTurnAction() Player::chooseTurnAction()
	 */
	public boolean canHunt() {
		return (this.huntEffect.isAllowed() && !this.revealed);
	}
	
	/**
	 * <b>Automatically builds the Rumour card's name based on its class name. A space is appended before each uppercase letter that is not the first one.</b>
	 * For example, calling this method on an extension the class of which is named AngryMob will return the string "Angry Mob".
	 * Make sure you give new Rumour cards a class name written in upper CamelCase and corresponding to the card's name.
	 * @return The extension's class name, with a space before each uppercase letter that is not the first one.
	 */
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
	
	/**
	 * @return {@link #getName()}
	 */
	@Override
	public String toString() {
		return this.getName();
	}
	
	public String getWitchEffectDescription() {
		return this.witchEffect.getDescription();
	}
	
	public String getHuntEffectDescription() {
		return this.huntEffect.getDescription();
	}
	
	public String getAdditionalEffectDescription() {
		return this.additionalEffectDescription;
	}

	/**
	 * <p><b>Loads the card's picture (in its unrevealed state), from a {@link #getImagePath() path based on its class name}.</b></p>
	 * <p>The picture is {@link Card#resizeCardImage(BufferedImage) resized} to the chosen {@link #IMAGES_SIZE normalized size} for all cards.</p>
	 * @return The revealed Rumour cards' picture, loaded as a {@link java.awt.image.BufferedImage BufferedImage} and resized to the chosen {@link #IMAGES_SIZE normalized size} for all cards.
	 * @see #getImagePath()
	 * @see Card#resizeCardImage(BufferedImage) 
	 */
	private final void loadImage() {
		InputStream is = this.getClass().getResourceAsStream(getImagePath());
		try {
			//this.image=Card.resizeCardImage(ImageIO.read(Paths.get(rsc.toURI()).toFile()));
			this.image=Card.resizeCardImage(ImageIO.read(is));
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Could not load resource : "+getImagePath());
			System.out.println("Aborting...");
			System.exit(-1);
		}
		finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * A Rumour card's picture is expected to be found at <i>"/images/cards/"+<b>className</b>+".png"</i>, <b>className</b> being the extension's class name.
	 * When adding a new Rumour card with a class named "className" to the game, <b>make sure you add its picture</b> at <i>"/images/cards/"+<b>className</b>+".png"</i>.</p>
	 * If you want to load an image with another name, override this method for your extension. 
	 * @return The extension's image path, computed automatically based on the extension's class name.
	 */
	private final String getImagePath() {
		String className = this.getClass().getSimpleName();
		return "/images/cards/"+className+".png";
	}


	
	public BufferedImage getImage() {
		return this.image;
	}

}
