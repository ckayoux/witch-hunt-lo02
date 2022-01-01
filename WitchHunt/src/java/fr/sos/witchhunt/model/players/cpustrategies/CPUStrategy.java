package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.List;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.AngryMob;
import fr.sos.witchhunt.model.cards.BlackCat;
import fr.sos.witchhunt.model.cards.Broomstick;
import fr.sos.witchhunt.model.cards.Cauldron;
import fr.sos.witchhunt.model.cards.DuckingStool;
import fr.sos.witchhunt.model.cards.EvilEye;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.cards.Toad;
import fr.sos.witchhunt.model.cards.Wart;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

/**
 * <p><b>Concrete {@link fr.sos.witchhunt.model.players.cpustrategies.PlayStrategy CPU strategy}. Base for all CPU strategies.</b></p>
 * <p>Defines all behaviors that are common between all strategies.</p>
 * <p>Also defines some methods that are used by most strategies, but can be overriden to add extra behaviors or to modify the default behavior.</p>
 * <p>This classe's most important feature is to manage the strategy's {@link CardValueMap}.</p>
 * @see PlayStrategy
 * 
 * @see OffensiveStrategy
 * @see DefensiveStrategy
 * @see GropingStrategy
 * 
 * @see CardValueMap
 * @see CardValue
 * @see #updateCardValueMap(boolean, Identity, RumourCardsPile)
 */
public abstract class CPUStrategy implements PlayStrategy {
	/**
	 * When a card's effect's value is equal or above to this thresold, it is considered valuable.
	 * @see CardValue#getHuntValue()
	 * @see CardValue#getWitchValue()
	 */
	protected int goodEffectThresold=2;
	/**
	 * When a card's overall value is equal or above to this thresold, it is considered valuable.
	 * @see CardValue#getOverallValue()
	 */
	protected int goodCardThresold=4;
	
	/**
	 * When the number of unrevealed remaining players is equal or lower than this thresold, the game is considered tight
	 */
	protected int gameIsTightThresold=2; 
	/**
	 * When the player has a playable cards count equal or lower than this thresold, they will consider they lack cards.
	 */
	protected int acceptableCardsLimit=2;
	
	/**
	 * <p><b>All CPU strategies make their decisions using a map that associates each {@link fr.sos.witchhunt.model.cards.ExistingRumourCards existing Rumour card} with
	 * its {@link CardValue value}.</b></p>
	 * <p>This map has handy methods that can return the list of cards with the best or worst Hunt! effects, for example.</p>
	 * @see CardValue
	 * @see CardValueMap
	 */
	protected CardValueMap cvm = new CardValueMap();


	/**
	 * <p><b>In some particular situations, a CPU player can accept to {@link fr.sos.witchhunt.model.players.Player#revealIdentity() reveal their identity} even though they have still other choices.</b></p>
	 * <p>Returns <i>true</i> if the strategy judges revealing the player's identity is okay, that is to say :
	 * + The player is a {@link fr.sos.witchhunt.model.Identity#VILLAGER villager}.
	 * + The game is {@link #gameIsTightThresold not tight}
	 * + The player has got playable cards with {@link #goodEffectThresold decent} Hunt! effects. 
	 * + The player considers themselves {@link #acceptableCardsLimit short on cards}.
	 * @param identity The {@link fr.sos.witchhunt.model.Identity identity} of the player using this strategy.
	 * @param rcp The {@link fr.sos.witchhunt.model.players.Player#getHand() hand} of the player using this strategy.
	 * @return <i>true</i> if the strategy judges revealing the player's identity is okay, <i>false</i> otherwise.
	 * 
	 * @see PlayStrategy#revealOrDiscard(Identity, RumourCardsPile)
	 * @see PlayStrategy#selectDefenseAction(Identity, RumourCardsPile, boolean)
	 * @see fr.sos.witchhunt.model.players.Player#revealIdentity() Player::revealIdentity()
	 * 
	 * @see fr.sos.witchhunt.model.players.Player#getIdentity() Player::getIdentity()
	 * @see fr.sos.witchhunt.model.players.Player#getHand() Player::getHand()
	 */
	protected boolean isOkayToReveal (Identity identity,RumourCardsPile rcp) {
		if(Tabletop.getInstance().getUnrevealedPlayersList().size()>gameIsTightThresold && identity==Identity.VILLAGER && 
				cvm.getAverageHuntValue(rcp.getUnrevealedSubpile())>=goodEffectThresold&&
				rcp.getUnrevealedSubpile().getCardsCount()<=acceptableCardsLimit) return true;
		else return false;
	}

