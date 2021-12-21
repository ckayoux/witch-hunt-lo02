package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.AngryMob;
import fr.sos.witchhunt.model.cards.BlackCat;
import fr.sos.witchhunt.model.cards.Broomstick;
import fr.sos.witchhunt.model.cards.Cauldron;
import fr.sos.witchhunt.model.cards.DuckingStool;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.cards.Toad;
import fr.sos.witchhunt.model.cards.Wart;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public abstract class CPUStrategy implements PlayStrategy {
	//Base for all other strategies, with default behaviours and cards value management
	
	
	protected int goodEffectThresold=2; //When a cards' effect is $ or above, the effect is considered like valuable
	protected int goodCardThresold=4; //When a cards' total value is $ or above, it is considered like a valuable card
	protected int gameIsTightThresold=2; //When $ or less players are remaining, the game is considered tight
	protected int acceptableCardsLimit=2; //When the player has $ or less remaining, they will consider they lack cards
	
	protected CardValueMap cvm = new CardValueMap();


	
	protected boolean isOkayToReveal (Identity identity,RumourCardsPile rcp) {
		if(Tabletop.getInstance().getUnrevealedPlayersList().size()>2 && identity==Identity.VILLAGER && 
				cvm.getAverageHuntValue(rcp.getUnrevealedSubpile())>=goodEffectThresold&&
				rcp.getUnrevealedSubpile().getCardsCount()<=acceptableCardsLimit) return true;
		else return false;
	}

	protected boolean wantsCards(RumourCardsPile rcp) {
		if( rcp.getUnrevealedSubpile().getCardsCount() <= getAcceptableCardsNumberLimit() ) return true;
		else return false;
	}
	
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
	
	public double getAverageUnrevealedCardsNumber() {
		List <Integer> cardNumbers = Tabletop.getInstance().getActivePlayersList().stream()
				.mapToInt(p->(p.hasUnrevealedRumourCards())?p.getUnrevealedSubhand().getCardsCount():0).boxed().toList();
		if(!cardNumbers.isEmpty()) return cardNumbers.stream().reduce(0, Integer::sum) / (double) Tabletop.getInstance().getActivePlayersList().size();
		else return 0;
	}
	
	public void updateCardValueMap(boolean iAmRevealed,Identity myIdentity, RumourCardsPile myHand) {
		CardValueMap M = getCardValueMap();
		CardValue cv;
		for(RumourCard rc : M.getMap().keySet()){
			cv=M.getValueByCard(rc);
			if (rc==ExistingRumourCards.getInstanceByClass(Wart.class)) { //WART default value handling
				RumourCard duckingStoolInstance = ExistingRumourCards.getInstanceByClass(DuckingStool.class);
				if (!cv.protects()&& myIdentity==Identity.WITCH 
				&& !duckingStoolInstance.isRevealed() && !myHand.contains(duckingStoolInstance)) {
					cv.setProtects(true);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				else if(cv.protects()&&(duckingStoolInstance.isRevealed()||myHand.contains(duckingStoolInstance))) {
					cv.unlock();
					cv.setProtects(false);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				
			}
			else if(rc==ExistingRumourCards.getInstanceByClass(Broomstick.class)) {//BROOMSTICK
				RumourCard angryMobInstance = ExistingRumourCards.getInstanceByClass(AngryMob.class);
				if(myIdentity==Identity.VILLAGER) { //it is a good thing to be targetted by the angrymob if you are a villager.
					cv.addAdditionnalValue(-1);
					M.setValueFor(rc,cv );
					cv.lock();
				}
				else if (!cv.protects()&& myIdentity==Identity.WITCH 
				&& !angryMobInstance.isRevealed() && !myHand.contains(angryMobInstance)) {
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
					||rc==ExistingRumourCards.getInstanceByClass(Toad.class)) {//TOAD
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
			else if(rc==ExistingRumourCards.getInstanceByClass(BlackCat.class)) {//EVIL EYE
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
	/*@Override
	public Identity selectIdentity() {
		int chooseVillagerProbability;
		if(this.isConfident()) chooseVillagerProbability = 30;
		else chooseVillagerProbability = 80;
		double n = Math.random()*100;
		return (n>100-chooseVillagerProbability) ? Identity.VILLAGER : Identity.WITCH;
	}*/
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
	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		return accusablePlayersList.get((int) (Math.random() * accusablePlayersList.size()) );
	}
	
	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		RumourCardsPile worstWitchCards = cvm.getCardsWithMinWitchValue(rcp);
		/*returns a random card among the ones with the lowest huntEffectValue among the ones with the lowest witchEffectValue.
		 * By default, the player keeps the best witch effects for the end of the round*/
		
		return worstWitchCards.getRandomCard(); 
	}

	@Override
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		/*Selects a random card in a list made of the cards with the best overall value + the unrevealed cards if we can't see them
		 * Useful when picking a new card or taking back a revealed card from your own hand
		 */
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
	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile rcp) {
		return selectWorstCard(rcp);
	}
	@Override
	public RumourCard selectHuntCard(RumourCardsPile rcp) {
		return cvm.getCardsWithMaxHuntValue(rcp).getRandomCard();
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
	
	@Override
	public DefenseAction selectDefenseAction(Identity myIdentity,RumourCardsPile myHand,boolean canWitch) {
		if (this.isOkayToReveal(myIdentity, myHand)||!canWitch) return DefenseAction.REVEAL;
		else return DefenseAction.WITCH;
	}
	public RumourCard selectWorstCard(RumourCardsPile rcp) {
		/*useful when choosing a card to discard.
		returns a random card among the ones with the lowest witchEffectValue + huntEffectValue sum.*/
		return cvm.getCardsWithMinOverallValue(rcp).getRandomCard();
	}
	@Override
	public void updateBehavior(boolean amIRevealed, Identity myIdentity, RumourCardsPile myHand) {
		this.updateCardValueMap(amIRevealed,myIdentity,myHand);
	}
	

	
}
