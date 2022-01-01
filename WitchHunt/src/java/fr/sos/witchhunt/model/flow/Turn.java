package fr.sos.witchhunt.model.flow;

import fr.sos.witchhunt.model.players.Player;

/**
 * <p><b>Class representing a single turn.</b></p>
 * <p>In charge of requesting the elected player to play their turn, and of managing accusator/accused and hunter/hunted communication.</p>
 * <p>Instantiated by an instance of {@link Round}. A single instance is expected to exist at a given time.</p>
 * @see Round
 */
public final class Turn {
	//ATTRIBUTES
	/**
	 * The turns counter. Incremented at each instantiation.
	 */
	private static int turnNumber=0;
	
	/**
	 * A reference to the player who has been accused during this turn, if they exist.
	 * @see fr.sos.witchhunt.model.players.Player#accuse(Player)	Player::accuse(Player)
	 */
	private Player accused;
	/**
	 * A reference to the current player, if they have chosen to accuse another player.
	 * @see fr.sos.witchhunt.model.players.Player#accuse(Player)	Player::accuse(Player)
	 */
	private Player accusator;
	
	/**
	 * A reference to the player targetted by a hunt effect during this turn, if there is one.
	 * @see fr.sos.witchhunt.model.players.Player#chooseHuntedTarget(java.util.List) Player::chooseHuntedTarget(List {@literal} <Player>})
	 * @see fr.sos.witchhunt.model.players.Player#beHunted() Player::beHunted()
	 */
	private Player hunted;
	
	/**
	 * A reference to the current player, if they checked whether they were able to play a Hunt effect or played it.
	 */
	private Player hunter;
	
	//CONSTRUCTOR
	/**
	 * <b>Constructor. Requests the current player to play his turn.</b>
	 * @param p The {@link fr.sos.witchhunt.model.players.Player player} whose turn it is.
	 */
	public Turn (Player p) {
		Tabletop.getInstance().getCurrentRound().setCurrentTurn(this);
		turnNumber++;
		p.playTurn();
	}
	
	//GETTERS
	public static int getTurnNumber() {
		return turnNumber;
	}
	public Player getAccusedPlayer() {
		return accused;
	}
	public Player getAccusator() {
		return accusator;
	}
	public Player getHuntedPlayer() {
		return this.hunted;
	}
	public Player getHunter() {
		return this.hunter;
	}
	
	//SETTERS
	public static void setTurnNumber(int turnNumber) {
		Turn.turnNumber = turnNumber;
	}
	public void setAccusedPlayer(Player accused) {
		this.accused = accused;
	}
	public void setAccusator(Player accusator) {
		this.accusator = accusator;
	}
	public void setHuntedPlayer(Player huntedPlayer) {
		this.hunted = huntedPlayer;
	}
	public void setHunter(Player hunter) {
		this.hunter = hunter;
	}
	

}
