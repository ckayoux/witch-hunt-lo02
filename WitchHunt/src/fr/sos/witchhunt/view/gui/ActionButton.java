package fr.sos.witchhunt.view.gui;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;

import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;
public class ActionButton extends JButton {
	
	public ActionButton(String str) {
		super(str);
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setMargin(new Insets(10, 10, 10, 10));
	}
	
	public static Color getButtonColorByActionType(Object o) {
		if (o instanceof Identity) {
			Identity i = (Identity) o;
			switch (i) {
			case VILLAGER:
				return new Color(255, 142, 85);
			case WITCH:
				return new Color(161,132,220);
			}
		}
		else if (o instanceof TurnAction) {
			TurnAction ta = (TurnAction) o;
			switch (ta) {
				case ACCUSE:
					return new Color(255,97,97);
				case HUNT:
					return new Color(255, 142, 85);
			}
		}
		else if (o instanceof DefenseAction) {
			DefenseAction da = (DefenseAction) o;
			switch (da) {
				case REVEAL:
					return new Color(255,97,97);
				case WITCH:
					return new Color(161,132,220);
				case DISCARD:
					return new Color(156,147,135);
			}
		}
		return null;
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
