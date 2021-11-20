package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.List;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;
import fr.sos.witchhunt.model.cards.*;

public interface PlayStrategy {
	
	final int goodEffectThresold=2; //When a cards' effect is $ or above, the effect is considered like valuable
	final int goodCardThresold=4; //When a cards' total value is $ or above, it is considered like a valuable card
	final int gameIsTightThresold=3; //When $ or less players are remaining, the game is considered tight
	final int acceptableCardsLimit=1; //When the player has $ or less remaining, they will consider they lack cards
	
	public Identity selectIdentity();
	public TurnAction selectTurnAction();
	public Player selectPlayerToAccuse(List<Player> accusablePlayersList);
	public default DefenseAction selectDefenseAction(boolean canWitch) {
		if (canWitch) return DefenseAction.WITCH;
		else return DefenseAction.REVEAL;
		/*Tout Doux : do better.
		 * distinguer le cas d'un villageois, qui au pire peut être révélé mais reste dans la partie
		 * de celui d'une sorcière pour qui c'est finito si elle se fait accuser*/
	}
	public default Player selectTarget(List<Player> eligiblePlayers) {
		return selectPlayerToAccuse(eligiblePlayers);
	}
	
	public RumourCard selectWorstCard(RumourCardsPile rcp);
	public RumourCard selectWitchCard(RumourCardsPile rcp);
	public default RumourCard selectCardToDiscard(RumourCardsPile rcp) {
		return selectWorstCard(rcp);
	}
	public Player selectNextPlayer(List<Player> list);
	public RumourCard selectBestCard(RumourCardsPile rcp, boolean seeUnrevealedCards);
	public default DefenseAction revealOrDiscard(Identity identity,RumourCardsPile rcp) {
		if(identity==Identity.WITCH) {
			return DefenseAction.DISCARD;
		}
		else {//as a villager, you might sometimes prefere to reveal
			RumourCard wouldDiscard = this.selectCardToDiscard(rcp);
			if (wouldDiscard.isRevealed()) return DefenseAction.DISCARD;
			else if (this.findsValuable(wouldDiscard,identity,rcp)&&
					isOkayToReveal(identity, rcp)) return DefenseAction.REVEAL;
			else return DefenseAction.DISCARD;
		}
	}
	public default boolean isOkayToReveal (Identity identity,RumourCardsPile rcp) {
		if(Tabletop.getInstance().getActivePlayersList().size()>2 && identity==Identity.VILLAGER && 
				rcp.getPlayableHuntSubpile().getCardsCount()>=1 ) return true;
		else return false;
	}
	public default int getRemainingActivePlayersNumber() {
		return Tabletop.getInstance().getActivePlayersList().size();
	}
	public default void updateCardValueMap(boolean iAmRevealed,Identity myIdentity, RumourCardsPile myHand) {
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
			
		};
	}
	public CardValueMap getCardValueMap();
	public default boolean wantsCards(RumourCardsPile rcp) {
		if( rcp.getCardsCount() <= getAcceptableCardsNumberLimit() ) return true;
		else return false;
	}
	public default int getAcceptableCardsNumberLimit() {
		return this.acceptableCardsLimit;
	}
	public default int getGoodEffectThresold() {
		return this.goodEffectThresold;
	}
	public default int getGoodCardThresold() {
		return this.goodCardThresold;
	}
	
	public default boolean findsValuable(RumourCard rc,Identity myIdentity,RumourCardsPile myHand) {
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
	

}
