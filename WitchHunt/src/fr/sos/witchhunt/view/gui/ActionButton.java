package fr.sos.witchhunt.view.gui;

import javax.swing.JButton;
import java.awt.Component;
import java.awt.Dimension;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;
import fr.sos.witchhunt.view.InputSource;

public class ActionButton extends JButton {
	
	public ActionButton(String str) {
		super(str);
	}
	
	public static String makeButtonText(Object o) {
		String buttonText=null;
		if (o instanceof String) {
			String str = (String) o;
			if(str.matches("^/c/.*")) {
				buttonText=null;//console only
			}
			else {
				buttonText=str;
			}
		}
		else if (o instanceof Identity) {
			Identity i = (Identity) o;
			switch (i) {
			case VILLAGER:
				return "Villager";
			case WITCH:
				return "Witch";
			}
		}
		else if (o instanceof TurnAction) {
			TurnAction ta = (TurnAction) o;
			switch (ta) {
				case ACCUSE:
					return "Accuse";
				case HUNT:
					return "Hunt";
			}
		}
		else if (o instanceof DefenseAction) {
			DefenseAction da = (DefenseAction) o;
			switch (da) {
				case REVEAL:
					return "Reveal";
				case WITCH:
					return "Witch";
				case DISCARD:
					return "Discard";
			}
		}
		else if (o instanceof Player) {
			Player p = (Player) o;
			return p.getName();
		}
		else if (o instanceof RumourCard) {
			RumourCard rc = (RumourCard) o;
			return rc.getName();
		}
		else {
			buttonText = o.toString();
		}
		return buttonText;
	}

	
}
