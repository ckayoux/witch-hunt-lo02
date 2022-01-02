package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.Collections;
import java.util.List;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.flow.Tabletop;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

/**
 * <p><b>This strategy will be chosen by the CPUPlayer when they feel threatened :</b>
 * If the {@link fr.sos.witchhunt.model.flow.Tabletop#gameIsTied() game is tied}, if the leading players are far above in score,
 * if this player has no cards left, or if this player plays as a {@link fr.sos.witchhunt.model.Identity#WITCH witch} and is not close to victory.</p>
 * <p>It will be likely to choose to play as a {@link fr.sos.witchhunt.model.Identity#WITCH witch}, as this suits well a defensive playstyle.</p>
 * <p>It will {@link #selectPlayerToAccuse(List) focus the leading players}, excepted when a player is ready to receive the finishing blow.</p>
 * <p>Chooses cards based mostly on their {@link CardValue#getWitchValue() Witch? effect},values cards tagged as {@link CardValue#protects() able to protect}
 * and avoids {@link CardValue#isRisked() risked} cards.</p>
 * <p>{@link #getAcceptableCardsNumberLimit() Easily} {@link #wantsCards(RumourCardsPile) feels short on cards}.</p>
 * <p>A witch using this strategy {@link fr.sos.witchhunt.model.Identity#WITCH witch} will never reveal their identity unless 
 * {@link fr.sos.witchhunt.model.players.Player#canWitch() they have no more playable Witch? effects in their hand}.</p>
 * @see PlayStrategy
 * @see CPUStrategy
 * @see fr.sos.witchhunt.model.players.CPUPlayer
 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy()
 * @see CardValueMap
 * @see CardValue
 */
