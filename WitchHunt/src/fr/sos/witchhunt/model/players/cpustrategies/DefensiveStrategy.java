package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.Collections;
import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public class DefensiveStrategy extends CPUStrategy {
	public DefensiveStrategy() {
		this.acceptableCardsLimit=this.acceptableCardsLimit+1;//will spend cards harder
		this.goodCardThresold=3;
		this.goodEffectThresold=1;
	}
	@Override
	public Identity selectIdentity() {
		//more chances to be a witch
		int chooseVillagerProbability= 37;
		double n = Math.random()*100;
		return (n>100-chooseVillagerProbability) ? Identity.VILLAGER : Identity.WITCH;
	}
	
	@Override
	public TurnAction selectTurnAction(Identity identity,RumourCardsPile myHand,boolean canHunt) {

		if(canHunt) {
			if(myHand.getPlayableHuntSubpile().getCards().stream()
					.filter(c->(cvm.getValueByCard(c).getHuntValue()>0&&
							((Math.random()<0.1)||(!cvm.getValueByCard(c).isRisked()&&
									(identity==Identity.WITCH||Tabletop.getInstance().getActivePlayersList().size()>gameIsTightThresold)))))
					.toList().isEmpty()) {
				return TurnAction.ACCUSE; /*if we only have cards with risked hunt effects (for a witch, or for any player when the game is not tight yet)
				,we don't want to Hunt at all*/
			}
			int chooseToAccuseProbability;
			if(identity==Identity.WITCH) {
				if(myHand.getCardsCount()>=acceptableCardsLimit+1) {
					chooseToAccuseProbability=75;
				}
				else {
					chooseToAccuseProbability=96;
				}
			}
			else {
				chooseToAccuseProbability=87-myHand.getCardsCount()*10; 
			}
			double n = Math.random()*100;
			return (n>100-chooseToAccuseProbability) ? TurnAction.ACCUSE : TurnAction.HUNT;
		}
		else return TurnAction.ACCUSE;
		
	}

	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		//accuse a player with 0 or 2 cards. If not possible, accuse the player with the most cards.

		int lowestCardsNo = Collections.min(accusablePlayersList.stream().mapToInt(p->p.getUnrevealedSubhand().getCardsCount()).boxed().toList());
		if(lowestCardsNo==0||lowestCardsNo==2) {
			List<Player> weakestPlayersList = accusablePlayersList.stream().filter(p->p.getUnrevealedSubhand().getCardsCount()==lowestCardsNo).toList();
			return weakestPlayersList.get((int) (Math.random() * weakestPlayersList.size()));
		}
		else {
			int highestCardsNo = Collections.max(accusablePlayersList.stream().mapToInt(p->p.getUnrevealedSubhand().getCardsCount()).boxed().toList());
			List<Player> strongestPlayersList = accusablePlayersList.stream().filter(p->p.getUnrevealedSubhand().getCardsCount()==highestCardsNo).toList();
			return strongestPlayersList.get((int) (Math.random() * strongestPlayersList.size()) ); 
		}
	}



	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		//selects the cards with the best witch effects, the best additionnal value and the lowest hunt effects
		RumourCardsPile bestWitchCards = cvm.getCardsWithMaxWitchValue(rcp);
		
		/*returns a random card among the ones with the lowest huntEffectValue among the ones with the lowest witchEffectValue.
		 * By default, the player keeps the best witch effects for the end of the round*/
		
		return cvm.getCardsWithMinHuntValue(cvm.getCardsWithMaxAdditionnalValue(bestWitchCards)).getRandomCard();
	}

	@Override
	public Player selectNextPlayer(List<Player> list) {
		//this strategy always select the player with the lowest score to play the next turn
		int lowestScore = Collections.min(list.stream().mapToInt(p->p.getScore()).boxed().toList());
		return list.stream().filter(p->p.getScore()==lowestScore).toList().get(list.size()*(int)Math.random());
	}

	@Override
	public RumourCard selectHuntCard(RumourCardsPile rcp) {
		RumourCardsPile interestingCards = new RumourCardsPile(rcp.getCards().stream().filter(rc->{
			CardValue cv = cvm.getValueByCard(rc);
			if((cv.getHuntValue()>0&&!cv.isRisked())&&(cv.protects()||cv.givesCards()||cv.isDecisive())) return true;
			else return false;
		}).toList());
		RumourCardsPile selection;
		if(!interestingCards.isEmpty()) selection=cvm.getCardsWithMaxHuntValue(interestingCards);
		else selection = cvm.getCardsWithMaxHuntValue(cvm.getCardsWithMinWitchValue(rcp));	
		return selection.getRandomCard();
	}
	
	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile rcp) {
		//priority to cards with bad witch effects for discarding
		return cvm.getCardsWithMinOverallValue(cvm.getCardsWithMinWitchValue(rcp)).getRandomCard();
	}
	
	@Override
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		/*Selects a random card in a list made of the cards with the best overall value + the unrevealed cards if we can't see them
		 * Useful when picking a new card or taking back a revealed card from your own hand
		 */
		RumourCardsPile selection;
		if(seeUnrevealedCards) {
			selection = cvm.getCardsWithMaxOverallValue(cvm.getCardsWithMaxWitchValue(rcp));
		}
		else {
			if(!rcp.getRevealedSubpile().isEmpty()) {
				selection = cvm.getCardsWithMaxOverallValue(cvm.getCardsWithMaxWitchValue(rcp.getRevealedSubpile()));
				rcp.getUnrevealedSubpile().getCards().forEach(c -> selection.addCard(c));
			}
			else selection = rcp;
		}
		return selection.getRandomCard();
	}
}
