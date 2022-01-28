package fr.sos.witchhunt.model.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.sos.witchhunt.model.Visitable;
import fr.sos.witchhunt.model.Visitor;
import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Class responsible for counting each player's score using the <i>Visitor design pattern</i>.</b></p>
 * <p>Keeps the accounts of the points obtained at each round by each player. Able to produce a scoreboard.</p>
 * <p>Can produce a ranking ordered by descending number of points.</p>
 * <p>Used to check whether the victory conditions are met.</p>
 * <p>Also polled by the AIs in order to choose a strategy based on their ranking and that of other players.</p>
 * @see fr.sos.witchhunt.model.Visitor
 */
public final class ScoreCounter implements Visitor {
	
	/**
	 * An object representing the score board, which keeps the accounts of the score obtained at each round by each player.
	 */
	private ScoreBoard sb;
	
	public ScoreCounter() {
		sb = new ScoreBoard(Tabletop.getInstance().getPlayersList());
	}
	/**
	 * @deprecated the <i>visit</i> method is not defined for a visitable of an unknown class.
	 * @param v A visitable whose class is not handled by this Visitor.
	 * @see fr.sos.witchhunt.model.Visitable Visitable
	 */
	@Deprecated
	@Override
	public void visit(Visitable v) {

	}
	/**
	 * <b>When visiting an instance of {@link fr.sos.witchhunt.model.players.Player Player}, updates the {@link #getScoreBoard() score board} by calling its own <i>visit</i> method.</b>
	 * @param p The player accepting a visit from the score counter.
	 * @see fr.sos.witchhunt.model.players.Player#accept(Visitor) Player::accept(Visitor)
	 * @see fr.sos.witchhunt.model.Visitable Visitable
	 */
	@Override
	public void visit(Player p) {
		sb.visit(p);
	}
	/**
	 * Add a column to the {@link ScoreBoard score board}. Called when a new {@link Round round} starts.
	 * @see Round
	 */
	public void addRound() {
		this.sb.addRound();
	}

	
	/**
	 * @return A list of all participating players, order by descending score.
	 * @see Tabletop#getPlayersList()
	 */
	public List<Player> getRanking(){
		List<Player> ranking = new ArrayList<Player>( );
		ranking=Tabletop.getInstance().getPlayersList();
		Collections.sort(ranking, Comparator.comparingInt(Player::getScore).reversed());
		return ranking;				//retourne la liste des joueurs classée par ordre décroissant de score 
	}
	
	/**
	 * @return A list of the players having the maximum score.
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy() CPUPlayer::chooseStrategy()
	 */
	public List<Player> getLeadingPlayers(){
		List<Player> playersList = Tabletop.getInstance().getPlayersList();
		int maxScore = Collections.max(playersList.stream().mapToInt(p->p.getScore()).boxed().toList());
		return playersList.stream().filter(p->p.getScore()==maxScore&&p.getScore()>0).toList();
	}
	
	/**
	 * @return A list of the players having the lowest score.
	 * @see fr.sos.witchhunt.model.players.CPUPlayer#chooseStrategy() CPUPlayer::chooseStrategy()
	 */
	public List<Player> getLastPlayers(){
		List<Player> playersList = Tabletop.getInstance().getPlayersList();
		int minScore = Collections.min(playersList.stream().mapToInt(p->p.getScore()).boxed().toList());
		return playersList.stream().filter(p->p.getScore()==minScore&&p.getScore()>0).toList();
	}
	
	/**
	 * <b>Checks whether the victory conditions are met or not.</b>
	 * @return true if {@link #getWinner()} returns an instance of {@link fr.sos.witchhunt.model.players.Player Player}.
	 * @see #getWinner()
	 */
	public boolean hasWinner() {
		return(this.getWinner()!=null);
	}
	
	/**
	 * <b>Returns the winning player if there is one, null otherwise.</b>
	 * @return true if there is only one potential winner (one player having the maximum score, the latter one being 5 or higher).
	 * @see #getPotentialWinners()
	 */
	public Player getWinner() {
		if (this.getPotentialWinners().size()==1) {
			return this.getPotentialWinners().get(0);
		}
		else return null;	
	}
	
