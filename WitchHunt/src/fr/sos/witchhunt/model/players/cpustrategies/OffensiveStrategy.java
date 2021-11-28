package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.Collections;
import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

public class OffensiveStrategy extends CPUStrategy {
	/*This strategy will be chosen by the CPUPlayer if they are in good position (high score or classment before more than half of the players).
	 * They will take more risks, and be is more likely to hunt and spend cards.
	 * Most often, they'll choose VILLAGER.
	 */
	public OffensiveStrategy() {
		this.acceptableCardsLimit=this.acceptableCardsLimit-1;//will spend cards easier
		this.gameIsTightThresold=2; //will risks as long as there are not only 2 unrevealed players
	}
	
	@Override
	public Identity selectIdentity() {
		//This strategy will lead the CPUPlayer to choose, almost exclusively, VILLAGER
		int chooseVillagerProbability= 87;
		double n = Math.random()*100;
		return (n>100-chooseVillagerProbability) ? Identity.VILLAGER : Identity.WITCH;
	}
	
	
	@Override
	public TurnAction selectTurnAction(Identity identity,RumourCardsPile myHand,boolean canHunt) {
		//more chances to hunt
		if(canHunt) {
			if(myHand.getPlayableHuntSubpile().getCards().stream()
					.filter(c->(cvm.getValueByCard(c).getHuntValue()>0&&
							((Math.random()<0.3)||(!cvm.getValueByCard(c).isRisked()&&getCardValueMap().getValueByCard(c).getHuntValue()>0&& //add a little chance to take risks
									(identity==Identity.WITCH||Tabletop.getInstance().getActivePlayersList().size()>gameIsTightThresold)))
							))
					.toList().isEmpty()) {
				return TurnAction.ACCUSE; /*if we only have cards with risked hunt effects (for a witch, or for any player when the game is not tight yet)
				,we don't want to Hunt at all*/
			}
			int chooseToAccuseProbability;
			if(identity==Identity.WITCH) {
				if(myHand.getCardsCount()>=acceptableCardsLimit+1) {
					chooseToAccuseProbability=47;
				}
				else {
					chooseToAccuseProbability=80;
				}
			}
			else {
				chooseToAccuseProbability=37-myHand.getCardsCount()*10; 
			}
			double n = Math.random()*100;
			return (n>100-chooseToAccuseProbability) ? TurnAction.ACCUSE : TurnAction.HUNT;
		}
		else return TurnAction.ACCUSE;
		
	}
	
