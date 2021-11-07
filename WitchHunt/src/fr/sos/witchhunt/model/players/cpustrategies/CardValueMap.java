package fr.sos.witchhunt.model.players.cpustrategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import fr.sos.witchhunt.controller.Tabletop;
import fr.sos.witchhunt.model.cards.ExistingRumourCards;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.cards.RumourCardsPile;


public class CardValueMap {
	Map <RumourCard,CardValue> m; //the instances of RumourCard stored in this map are NOT the ones manipulated by the players, they belong to ExistingRumourCards.
	
	public CardValueMap () {
		m =  new HashMap <RumourCard,CardValue> ();
		for( RumourCard rc : ExistingRumourCards.getInstance().getList()) {
			m.put(rc, rc.getDefaultValue());
		}
	}

	
	public void setValueFor(String cardClassName,CardValue cv) throws ClassNotFoundException {
		for (RumourCard rc : ExistingRumourCards.getInstance().getList()) {
			try  {
				if(rc.getClass().equals(Class.forName(cardClassName))) {
					m.replace(rc, cv);
				}
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public Map<RumourCard,CardValue> match(RumourCardsPile rcp){
		/*Copies the map $m. 
		 *Keeps only the Rumour Cards types that are in rcp.
		 *Replaces the instances of the RumourCards kept by the actual ones which are used in the game. 
		 */
		return m.entrySet().stream().filter(e -> rcp.containsCardWithClassName(e.getKey().getClass().toString()))
			.collect(Collectors.toMap(e -> rcp.getCardWithClassName(e.getKey().getClass().getName()), Map.Entry::getValue));
	}
	
	public List<RumourCard> getCardsWithMinWitchValue(RumourCardsPile rcp) {
		//returns the classname of the cards with the lowest witchEffect value
		List<RumourCard> retainedCards = new ArrayList<RumourCard>() ;
		Map<RumourCard,CardValue> M = this.match(rcp);
		List<CardValue> CardValues = (List<CardValue>) M.values();
		List<Integer> witchEffectValues = new ArrayList<Integer>();
		M.values().forEach(cv -> witchEffectValues.add(cv.getWitchValue()));
		int min = Collections.min(witchEffectValues);
		M.forEach( (k,v) -> {if(v.getWitchValue()==min) retainedCards.add(k); });
		Collections.shuffle(retainedCards);
		return retainedCards;
	}
}
