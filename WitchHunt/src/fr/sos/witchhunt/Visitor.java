package fr.sos.witchhunt;

import fr.sos.witchhunt.model.players.Player;

public interface Visitor {
	void visit(Visitable  v);
	void visit(Player p);
}