	@Override
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList) {
		//Players using this strategy will be likely to accuse the accusable players with the lowest number of revealed cards
		int chooseWeakestPlayerProbability;
		if(Tabletop.getInstance().getActivePlayersList().size()<=gameIsTightThresold) chooseWeakestPlayerProbability= 100;
		else chooseWeakestPlayerProbability = 83;
		double n = Math.random()*100;
		int lowestCardsNo = Collections.min(accusablePlayersList.stream().mapToInt(p->p.getUnrevealedSubhand().getCardsCount()).boxed().toList());
		if(n>100-chooseWeakestPlayerProbability||lowestCardsNo==0) {
			List<Player> weakestPlayersList = accusablePlayersList.stream().filter(p->p.getUnrevealedSubhand().getCardsCount()<=lowestCardsNo).toList();
			return weakestPlayersList.get((int) (Math.random() * weakestPlayersList.size()));
		}
		else return accusablePlayersList.get((int) (Math.random() * accusablePlayersList.size()) ); //Will still choose another player from time to time
	}
	
	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		//selects witch cards with the worst hunt effects
		RumourCardsPile worstHuntCards = cvm.getCardsWithMinHuntValue(rcp);
		return worstHuntCards.getRandomCard();
		
	}
	
	@Override
	public Player selectNextPlayer(List<Player> list) {
		// will select the player with the lowest number of unrevealed cards unless he has only one unrevealed card (it is then better to let someone else accuse him first)
		int lowestCardsNo= Collections.min(list.stream().mapToInt(p->p.getUnrevealedSubhand().getCardsCount()).boxed().toList());
		final int lwcn=lowestCardsNo;
		List<Player> weakestPlayersList = list.stream().filter(p->p.getUnrevealedSubhand().getCardsCount()<=lwcn).toList();
		if(lowestCardsNo!=1) {
			return weakestPlayersList.get((int) (Math.random() * weakestPlayersList.size()));
		}
		else {
			List<Player> playersExceptedWeakestOnesList = list.stream().filter(p->p.getUnrevealedSubhand().getCardsCount()>lwcn).toList();
			if(!playersExceptedWeakestOnesList.isEmpty()) {
				lowestCardsNo = Collections.min(playersExceptedWeakestOnesList.stream().mapToInt(p->p.getUnrevealedSubhand().getCardsCount()).boxed().toList());
				weakestPlayersList = list.stream().filter(p->p.getUnrevealedSubhand().getCardsCount()<=lwcn).toList();
				return weakestPlayersList.get((int) (Math.random() * weakestPlayersList.size()));
			}
			else {
				return list.get((int) Math.random()*list.size());
			}
		}
	}
	
	@Override
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards) {
		/*Priority to hunt effect values, as longas the player is not in too bad position*/
		RumourCardsPile selection;
		if(seeUnrevealedCards) {
			if(isConfident(rcp)) selection = cvm.getCardsWithMaxOverallValue(cvm.getCardsWithMaxHuntValue(rcp));
			else selection = cvm.getCardsWithMaxOverallValue(cvm.getCardsWithMaxWitchValue(rcp));
		}
		else {
			if(!rcp.getRevealedSubpile().isEmpty()) {
				if(isConfident(rcp)) selection = cvm.getCardsWithMaxHuntValue(rcp.getRevealedSubpile());
				else selection = cvm.getCardsWithMaxWitchValue(rcp.getRevealedSubpile());
				rcp.getUnrevealedSubpile().getCards().forEach(c -> selection.addCard(c));
			}
			else selection = rcp;
		}
		return selection.getRandomCard();
	}

	private boolean isConfident(RumourCardsPile rcp) {
		//even with that "big guns" strategy, there are situations where we try to be careful
		if(Tabletop.getInstance().getActivePlayersList().size()<=gameIsTightThresold
				||rcp.getUnrevealedSubpile().getCardsCount()<=acceptableCardsLimit) return false;
		else return true;
	}
	
	@Override
	protected boolean findsValuable(RumourCard rc,Identity myIdentity,RumourCardsPile myHand) {
		//also finds valuable offensive cards with both values positive
		CardValue cv = getCardValueMap().getValueByCard(rc);
		if(cv.givesCards()&&this.wantsCards(myHand)) return true;
		else if(cv.isOffensive()&&cv.getHuntValue()>=0&&cv.getWitchValue()>=0&&myIdentity==Identity.VILLAGER) return true;
		
		if(myIdentity==Identity.WITCH) {
			if (cv.getOverallValue()>getGoodCardThresold() || cv.getWitchValue()>=getGoodEffectThresold()
					|| Tabletop.getInstance().getActivePlayersList().size()<gameIsTightThresold || cv.protects()) {
				return true;
			}
			else return false;
		}
		else {
			if(Tabletop.getInstance().getActivePlayersList().size()<gameIsTightThresold && 
					this.wantsCards(myHand) && cv.isRisked()) return false;
			else if (cv.getOverallValue()>getGoodCardThresold() || cv.getHuntValue()>=getGoodEffectThresold()
					|| Tabletop.getInstance().getActivePlayersList().size()<gameIsTightThresold || (cv.protects()) ){
				return true;
			}
			else return false;
		}
	}
	@Override
	public String toString() {
		return "an agressive playstyle";
	}
	
	
	
}