public class DefensiveStrategy extends CPUStrategy {
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will :</b>
	 * + Be more stingy on spending cards
	 * + Be less picky on cards and effects
	 * + Consider the game is tight easier
	 */
	public DefensiveStrategy() {
		this.acceptableCardsLimit=this.acceptableCardsLimit+1;//will spend cards harder
		this.goodCardThresold=3;
		this.gameIsTightThresold=3;
		this.goodEffectThresold=1;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will be likely to choose to play as a {@link fr.sos.witchhunt.model.Identity#WITCH witch}, as this suits well a 
	 * defensive and careful playstyle.</b>
	 * There is still a little chance to choose to play as a {@link fr.sos.witchhunt.model.Identity#VILLAGER villager}.
	 */
	@Override
	public Identity selectIdentity() {
		//more chances to be a witch
		int chooseVillagerProbability= 37;
		double n = Math.random()*100;
		return (n>100-chooseVillagerProbability) ? Identity.VILLAGER : Identity.WITCH;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will be more likely to avoid playing {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} than others, and will almost always choose to {@link fr.sos.witchhunt.model.players.TurnAction#ACCUSE accuse}.</b>
	 * <p>If the calling player is <i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i>, the more cards they have, the more likely it is to choose to hunt - but still unlikely.</p>
	 * <p>Otherwise, if they are a <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i>, they will be very likely to choose to 
	 * {@link fr.sos.witchhunt.model.players.TurnAction#ACCUSE accuse}, even more if they feel {@link #getAcceptableCardsNumberLimit() short on cards}.</p>
	 * <p>Independantly of the calling player's identity, if there are only risked cards or cards with {@link CardValue#getHuntValue() non-valuable Hunt! effects},
	 * within their hand, systematically chooses to {@link fr.sos.witchhunt.model.players.TurnAction#ACCUSE accuse}.</p>
	 */
	@Override
	public TurnAction selectTurnAction(Identity identity,RumourCardsPile myHand,boolean canHunt) {

		if(canHunt) {
			if(myHand.getPlayableHuntSubpile().getCards().stream()
					.filter(c->(cvm.getValueByCard(c).getHuntValue()>0&&
							((Math.random()<0.1)||(!cvm.getValueByCard(c).isRisked()&&
									(identity==Identity.WITCH||Tabletop.getInstance().getActivePlayersList().size()>gameIsTightThresold)))))
					.toList().isEmpty()) {
				return TurnAction.ACCUSE; /*if we only have cards with risked or bad hunt effects, we don't want to Hunt at all*/
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will priorize accusing players with exactly 0 or 2 cards - if there are none, they will target the leading players.</b>
	 * @see fr.sos.witchhunt.model.flow.Tabletop#getLeadingPlayers() Tabletop::getLeadingPlayers()
	 */
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


	/**
	 * {@inheritDoc}
	 * 
	 * When choosing a card to trigger its {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect}, 
	 * <b>this strategy will try selecting the cards with the {@link CardValueMap#getCardsWithMaxWitchValue(RumourCardsPile) best one}.</b>
	 * Among the cards with the best witch effects, will choose the ones with the {@link CardValueMap#getCardsWithMaxOverallValue(RumourCardsPile) best overall value}.
	 * Among this selection, will choose the ones with the {@link CardValueMap#getCardsWithMinHuntValue(RumourCardsPile) worse Hunt! effects}.
	 * @see CardValueMap
	 * @see CardValueMap#getCardsWithMaxWitchValue(RumourCardsPile)
	 * @see CardValueMap#getCardsWithMaxOverallValue(RumourCardsPile)
	 * @see CardValueMap#getCardsWithMaxHuntValue(RumourCardsPile)
	 */
	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		//selects the cards with the best witch effects, the best additionnal value and the lowest hunt effects
		RumourCardsPile bestWitchCards = cvm.getCardsWithMaxWitchValue(rcp);
		
		/*returns a random card among the ones with the lowest huntEffectValue among the ones with the lowest witchEffectValue.
		 * By default, the player keeps the best witch effects for the end of the round*/
		
		return cvm.getCardsWithMinHuntValue(cvm.getCardsWithMaxAdditionnalValue(bestWitchCards)).getRandomCard();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will always select the players with the lowest score to play the next turn.</b>
	 */
	@Override
	public Player selectNextPlayer(List<Player> list) {
		int lowestScore = Collections.min(list.stream().mapToInt(p->p.getScore()).boxed().toList());
		return list.stream().filter(p->p.getScore()==lowestScore).toList().get(list.size()*(int)Math.random());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p><b>This strategy</b> will make a pre-selection of all cards with {@link CardValueMap#getCardsWithPositiveHuntValue(RumourCardsPile) positive Hunt! effect values},
	 * that aren't {@link CardValue#isRisked() risked}, that are {@link CardValue#isDecisive() decisive}, that are able to 
	 * {@link CardValue#givesCards() give cards} or that {@link CardValue#protects() grant a protection}.</p>
	 * 
	 * <p>If this pre-selection contains cards, it will choose within it the cards with the {@link CardValueMap#getCardsWithMaxHuntValue(RumourCardsPile) best Hunt! effects}.
	 * Otherwise, it will choose, within all eligible cards, the ones with the {@link CardValueMap#getCardsWithMaxHuntValue(RumourCardsPile) best Hunt! effects},
	 * among the ones with the <b>{@link CardValueMap#getCardsWithMinWitchValue(RumourCardsPile) worst Witch? effects.}</b>.</p>
	 * @see CardValueMap
	 * @see CardValue
	 * @see CardValue#isRisked()
	 * @see CardValue#isDecisive()
	 * @see CardValue#protects()
	 * @see CardValue#givesCards()
	 * @see CardValueMap#getCardsWithMinWitchValue(RumourCardsPile)
	 * @see CardValueMap#getCardsWithMaxHuntValue(RumourCardsPile)
	 */
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will consider as the worse cards, the ones with the {@link CardValueMap#getCardsWithMinOverallValue(RumourCardsPile) worst overall values},
	 * among the ones with the {@link CardValueMap#getCardsWithMinWitchValue(RumourCardsPile) worst Witch? effects}.</b>
	 * @see CardValueMap
	 * @see CardValue
	 * @see CardValueMap#getCardsWithMinWitchValue(RumourCardsPile)
	 * @see CardValueMap#getCardsWithMaxOverallValue(RumourCardsPile)
	 */
	@Override
	public RumourCard selectCardToDiscard(RumourCardsPile rcp) {
		//priority to cards with bad witch effects for discarding
		return cvm.getCardsWithMinOverallValue(cvm.getCardsWithMinWitchValue(rcp)).getRandomCard();
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p><b>This strategy will consider as the best cards, the ones with the {@link CardValueMap#getCardsWithMaxOverallValue(RumourCardsPile) best overall values},
	 * among the ones with the {@link CardValueMap#getCardsWithMinWitchValue(RumourCardsPile) best Witch? effects} (priority is 
	 * given on Witch? effects).</b></p>
	 * <p>Unrevealed cards will have equal chances than the best reveal cards to be chosen.</p>
	 */
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
	@Override
	public String toString() {
		return "a careful playstyle";
	}
}
