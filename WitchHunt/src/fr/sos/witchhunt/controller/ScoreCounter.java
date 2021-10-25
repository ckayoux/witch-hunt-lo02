package fr.sos.witchhunt.controller;

import java.util.ArrayList;
import java.util.List;

import fr.sos.witchhunt.model.players.Player;

import fr.sos.witchhunt.Visitable;
import fr.sos.witchhunt.Visitor;

public final class ScoreCounter implements Visitor {

	@Override
	public void visit(Visitable v) {
		// TODO Auto-generated method stub
		
	}
	
	public List<Player> getClassment(){
		List<Player> classment = new ArrayList<Player>( );//todo : make real classment
		return classment;
	}
	
}
