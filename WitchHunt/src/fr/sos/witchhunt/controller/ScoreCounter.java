package fr.sos.witchhunt.controller;

import java.util.*;
import java.util.stream.Collectors;

import fr.sos.witchhunt.Visitable;
import fr.sos.witchhunt.Visitor;
import fr.sos.witchhunt.model.players.Player;

public final class ScoreCounter implements Visitor {
	
	private ScoreBoard sb;
	
	public ScoreCounter() {
		sb = new ScoreBoard(Tabletop.getInstance().getPlayersList());
	}
	
	public void visit(Visitable v) {

	}
	
	public void addRound() {
		this.sb.addRound();
	}

	@Override
	public void visit(Player p) {
		sb.visit(p);
	}
			
	public List<Player> getRanking(){
		List<Player> ranking = new ArrayList<Player>( );
		ranking=Tabletop.getInstance().getPlayersList();
		Collections.sort(ranking, Comparator.comparingInt(Player::getScore).reversed());
		return ranking;				//retourne la liste des joueurs classée par ordre décroissant de score 
	}
	
	public List<Player> getLeadingPlayers(){
		List<Player> playersList = Tabletop.getInstance().getPlayersList();
		int maxScore = Collections.max(playersList.stream().mapToInt(p->p.getScore()).boxed().toList());
		return playersList.stream().filter(p->p.getScore()==maxScore&&p.getScore()>0).toList();
	}
	
	public List<Player> getLastPlayers(){
		List<Player> playersList = Tabletop.getInstance().getPlayersList();
		int minScore = Collections.min(playersList.stream().mapToInt(p->p.getScore()).boxed().toList());
		return playersList.stream().filter(p->p.getScore()==minScore&&p.getScore()>0).toList();
	}
	
	
	public boolean hasWinner() {
		return(this.getWinner()!=null);
	}
	
	
	public Player getWinner() {
		if (this.getPotentialWinners().size()==1) {
			return this.getPotentialWinners().get(0);
		}
		else return null;	
	}
	
	
	
	public ScoreBoard  getScoreBoard() {		
		return this.sb;
	}
	
	
	public List<Player> getPotentialWinners(){
		List<Player> playersList = Tabletop.getInstance().getPlayersList();
		int maxScore = Collections.max(playersList.stream().mapToInt(p->p.getScore()).boxed().toList());
		List<Player> potentialWinners = playersList.stream().filter(p->p.getScore()>=5&&p.getScore()==maxScore).toList();
		return potentialWinners;
	}
	
	
	public class ScoreBoard implements Visitor  {
		private HashMap<Player,ArrayList<Integer>> playerScoreByRound;
		private int roundsCount = 0;
		
		public ScoreBoard (List<Player> playersList) {
			playerScoreByRound = new HashMap<Player,ArrayList<Integer>>();
			playersList.forEach(p->{
				playerScoreByRound.put(p, new ArrayList<Integer>());
			});

		}

		public void addRound() {
			if(Tabletop.getInstance().getCurrentRound().getRoundNumber()>roundsCount) {
				this.roundsCount++;
				playerScoreByRound.forEach((p,l)->l.add(0));
			}
		}
		public void visit(Player p) {

			if(roundsCount>0) {
				playerScoreByRound.get(p).set(roundsCount-1,p.getScore()-computeAnteriorRoundsTotal(p));
			}
				
			
		}

		private int computeAnteriorRoundsTotal(Player p) {
			int total = 0;
			if(roundsCount>1) {
				List<Integer> anteriorRoundsScores = playerScoreByRound.get(p).subList(0, roundsCount-1);
				total = anteriorRoundsScores.stream().reduce(total , Integer::sum);
			}
			return total;
		}
		

		@Override
		public void visit(Visitable v) {

		}
		
		public String toString() {
			int maxPNameLength = Collections.max(playerScoreByRound.keySet().stream().mapToInt(p->p.getName().length()).boxed().toList());
			StringBuffer sb = new StringBuffer(" ".repeat(maxPNameLength+2));
			int lineLength=0;
			for(int i = 1; i<=roundsCount; i++) {
				sb.append("|  ");
				sb.append(i);
				sb.append("  ");
			}
			sb.append("| Total ");
			lineLength=sb.length();
			final int lL = lineLength;
			sb.append('\n');
			sb.append("/+/"+"―".repeat(lineLength));
			sb.append('\n');
			List<Player> classment = getRanking();
			classment.forEach(p->{
				List<Integer> l = playerScoreByRound.get(p);
				sb.append("/+/");
				sb.append(' ');
				sb.append(p.getName());
				sb.append(" ".repeat(maxPNameLength-p.getName().length()));
				sb.append(' ');
				for(int i = 0; i<roundsCount; i++) {
					int score = l.get(i); 
					sb.append("| ");
					if(Integer.toString(score).length()==1) {
						sb.append(' ');
						sb.append(score);
						sb.append(' ');
					}
					else {
						sb.append(' ');
						sb.append(score);
					}	
					sb.append(' ');
				}
				sb.append("|   "+p.getScore()+"   ");
				boolean isLastPlayer = (classment.indexOf(p)+1==Tabletop.getInstance().getPlayersCount());
				sb.append('\n');
				sb.append("/+/");
				if(isLastPlayer) {
					sb.append("―".repeat(lL));
				}
				else {
					sb.append("-".repeat(lL));
				}
				sb.append('\n');
			});
			return sb.toString();
		}
		
	}


	
	
}
