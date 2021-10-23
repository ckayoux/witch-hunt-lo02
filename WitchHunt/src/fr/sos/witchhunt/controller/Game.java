package fr.sos.witchhunt.controller;

import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.view.std.StdView;
import fr.sos.witchhunt.model.players.HumanPlayer;

public final class Game {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StdView stdView = new StdView();
		DisplayController displayController = new DisplayController(stdView);
		Player p = new HumanPlayer("Jacques",1);
		p.setDisplayObserver(displayController);
		p.playTurn();

	}

}