	/**
	 * <b>Asserts whether the strategy should consider the player short on cards or not.</b>
	 * @param rcp The {@link fr.sos.witchhunt.model.players.Player#getHand() hand} of the player using this strategy.
	 * @return <i>true</i> if the Rumour cards count in the player's hand is equal or below this strategy's {@link #acceptableCardsLimit limit}, <i>false</i> otherwise.
	 */
	protected boolean wantsCards(RumourCardsPile rcp) {
		if( rcp.getUnrevealedSubpile().getCardsCount() <= getAcceptableCardsNumberLimit() ) return true;
		else return false;
	}
	
	/**
	 * <p><b>Asserts whether a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} should be considered valuable or not, depending on the situation and
	 * of the card's values and special benefits.</b></p>
	 * <p>For example, when playing as a {@link fr.sos.witchhunt.model.Identity#WITCH witch}, even if a card's {@link CardValue#getWitchValue() Witch? effect value} is
	 * not juged {@link #goodEffectThresold interesting}, it can be valuable if it is a {@link CardValue#protects() protective} card.</p>
	 * @param rc The Rumour card the value of which is being discussed.
	 * @param myIdentity The {@link fr.sos.witchhunt.model.players.Player#getIdentity() identity} of the player using this strategy.
	 * @param myHand The {@link fr.sos.witchhunt.model.players.Player#getHand() hand} of the player using this strategy.
	 * @return <i>true</i> if the card is found valuable by this strategy, <i>false</i> otherwise.
	 * @see CardValue
	 * @see CardValueMap
	 */
	protected boolean findsValuable(RumourCard rc,Identity myIdentity,RumourCardsPile myHand) {
		CardValue cv = getCardValueMap().getValueByCard(rc);
		if(cv.givesCards()&&this.wantsCards(myHand)) return true;
		
		if(myIdentity==Identity.WITCH) {
			if (cv.getOverallValue()>getGoodCardThresold() || cv.getWitchValue()>=getGoodEffectThresold()
					|| Tabletop.getInstance().getActivePlayersList().size()<gameIsTightThresold || cv.protects()) {
				return true;
			}
			else return false;
		}
		else {
			if(Tabletop.getInstance().getActivePlayersList().size()<gameIsTightThresold && 
					myHand.getCardsCount()<getAcceptableCardsNumberLimit()&& cv.isRisked()) return false;
			else if (cv.getOverallValue()>getGoodCardThresold() || cv.getHuntValue()>=getGoodEffectThresold()
					|| Tabletop.getInstance().getActivePlayersList().size()<gameIsTightThresold || (cv.protects()) ){
				return true;
			}
			else return false;
		}
	}
	
	/**
	 * <b>Returns the average number of unrevealed cards, all players considered.</b>
	 * <p>Used for assessing the clutchness of the situation. If the players are averagely short on cards, offensive actions become more interesting.</p>
	 * @return The average count of {@link fr.sos.witchhunt.model.cards.RumourCard#isRevealed() unrevealed} cards, all {@link fr.sos.witchhunt.model.players.Player#isActive() active players} considered.
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getActivePlayersList() Tabletop::getActivePlayersList
	 */
	public double getAverageUnrevealedCardsNumber() {
		List <Integer> cardNumbers = Tabletop.getInstance().getActivePlayersList().stream()
				.mapToInt(p->(p.hasUnrevealedRumourCards())?p.getUnrevealedSubhand().getCardsCount():0).boxed().toList();
		if(!cardNumbers.isEmpty()) return cardNumbers.stream().reduce(0, Integer::sum) / (double) Tabletop.getInstance().getActivePlayersList().size();
		else return 0;
	}
	
