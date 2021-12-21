package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.Collections;
import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

/**
 * <b>This strategy will be chosen by the CPUPlayer if they are in good position (score close to 5, well ranked, or has a lot of cards).</b>
 * <p>It will be likely to choose to play as a Villager.
 * It can even sometimes {@link #isOkayToReveal(Identity, RumourCardsPile) accept to reveal their identity} if it is in their interest.</p>
 * 
 * <p>It will also be more likely to play {@link CardValue#isRisked() risked} cards than other strategies, and 
 * to {@link #selectTurnAction(Identity, RumourCardsPile, boolean) choose to play  Hunt! effect}.</p>
 * <p>Chooses cards based mostly on their {@link CardValue#getHuntValue() Hunt! effect} and values cards tagged as {@link CardValue#isOffensive() offensive}.</p>
 * @see PlayStrategy
 * @see CPUStrategy
 * @see fr.sos.witchhunt.model.players.CPUPlayer
 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy()
 * @see CardValueMap
 * @see CardValue
 */
public class OffensiveStrategy extends CPUStrategy {
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will consider that a player is low on cards under a lower thresold than other strategies.
	 * Thus, it will spend cards easier.</b>
	 */
	public OffensiveStrategy() {
		this.acceptableCardsLimit=this.acceptableCardsLimit-1;
	}
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p><b>This strategy will be very likely to make a player choose to play as a <i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i>,</b>
	 * since a lot of {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effects} require the player to be a villager,
	 * and since it allows it to keep playing even if revealed and/or out of cards.</p>
	 * <p>There is still a little chance for it to choose to play as a <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i>.</p>
	 */
	@Override
	public Identity selectIdentity() {
		//This strategy will lead the CPUPlayer to choose, almost exclusively, VILLAGER
		int chooseVillagerProbability= 87;
		double n = Math.random()*100;
		return (n>100-chooseVillagerProbability) ? Identity.VILLAGER : Identity.WITCH;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>This strategy will be more likely to choose to play a {@link fr.sos.witchhunt.model.cards.HuntEffect Hunt! effect} than others.</b>
	 * <p>If the calling player is <i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i>, the more cards they have, the more likely it is to choose to hunt.</p>
	 * <p>Otherwise, if they are a <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i>, they will hunt only if they have cards with {@link CardValue#getHuntValue() valuable Hunt! effects},
	 * that are not {@link CardValue#isRisked() risked}, or if they still have a lot of cards.</p>
	 * Generally speaking, a <i>{@link fr.sos.witchhunt.model.Identity#WITCH WITCH}</i> using this strategy will be more likely to choose to {@link fr.sos.witchhunt.model.players.TurnAction#ACCUSE accuse}.
	 * in order to keep their cards.</p>
	 */
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
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>Players using this strategy will be likely to focus the accusable players with the lowest number of revealed cards.</b>
	 */
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
	
	/**
	 *{@inheritDoc}
	 *
	 *<b>This strategy will be likely to choose to play the {@link fr.sos.witchhunt.model.cards.WitchEffect Witch? effect} of cards with 
	 *{@link CardValueMap#getCardsWithMinHuntValue(RumourCardsPile) the lowest Hunt! effect values}.</b>
	 *Uses witch effects only as a defense against accusation, avoids wasting cards with powerful Hunt! effects.
	 */
	@Override
	public RumourCard selectWitchCard(RumourCardsPile rcp) {
		//selects witch cards with the worst hunt effects
		RumourCardsPile worstHuntCards = cvm.getCardsWithMinHuntValue(rcp);
		return worstHuntCards.getRandomCard();
		
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>Players using this strategy will be likely to choose the players with the lowest number of revealed cards to take the next turn,
	 * </b>unless they only have exactly one (it is then better to let someone else play, hoping this player will get accused). 
	 */
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
	
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>Priority is given to {@link CardValue#getHuntValue() Hunt! effects values}, as long as the player
	 * is not in a bad position.</b>
	 * If there unrevealed cards cannot be seen, they have equal chances to be chosen than the best revealed cards.
	 * @see #isConfident(RumourCardsPile)
	 */
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

	/**
	 * Even with this agressive strategy, there are situations where it is preferable to stay careful.
	 * This private method is used to avoid running like a bull when the situation requires caution
	 * @param rcp The calling player's hand
	 * @return <i>false</i> if the game is considered tight (only a few unrevealed players left) or if the player is short on cards, <i>true</i> otherwise.
	 */
	private boolean isConfident(RumourCardsPile rcp) {
		if(Tabletop.getInstance().getActivePlayersList().size()<=gameIsTightThresold
				||rcp.getUnrevealedSubpile().getCardsCount()<=acceptableCardsLimit) return false;
		else return true;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <b>As a <i>{@link fr.sos.witchhunt.model.Identity#VILLAGER VILLAGER}</i>, also finds valuable cards with offensive effects and positive 
	 * both Witch and Hunt values.</b>
	 */
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