	/**
	 * <b>Used to determine whether there is a winning player, or if the game is tied, or if nobody is close to the victory.</b>
	 * @return A list of all players sharing the maximum score, the latter one being 5 or higher
	 * @see Tabletop#gameIsTied()
	 */
	public List<Player> getPotentialWinners(){
		List<Player> playersList = Tabletop.getInstance().getPlayersList();
		int maxScore = Collections.max(playersList.stream().mapToInt(p->p.getScore()).boxed().toList());
		List<Player> potentialWinners = playersList.stream().filter(p->p.getScore()>=5&&p.getScore()==maxScore).toList();
		return potentialWinners;
	}
	
	public ScoreBoard  getScoreBoard() {		
		return this.sb;
	}
	
	
	/**
	 * <p><b>Internal class of {@link ScoreCounter}.</b></p>
	 * <p>In charge of keeping accounts of the score obtained at each round by each player.</p>
	 * <p>Update itselfs using the <i>Visitor design pattern</i>.</p>
	 * @see fr.sos.witchhunt.model.Visitor
	 */
	public class ScoreBoard implements Visitor  {
		/**
		 * <b>The accounts are kept using a map associating each player with the list of the scores they obtained at each round.</b>
		 */
		private HashMap<Player,ArrayList<Integer>> playerScoreByRound;
		private int roundsCount = 0;
		
		/**
		 * <b>At instanciation, adds each players to the map and associates them with a yet-empty scores list.</b>
		 * @param playersList The list of all players participating to the match.
		 */
		public ScoreBoard (List<Player> playersList) {
			playerScoreByRound = new HashMap<Player,ArrayList<Integer>>();
			playersList.forEach(p->{
				playerScoreByRound.put(p, new ArrayList<Integer>());
			});

		}
		
		/**
		 * <b>Adds a column (with each player's obtained score initialized to 0) to the score board. Called when a new {@link Round round} starts.</b>
		 * @see Round
		 */
		public void addRound() {
			if(Tabletop.getInstance().getCurrentRound().getRoundNumber()>roundsCount) {
				this.roundsCount++;
				playerScoreByRound.forEach((p,l)->l.add(0));
			}
		}
		
		/**
		 * When visiting an instance of {@link fr.sos.witchhunt.model.players.Player Player}, 
		 * updates this player's list of score replacing the value found at the index corresponding 
		 * to the current round with the player's new score minus the {@link #computeAnteriorRoundsTotal(Player) score obtained at previous rounds}. 
		 * @param p {@link fr.sos.witchhunt.model.players.Player Player} whose score has changed.
		 * @see fr.sos.witchhunt.model.players.Player#addScore(int) Player::addScore(int)
		 */
		@Override
		public void visit(Player p) {

			if(roundsCount>0) {
				playerScoreByRound.get(p).set(roundsCount-1,p.getScore()-computeAnteriorRoundsTotal(p));
			}
				
			
		}
		/**
		 * <b>Computes the total score obtained by a {@link fr.sos.witchhunt.model.players.Player player}, ignoring the score obtained during the current round.</b>
		 * @param p {@link fr.sos.witchhunt.model.players.Player Player} of which the previous rounds' score total is computed.
		 */
		private int computeAnteriorRoundsTotal(Player p) {
			int total = 0;
			if(roundsCount>1) {
				List<Integer> anteriorRoundsScores = playerScoreByRound.get(p).subList(0, roundsCount-1);
				total = anteriorRoundsScores.stream().reduce(total , Integer::sum);
			}
			return total;
		}
		
		/**
		 * @deprecated the <i>visit</i> method is not defined for a visitable of an unknown class.
		 * @param v A visitable whose class is not handled by this Visitor.
		 * @see fr.sos.witchhunt.model.Visitable Visitable
		 */
		@Deprecated
		@Override
		public void visit(Visitable v) {

		}
		
		/**
		 * @return A string representing the score board, with normalized cells size.
		 */
		@Override
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
		
		public Map<Player,ArrayList<Integer>> getPlayerScoreByRound() {
			return this.playerScoreByRound;
		}
		
		public int getRoundsCount() {
			return this.roundsCount;
		}
		
	}


	
	
}