	/**
	 * <p><b>Responsible of updating {@link #cvm the card-value map} depending on the situation.</b></p>
	 * <p>Avoids using disadvantageous cards : for example, using {@link fr.sos.witchhunt.model.cards.Toad Toad} or {@link fr.sos.witchhunt.model.cards.Cauldron Cauldron},
	 * which makes yourself {@link fr.sos.witchhunt.model.players.Player#revealIdentity() reveal your identity}, is always suicidal for Witches and should be done by Villagers
	 * only in specific situations.</p>
	 * <p>Finds an instance of a card of a given class using {@link fr.sos.witchhunt.model.cards.ExistingRumourCards#getInstanceByClass(Class) ExistingRumourCards#getInstanceByClass(Class)}, which returns
	 * the unique instance of a card of the given class.</p>
	 * <p>The card's current value is obtained using {@link CardValueMap#getValueByCard(RumourCard)}.
	 * It is modified using {@link CardValueMap#setValueFor(RumourCard, CardValue)}.
	 * It is protected against modification using {@link CardValue#lock()} and unprotected using {@link CardValue#unlock()}
	 * @param iAmRevealed The {@link fr.sos.witchhunt.model.players.Player#isRevealed() reveal status} of the player using this strategy.
	 * @param myIdentity The {@link fr.sos.witchhunt.model.Identity identity} of the player using this strategy.
	 * @param myHand The {@link fr.sos.witchhunt.model.players.Player#getHand() hand} of the player using this strategy.
	 * 
	 * @see CardValue
	 * @see CardValueMap
	 * @see ExistingRumourCards
	 * 
	 * @see ExistingRumourCards#getInstanceByClass(Class)
	 * @see CardValueMap#getValueByCard(RumourCard)
	 * @see CardValueMap#setValueFor(RumourCard, CardValue)
	 * @see CardValue#lock()
	 * @see CardValue#unlock()
	 * 
	 * @see fr.sos.witchhunt.model.cards.Toad The example of Toad, a card that is most often suicidal.
	 */
	public void updateCardValueMap(boolean iAmRevealed,Identity myIdentity, RumourCardsPile myHand) {
		CardValueMap M = getCardValueMap();
		CardValue cv;
		for(RumourCard rc : M.getMap().keySet()){
			cv=M.getValueByCard(rc);
			if (rc==ExistingRumourCards.getInstanceByClass(Wart.class)) { //WART value handling
				RumourCard duckingStoolInstance = ExistingRumourCards.getInstanceByClass(DuckingStool.class);
				if (!cv.protects()&& myIdentity==Identity.WITCH 
						//Becomes protective if DuckingStool hasn't been revealed and is not in own hand
				&& !duckingStoolInstance.isRevealed() && !myHand.contains(duckingStoolInstance)) {
					cv.setProtects(true);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				else if(cv.protects()&&(duckingStoolInstance.isRevealed()||myHand.contains(duckingStoolInstance))) { 
					//Stops being protective if DuckingStool in own hand or if it has been revealed.
					cv.unlock();
					cv.setProtects(false);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				
			}
			else if(rc==ExistingRumourCards.getInstanceByClass(Broomstick.class)) {//BROOMSTICK
				RumourCard angryMobInstance = ExistingRumourCards.getInstanceByClass(AngryMob.class);
				if(myIdentity==Identity.VILLAGER&& !angryMobInstance.isRevealed() && !myHand.contains(angryMobInstance)) { 
					//it is a good thing to be targetted by the angrymob if you are a villager - therefore, it becomes better not to use this card at all until it is revealed
					cv.setAdditionalValue(-2);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				else if (myIdentity==Identity.VILLAGER && (angryMobInstance.isRevealed() || myHand.contains(angryMobInstance))) {
					//reversing the modification if the card has been revealed
					cv.unlock();
					cv.setAdditionalValue(rc.getDefaultValue().getAdditionalValue());
					M.setValueFor(rc,cv );
					cv.lock();
				}
				else if (!cv.protects()&& myIdentity==Identity.WITCH 
				&& !angryMobInstance.isRevealed() && !myHand.contains(angryMobInstance)) {
					//it is interesting for witches to protect against AngryMob
					cv.setProtects(true);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				else if(cv.protects()&&(angryMobInstance.isRevealed()||myHand.contains(angryMobInstance))) {
					cv.unlock();
					cv.setProtects(false);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				
			}
			else if(rc==ExistingRumourCards.getInstanceByClass(Cauldron.class)//CAULDRON
					||rc==ExistingRumourCards.getInstanceByClass(Toad.class)) {//or TOAD
				/*the hunt effect has initially a bad value as it is risked,
				but it becomes an acceptable card if :
				- You are already revealed
				- You are a villager and thing revealing your identity is not that bad
				- You are a witch with no more playable witch cards, while the others still have some, so you cant eliminate yourselve and not give the others points*/
				if(iAmRevealed||this.isOkayToReveal(myIdentity, myHand)
						|| (myIdentity==Identity.WITCH && getAverageUnrevealedCardsNumber()<=0) ){
					cv.addHuntValue(2);
					cv.setRisked(false);
					M.setValueFor(rc,cv );
					cv.lock();
				}
			}
			else if(rc==ExistingRumourCards.getInstanceByClass(BlackCat.class)) {//BLACKCAT
				/*the hunt effect becomes more valuable if there are cards in the pile, 
				 * even more if you want cards,
				 * and even more if there is a good revealed card in the pile*/
				RumourCardsPile pile = Tabletop.getInstance().getPile();
				if(!pile.isEmpty()){
					int bonus = this.wantsCards(myHand)?1:0;
					if(!pile.getCards().stream().filter(c-> {
							CardValue val = cvm.getValueByCard(c);
							if(val.getHuntValue()>=goodEffectThresold||val.getWitchValue()>=goodEffectThresold||val.getOverallValue()>=goodCardThresold) return true;
							else return false;
						}).toList().isEmpty()) {
						cv.setHuntValue(3+bonus);
					}
					else {
						cv.setHuntValue(2+bonus);
					}
					M.setValueFor(rc,cv );
				}
			}
			else if(rc==ExistingRumourCards.getInstanceByClass(EvilEye.class)) {//EVIL EYE
				/*value becomes -1, risked if there are only 2 accusable players remaining
				 * the card is valuable when this is not the case and the average cards number is low (chances to cause a player to have no cards on your turn)*/
				if(Tabletop.getInstance().getActivePlayersList().size()<=2) {
					cv.unlock();
					cv.setHuntValue(-1);
					cv.setWitchValue(-1);
					cv.setDecisive(false);
					cv.setRisked(true);
					cv.lock();
				}
				else if(getAverageUnrevealedCardsNumber()<1.5) {
					cv.setHuntValue(3);
					cv.setDecisive(true);
					cv.setWitchValue(3);
					cv.lock();
				}
				M.setValueFor(rc,cv );
			}
			
			
		};
	}
	
	/**
	 * <p><b>Chooses an {@link fr.sos.witchhunt.model.players.TurnAction action to perform on the player's turn}, based on the current situation.</p></b>
	 * <p>When playing as a {@link fr.sos.witchhunt.model.Identity#WITCH witch}, a CPU Player will logically be more likely to choose to
	 * {@link fr.sos.witchhunt.model.players.Player#accuse(Player) accuse another player}, and even more when the situation becomes critical.</p>
	 * <p>There is always a chance of choosing to play a {@link fr.sos.witchhunt.model.players.Player#hunt() play a Hunt! effect}, excepted when the player only has risked Hunt! effects.</p>
	 */
	@Override
	public TurnAction selectTurnAction(Identity identity,RumourCardsPile myHand,boolean canHunt) {

		if(canHunt) {
			if(myHand.getPlayableHuntSubpile().getCards().stream()
					.filter(c->(cvm.getValueByCard(c).getHuntValue()>0&&
							((Math.random()<0.2)||(!cvm.getValueByCard(c).isRisked()&&
									(identity==Identity.WITCH||Tabletop.getInstance().getActivePlayersList().size()>gameIsTightThresold)))))
					.toList().isEmpty()) {
				return TurnAction.ACCUSE; /*if we only have cards with risked hunt effects (for a witch, or for any player when the game is not tight yet)
				,we don't want to Hunt at all*/
			}
			int chooseToAccuseProbability;
			if(identity==Identity.WITCH) {
				if(myHand.getCardsCount()>=acceptableCardsLimit+1) {
					chooseToAccuseProbability=60;
				}
				else {
					chooseToAccuseProbability=90;
				}
			}
			else {
				chooseToAccuseProbability=73-myHand.getCardsCount()*10; 
			}
			double n = Math.random()*100;
			return (n>100-chooseToAccuseProbability) ? TurnAction.ACCUSE : TurnAction.HUNT;
		}
		else return TurnAction.ACCUSE;
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>Will almost always choose to discard a card, excepted when the strategy judges that it is {@link #isOkayToReveal(Identity, RumourCardsPile) okay to reveal} and
	 * when the strategy finds valuable the card that it would choose to discard.</b>
	 */
	@Override
	public DefenseAction revealOrDiscard(Identity identity,RumourCardsPile rcp) {
		if(identity==Identity.WITCH) {
			return DefenseAction.DISCARD;
		}
		else {//as a villager, you might sometimes prefere to reveal
			RumourCard wouldDiscard = this.selectCardToDiscard(rcp);
			if (wouldDiscard.isRevealed()) 
				return DefenseAction.DISCARD;
			else if (this.findsValuable(wouldDiscard,identity,rcp)&& isOkayToReveal(identity, rcp)) 
				return DefenseAction.REVEAL;
			else 
				return DefenseAction.DISCARD;
		}
	}
	

	/**
	 * {@inheritDoc}
	 * <b>Useful when picking a new card or taking back a revealed card from your own hand.</b>
	 * <b>Default definition for what is the "best" card within a pile.</b>
	 * @return A random card in a list made of the cards with the best overall value + the unrevealed cards if they can't be seen
	 */
	@Override
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		RumourCardsPile selection;
		if(seeUnrevealedCards) {
			selection = cvm.getCardsWithMaxOverallValue(rcp);
		}
		else {
			if(!rcp.getRevealedSubpile().isEmpty()) {
				selection = cvm.getCardsWithMaxOverallValue(rcp.getRevealedSubpile());
				rcp.getUnrevealedSubpile().getCards().forEach(c -> selection.addCard(c));
			}
			else selection = rcp;
		}
		return selection.getRandomCard();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @return {@link #selectWorstCard(RumourCardsPile) the worst card found within the given deck}.
	 */
	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile rcp) {
		return selectWorstCard(rcp);
	}
	
	/**
	 * {@inheritDoc}
	 * <b>Default definition of what is the best Hunt! card to play for a strategy.</b>
	 * @return The card with the best {@link CardValue#getHuntValue() Hunt! effect value} within the given deck.
	 * @see CardValueMap#getCardsWithMaxHuntValue(RumourCardsPile)
	 */
	@Override
	public RumourCard selectHuntCard(RumourCardsPile rcp) {
		return cvm.getCardsWithMaxHuntValue(rcp).getRandomCard();
	}
	
	/**
	 * {@inheritDoc}
	 * <b>Common definition of what is the best {@link fr.sos.witchhunt.model.players.DefenseAction action to perform when accused}.</b>
	 * @return <i>{@link fr.sos.witchhunt.model.players.DefenseAction#REVEAL DefenseAction.REVEAL}</i> only if the strategy judges it is {@link #isOkayToReveal(Identity, RumourCardsPile) okay to reveal}, {@link fr.sos.witchhunt.model.players.DefenseAction#WITCH DefenseAction.WITCH} otherwise. 
	 */
	@Override
	public DefenseAction selectDefenseAction(Identity myIdentity,RumourCardsPile myHand,boolean canWitch) {
		if (this.isOkayToReveal(myIdentity, myHand)||!canWitch) return DefenseAction.REVEAL;
		else return DefenseAction.WITCH;
	}
	
	/**
	 * {@inheritDoc}
	 * <b>Default definition of what is the worst card within the given deck. Useful when choosing a card to discard.</b>
	 * @return A random card among the ones with the lowest {@link CardValue#getOverallValue() overall values}
	 * @see CardValueMap#getCardsWithMinOverallValue(RumourCardsPile)
	 */
	public RumourCard selectWorstCard(RumourCardsPile rcp) {
		return cvm.getCardsWithMinOverallValue(rcp).getRandomCard();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>Could also call a method that updates a map the keys of which would be players, for the strategy to dynamically select adversaries to target depending on their actions and status.</b>
	 */
	@Override
	public void updateBehavior(boolean amIRevealed, Identity myIdentity, RumourCardsPile myHand) {
		this.updateCardValueMap(amIRevealed,myIdentity,myHand);
	}
	
	
	public int getAcceptableCardsNumberLimit() {
		return this.acceptableCardsLimit;
	}
	public int getGoodEffectThresold() {
		return this.goodEffectThresold;
	}
	public int getGoodCardThresold() {
		return this.goodCardThresold;
	}
	
	public CardValueMap getCardValueMap() {
		return this.cvm;
	};
	


	
}
