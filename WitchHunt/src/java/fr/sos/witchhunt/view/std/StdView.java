package fr.sos.witchhunt.view.std;

import java.util.List;

import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.model.Identity;
import fr.sos.witchhunt.model.cards.RumourCard;
import fr.sos.witchhunt.model.players.DefenseAction;
import fr.sos.witchhunt.model.players.HumanPlayer;
import fr.sos.witchhunt.model.players.Player;
import fr.sos.witchhunt.model.players.TurnAction;

/**
 * <b>Central class of the console view, responsible for console display.</b>
 * <p>Comes with console-display related utilities ({@link #logDashedLine() drawing a line, #setTabulation(int) managing indentation}) ...</p>
 * <p>Defines one method per situation where console display is required.</p>
 * <p>Special character sequences can be used.</p>
 * <p>A "/c/" sequence in {@link fr.sos.witchhunt.controller.interactions.Menu a menu's} String option means "display this entry only in the console view". The sequence itselfs will be invisible</p>
 * <p>A "/+/" sequence in a String will be replaced by the sum of the current {@link #offset} and the current {@link #tabulation} to display all lines after the first one as within a paragraph.</p>
 * 
 * <p>Display requests sent from the Model are supposed to be managed by {@link fr.sos.witchhunt.controller.DisplayMediator a DisplayMediator}.</p>
 *
 * <p>Not in charge of collecting any input. See {@link InterruptibleStdInput}.</p>
 *
 * @see fr.sos.witchhunt.controller.DisplayMediator DisplayMediator
 * @see fr.sos.witchhunt.controller.ConcreteDisplayMediator ConcreteDisplayMediator
 * 
 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
 * 
 * @see fr.sos.witchhunt.view.std
 */
public final class StdView {
	
	/**
	 * <p>When using {@link #tabbedLog(String)} or {@link #tabbedPrint(String)}, any "/+/" sequence <b>will be replaced by 
	 * a number of "\t" </b> equal to this field + a number of ' ' characters equal to {@link #offset}.</p>
	 * <p>For example, if {@link #tabulation} == 2 and {@link #offset} == "LO02",
	 * <code>{@link #tabbedLog(String) tabbedLog("Roses are red\n/+/,Violets are blue")}</code> will log the following String into the console :
	 * <pre>\t\tLO02Roses are red\n\t\t    Violets are blue</pre></p>
	 * 
	 * <p>Allows displaying dynamically-indented paragraphs.</p>
	 * <p>The "/+/" sequence should not be added to the first '\n' (newline character)	of the paragraph.</p>
	 * @see #offset
	 * @see #tabbedLog(String)
	 * @see #tabbedPrint(String)
	 * @see #increaseTabulation()
	 * @see #decreaseTabulation()
	 * @see #setTabulation(int)
	 */
	int tabulation = 0;
	
	/**
	 * <p>This String will be <b>displayed before the String parameter of any display method</b> within this class.</p>
	 * <p>When using {@link #tabbedLog(String)} or {@link #tabbedPrint(String)}, any "/+/" sequence <b>will be replaced by 
	 * a number of "\t" </b> equal to {@link #tabulation} + a number of ' ' character equal to this String's length.</p>
	 * 
	 * <p>For example, if {@link #tabulation} == 0 and {@link #offset} == "LO02",
	 * <code>{@link #tabbedLog(String) tabbedLog("Roses are red\n/+/,Violets are blue")}</code> will log the following String into the console :
	 * <pre>LO02Roses are red\n    Violets are blue</pre></p>
	 * 
	 * <p>Allows displaying dynamically-indented paragraphs precedented by this character sequence and normalized in terms of indentation.</p>
	 * <p>The "/+/" sequence should not be added to the first '\n' (newline character)	of the paragraph.</p>
	 * @see #offset
	 * @see #tabbedLog(String)
	 * @see #tabbedPrint(String)
	 * @see #increaseTabulation()
	 * @see #decreaseTabulation()
	 * @see #setTabulation(int)
	 */
	private String offset="";
	
	/**
	 * <b>Logs the welcome message into the console when instantiated.</b>
	 * @see #logSplash()
	 */
	//CONSTRUCTORS
	public StdView() {
		logStarsLine();
		increaseTabulation();
		increaseTabulation();
		logSplash();
		crlf();
		logStarsLine();
		crlf();
		tabbedLog("Welcome to Witch Hunt !");
		decreaseTabulation();
		decreaseTabulation();
	}
	
//UTILS
	
	/**
	 * <b>Calls {@link #tabbedPrint(String)} and logs a newline '\n' character.</b>
	 * @param msg The String to be logged with the current {@link #tabulation} and {@link #offset}.
	 * @see #tabbedPrint(String)
	 * @see #crlf()
	 */
	public void tabbedLog(String msg) {
		tabbedPrint(msg);
		crlf();
	}
	
	/**
	 * <b>Simply calls <code>System.out.println</code> after replacing any "/c/" ("console-only") sequence by an empty String</b> 
	 * @param msg The text to be logged into the console.
	 */
	public void log(String msg) {
		System.out.println(msg.replace("/c/",""));
	}
	
	/**
	 * <p>- Prints the current{@link #offset}.</p>
	 * <p>- Prints the {@link #tabulation} times '\t'.</p>
	 * <p>- Prints the first line of the given String.</p>
	 * <p>- Prints all other lines, replacing any "/+/" sequence by {@link #offset} times ' ' and {@link #tabulation} times '\t'
	 * <p>- No '\n' character added at the end, in opposition to {@link #tabbedLog(String)}.
	 * @param msg The text to display. My contain "/+/" ("Catch-up firstline indentation") special sequences. 
	 */
	public void tabbedPrint(String msg) {
		StringBuffer sb = new StringBuffer();
		StringBuffer megaTabSb= new StringBuffer();
		if(tabulation != 0) {
			for(int i=0; i<this.tabulation; i++) {
				megaTabSb.append('\t');
			}
			sb.append(megaTabSb.toString());
			
		}
		sb.append(this.offset);
		megaTabSb.append(" ".repeat(this.offset.length()));
		sb.append(msg.replace("/+/", megaTabSb.toString()));
		System.out.print(sb.toString());
	}
	
	/**
	 * Simply calls <code>System.out.print</code> with the given String as a parameter.
	 * @param msg The String to be displayed with no modifications.
	 */
	public void print(String msg) {
		System.out.print(msg);
	}
	
	/**
	 * <b>Logs a fancy stars line into the console.</b>
	 * @see #log(String)
	 */
	public void logStarsLine() {
		log("*********************************************************");
	}
	
	/**
	 * <b>Logs a fancy dashed line into the console.</b>
	 * @see #log(String)
	 */
	public void logDashedLine() {
		log("---------------------------------------------------------");
	}
	
	/**
	 * <b>Logs an indented and porous dashed line into the console.</b>
	 * @see #log(String)
	 */
	public void logWeakDashedLine() {
		log("     - - - - - - - - - - - - - - - - - - - - - - - - - - -");
	}
	
	/**
	 * <b>Logs a solid line made of underscore '_' characters.</b>
	 * @see #log(String)
	 */
	public void logHardLine() {
		log("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
	}
	
	/**
	 * <b>Logs a '\n' newline character into the console.</b>
	 */
	public void crlf() {
		System.out.print('\n');
	}
	
	/**
	 * <b>Displays a prompt inviting the user to enter input into the console.</b>
	 * <p>Not in charge of collecting the input : see {@link InterruptibleStdInput}.</p>
	 * @see InterruptibleStdInput
	 * @see fr.sos.witchhunt.controller.InputMediator#getStringInput() InputMediator::getStringInput()
	 */
	public void invite() {
		System.out.print("\t>> ");
	}
	
	/**
	 * <b>Displays a message asking for user-input before continuing.</b>
	 * <p>Not in charge of collecting the input and pausing the game : see {@link InterruptibleStdInput}.</p>
	 * @see InterruptibleStdInput
	 * @see fr.sos.witchhunt.controller.InputMediator#wannaContinue() InputMediator::wannaContinue()
	 */
	public void logContinueMessage() {
		log("\tPress ENTER to continue:");
	}
	
	/**
	 * @see #offset
	 */
	public void setOffset(String str) {
		this.offset=str;
	}
	/**
	 * @see #offset
	 */
	public void setBlankOffset(String str) {
		this.offset=" ".repeat(str.length());
	}
	/**
	 * @see #offset
	 */
	public void addOffset(String str) {
		this.offset+=str;
	}
	/**
	 * @see #offset
	 */
	public void resetOffset() {
		this.offset="";
	}
	/**
	 * @see #tabulation
	 */
	public void setTabulation(int t) {
		this.tabulation=t;
	}
	/**
	 * @see #tabulation
	 */
	public void increaseTabulation() {
		this.tabulation++;
	}
	/**
	 * @see #tabulation
	 */
	public void decreaseTabulation() {
		this.tabulation--;
	}

//ONE DISPLAY METHOD FOR EACH SITUATION REQUIRING CONSOLE DISPLAY
	
	/**
	 * <p><b>Converts a single {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu's entry} into a String. (Console version)</b></p>
	 * <p>Conversion is generaly more verbose than for the {@link fr.sos.witchhunt.view.gui.scenes.ActionButton#makeButtonText(Object) Graphical User Interface}.</p>
	 * <p>A {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu's entry} is of the <code>Object</code> type. The conversion result will depend on <code>instanceof</code> assertions, testing for 
	 * a membership to a more specific type.</p>
	 * <p>A String is displayed as it is, excepted for "/c/" ("Console-only" special sequence), which will be removed.</p>
	 * <p>An {@link fr.sos.witchhunt.model.Identity Identity} will display a text corresponding to it.</p>
	 * <p>A {@link fr.sos.witchhunt.model.players.TurnAction TurnAction} or a {@link fr.sos.witchhunt.model.players.DefenseAction DefenseAction} will be translated into a verbose description of the action's effect.</p>
	 * <p>A {@link fr.sos.witchhunt.model.players.Player Player} or a {@link fr.sos.witchhunt.model.cards.RumourCard Rumour card} will be replaced by the value of their <code>getName()</code> method.</p>
	 * @param o A {@link fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu's entry} (of type <code>Object</code>)
	 * @return The String describing the Menu's entry, depending on the classes or enums it is an instance of.
	 * 
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see fr.sos.witchhunt.controller.interactions.Menu#getOptions() Menu::getOptions()
	 * 
	 * @see #makeMenu(Menu)
	 * @see #logPossibilities(Menu)
	 * 
	 * @see fr.sos.witchhunt.view.gui.scenes.ActionButton#makeButtonText(Object) Console homologue of the GUI's ActionButton::makeButtonText(Object) method.
	 */
	public String stringifyMenuOption(Object o) {
		if (o instanceof String) {
			String str = (String) o;
			return str.replaceFirst("^/c/", ""); //console only
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
					return "Accuse another player";
				case HUNT:
					return "Play the Hunt! effect of a Rumour card from your hand";
			}
		}
		else if (o instanceof DefenseAction) {
			DefenseAction da = (DefenseAction) o;
			switch (da) {
				case REVEAL:
					return "Reveal your identity";
				case WITCH:
					return "Play the Witch? effect of a Rumour card from your hand";
				case DISCARD:
					return "Discard a card";
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
		return o.toString();
	}
	
	/**
	 * <p><b>Displays an Application-scale {@link fr.sos.witchhunt.controller.interactions.Menu Menu}, (such as the {@link fr.sos.witchhunt.controller.core.Game#gotoMainMenu() Game's main menu}) into the console.</b></p>
	 * <p>- Logs the {@link fr.sos.witchhunt.controller.interactions.Menu#getName() Menu's entitled} in UPPERCASE</p>
	 * <p>- {@link #logHardLine() Logs a solid separator}.</p>
	 * <p>- {@link #stringifyMenuOption(Object) Converts each entry of this Menu} into a String and logs it.</p> 
	 * <p>Secondary menus are displayed differently using the {@link #logPossibilities(Menu)} method.</p>
	 * @param m The {@link fr.sos.witchhunt.controller.interactions.Menu Menu} to be displayed into the console.
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see #logPossibilities(Menu)
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayMenu(Menu) DisplayMediator::displayMenu(Menu) 
	 */
	public void makeMenu(Menu m) {
		//used for main menus
		crlf();
		logHardLine();
		log("  "+m.getName().toUpperCase());
		logHardLine();
		int n=1;
		for (Object o : m.getOptions()) {
			StringBuffer sb = new StringBuffer("\t ");
			sb.append(n);
			sb.append(" - ");
			sb.append(stringifyMenuOption(o));
			log(sb.toString());
			n++;
		}
		crlf();
	}
	
	/**
	 * <p><b>Displays a match-scale, secondary {@link fr.sos.witchhunt.controller.interactions.Menu Menu}, (such as a {@link fr.sos.witchhunt.model.players.HumanPlayer#chooseNextPlayer() "Choose the next Player to play"} menu) into the console.</b></p>
	 * <p>All menus created from {@link fr.sos.witchhunt.model model} to ask an user to make a choice should be displayed as secondary menus.</p> 
	 * <p>- Logs the {@link fr.sos.witchhunt.controller.interactions.Menu#getName() Menu's entitled} with no modifications</p>
	 * <p>- {@link #stringifyMenuOption(Object) Converts each entry of this Menu} into a String and logs it.</p> 
	 * <p>Application-scale menus are displayed differently using the {@link #makeMenu(Menu)} method.</p>
	 * @param m The {@link fr.sos.witchhunt.controller.interactions.Menu Menu} to be displayed into the console.
	 * @see fr.sos.witchhunt.controller.interactions.Menu Menu
	 * @see #makeMenu(Menu)
	 * @see fr.sos.witchhunt.controller.DisplayMediator#displayPossibilities(Menu) DisplayMediator::displayPossibilities(Menu) 
	 */
	public void logPossibilities(Menu possibilities) {
		//used for making choices within a match
		log("\t"+possibilities.getName());
		int n=1;
		for (Object o : possibilities.getOptions()) {
			StringBuffer sb = new StringBuffer("\t\t ");
			sb.append(n);
			sb.append(" - ");
			sb.append(stringifyMenuOption(o));
			log(sb.toString());
			n++;
		}
		crlf();
	}

	/**
	 * <p><b>Displays a fancy ASCII-art splash.</b></p>
	 * <p>Indentation is determined dynamically thanks to the "/+/" ("Catch-up firstline indentation") special characters sequence.</p>
	 * @see #StdView() The splash is displayed at instantiation.
	 * @see #tabbedLog(String)
	 */
	public void logSplash() {
		tabbedPrint(" _     _         	\n"
				+ "/+/' )   / _/_    /    \n" 
				+ "/+/ / / /o /  _. /_       _   ,_,   _\n"
				+ "/+/(_(_/<_<__(__/ /_     / `'=) (='` \\\\ \n");
		tabbedLog(" _    ,             /.-.-.\\\\ /.-.-.\\\\ \n"
				+ "/+/' )  /          _/_  `      \\\"      `\n"
				+ "/+/ /--/ . . ____  /   \n"
				+ "/+//  (_(_/_/ / <_<__");

	}
	
	public void logMatchStartMessage() {
		crlf();
		logHardLine();
		log("Let the witch hunt begin !");
	}
	
	public void logMatchEndMessage() {
		logDashedLine();
		log("The game is over.");
		logHardLine();
	}
	
	public void logRoundStartMessage(int roundNumber) {
		logDashedLine();
		log("ROUND "+roundNumber+" :");
		crlf();
	}
	
	public void logRoundEndMessage(int roundNumber) {
		crlf();
		log("ROUND "+roundNumber+" IS OVER.");
		crlf();
	}
	
	public void logWinnerMessage(String name, int score) {
		//TEMPORARY
		crlf();
		log("\t~ "+name+ " has won the game with a score of "+score+" ! ~");
		crlf();
	}
	

	public void yesNoQuestion(String q) {
		log(q+" (y/n) :");
	}

	

	public void logChooseIdentityMessage() {
		logWeakDashedLine();
		crlf();
		log("\t~ Please choose your identity for this round ~");
		crlf();
	}
	
	public void logHasChosenIdentityMessage(String playerName) {
		log("\t"+playerName+" has chosen their identity.");
	}
	
	public void logAllPlayersHaveChosenTheirIdentityMessage() {
		crlf();
		logWeakDashedLine();
	}


	public void logHandDistributionMessage(int cardsNumber,int discardedCardsNumber) {
		crlf();
		log("\t~ Each player has received " + cardsNumber + " Rumour cards ~");
		if(discardedCardsNumber > 0) log("\t~ "+ discardedCardsNumber +" were put into the pile. ~");
		crlf();
		logWeakDashedLine();
		crlf();
	}

	public void logPlayTurnMessage(String playerName) {
		log("\tIt is " + playerName + (playerName.charAt(playerName.length()-1)!='s'?"'s":"'") + " turn.");
	}

	public void logEndOfTurnMessage() {
		crlf();
		logWeakDashedLine();
		crlf();
	}
	
	
	public void logAccusationMessage(String accusatorName, String accusedName) {
		log("\n\t"+accusedName + ", " + accusatorName + " accused you of practicing witchcraft !");
	}
	
	public void logChooseDefenseMessage() {
		log("\tThe village's pyre has been lit up.\n");
	}

	public void logForcedToRevealMessage() {
		log("\tYou have no other choice but to reveal who you really are.\n");
	}
	
	public void logGoingToRevealMessage(String playerName) {
		log("\t"+playerName+" is going to reveal their identity...");
	}
	
	public void logVillagerRevealMessage(String playerName) {
		log("\t"+playerName+" was only a villager.\n");
	}
	public void logWitchEliminatedMessage(String playerName) {
		increaseTabulation();
		tabbedLog("           (                      )      ____ \n"
				+ "/+/ (  (      )\\ )  *   )   (     ( /(     |   / \n"
				+ "/+/ )\\))(   '(()/(` )  /(   )\\    )\\())    |  /  \n"
				+ "/+/((_)()\\ )  /(_))( )(_))(((_)  ((_)\\     | /   \n"
				+ "/+/_(())\\_)()(_)) (_(_()) )\\___   _((_)    |/    \n"
				+ "/+/\\ \\((_)/ /|_ _||_   _|((/ __| | || |   (      \n"
				+ "/+/ \\ \\/\\/ /  | |   | |   | (__  | __ |   )\\     \n"
				+ "/+/  \\_/\\_/  |___|  |_|    \\___| |_||_|  ((_)    \n");
		decreaseTabulation();
		//log("\t"+playerName+" was a witch !\n");
	}
	public void logWitchRevealMessage(String playerName) {
		log("\t"+playerName+" was a witch !\n");
	}
	
	public void logTakeNextTurnMessage(String playerName) {
		log("\t"+playerName+" takes the next turn.");
	}
	
	public void logPlayTurnAgainMessage(String playerName) {
		log("\t"+playerName+" can play again.");
	}

	public void logUpdateScreenMessage(String playerName, int points, int totalPlayerScore) {
		if(points>0) print("\t"+playerName+" earned "+Integer.toString(points));
		else if (points<0) print("\t"+playerName+" lost "+Integer.toString(Math.abs(points)));
		else;
		print(" " + ( (Math.abs(points) == 1)?"point":"points" ) + ".");
		print(" (Total : "+ totalPlayerScore+")\n");
	}

	public void logEliminationMessage(String eliminatorName, String victimName) {
		if(eliminatorName!=victimName)
			log("\t"+eliminatorName + " has sentenced " + victimName + " to the stake !");
		else 
			log("\t"+eliminatorName+" has eliminated themselve.");
	}

	public void logLastUnrevealedMessage(String playerName) {
		log("\t" + playerName + " is the last unrevealed player remaining. It turns out...");
	}

	public void logNoCardsMessage(String playerName) {
		log("\t" + playerName + " has got not cards.");
	}

	public void logOnlyTwoUnrevealedRemainingMessage() {
		log("\t~ Only two unrevealed players remaining ! ~");
	}

	public void logUnrevealedCard() {
		tabbedLog("*Unrevealed*");
	}
	
	public void logRumourCard(String name, boolean revealed, String additionnalEffectDescription, String witchEffectDescription,
		String huntEffectDescription) {
		tabbedLog(name + ((revealed)?"\t(Revealed)":""));
		String off=" ".repeat(offset.length());//+"  ";
		
			setOffset(off);
			tabbedLog("*" + additionnalEffectDescription + "*");
			setOffset(off+"Witch : ");
			tabbedLog(witchEffectDescription);
			setOffset(off+"Hunt  : ");
			tabbedLog(huntEffectDescription);
			resetOffset();
		crlf();
	}
	public void logRumourCard(String name, boolean revealed, String witchEffectDescription, String huntEffectDescription) {
		tabbedLog(name + ((revealed)?"\t(Revealed)":""));
		String off=" ".repeat(offset.length());//+"  ";
			setOffset(off+"Witch : ");
			tabbedLog(witchEffectDescription);
			setOffset(off+"Hunt  : ");
			tabbedLog(huntEffectDescription);
			resetOffset();

		crlf();
	}

	public void logEffect(String name, String effectDescription) {
		tabbedLog(name+" :");
		setBlankOffset(this.offset+"  ");
		tabbedLog(effectDescription);
	}

	public void logEffect(String name, String additionnalEffectDescription, String effectDescription) {
		tabbedLog(name+" :");
		setBlankOffset(this.offset+"  ");
		tabbedLog("*"+additionnalEffectDescription +"*\n/+/" + effectDescription);
	}

	public void logShowPlayersCardsMessage(String playerName) {
		log("\t"+playerName + ", here are all the cards in your possession (others, don't look !) :");
	}
	
	public void logSelectCardsMessage(String cardAdjective) {
		log("\tSelect any "+((cardAdjective!=null)?cardAdjective+" ":"")+"card among these ones :");
	}

	public void logPlayerPlaysEffectMessage(String playerName,String cardName, String effectDescription) {
		setOffset("\t"+playerName+ " uses ");
		logEffect(cardName,effectDescription);
		resetOffset();
		crlf();
		
	}
	public void logPlayerPlaysEffectMessage(String playerName,String cardName, String effectDescription, String additionnalEffectDescription) {
		setOffset("\t"+playerName + " uses ");
		logEffect(cardName,additionnalEffectDescription,effectDescription);
		resetOffset();
		crlf();
	}

	public void logHasChosenCardMessage(String playerName,String cardName,boolean revealed,String additionnalEffectDescription,
			String witchEffectDescription,String huntEffectDescription) {
		setOffset("\t"+playerName + " has taken ");
		if(revealed) logRumourCard(cardName,revealed,additionnalEffectDescription,witchEffectDescription,huntEffectDescription);
		else logUnrevealedCard();
		resetOffset();
		crlf();
	}
	public void logHasChosenCardMessage(String playerName,String cardName,boolean revealed,String witchEffectDescription,String huntEffectDescription) {
		setOffset("\t"+playerName + " has taken ");
		if(revealed) logRumourCard(cardName,revealed,witchEffectDescription,huntEffectDescription);
		else logUnrevealedCard();
		resetOffset();
		crlf();
	}

	public void logEmptyHandMessage(String ownersName) {
		log("\t"+ownersName+"'"+((ownersName.charAt(ownersName.length()-1)!='s')?"s":"")+ "hand is empty.");
	}
	public void logEmptyPileMessage() {
		log("\tThe pile is empty.");
	}

	public void printPlayerDiscardedCardMessage(String playerName) {
		setOffset("\t"+playerName + " has discarded ");
	}

	public void logLookAtPlayersIdentityMessage(String myName, String targetsName) {
		log("\t"+myName + " is looking at "+targetsName+"'"+((targetsName.charAt(targetsName.length()-1)!='s')?"s":"") + " identity.");
	}

	public void logSecretIdentityRevealMessage(String targetsName, String identity) {
		log("\tOthers, close your eyes !");
		log("\t\t"+targetsName + " is a "+identity.toLowerCase()+ ".\n");
	}

	public void logWasAlreadyRevealedAs(String playerName, String identity) {
		log("\t"+playerName + " was already revealed as a "+identity.toLowerCase()+ ".\n");
	}

	public void logPlayerHasResetCardMessage(String playerName) {
		setOffset("\t"+playerName + " took back ");
		
	}

	public void logForcedToAccuseMessage(String accuserName, String forcedByName, boolean immunizedThemselve) {
		log("\t"+accuserName+", "+forcedByName+" forced you to accuse "+((immunizedThemselve)?"someone else ":"them ")+ "!");
	}

	public void logStealCardMessage(String thiefName,String stolenName) {
		log("\t"+thiefName+" is subtilizing a card from "+stolenName+" !");
	}

	public void logScoreBoard(String scoreBoardAsString) {
		tabbedLog("SCOREBOARD :\n");
		addOffset("   ");
		tabbedLog(scoreBoardAsString);
		resetOffset();
	}

	public void logGameIsTiedScreen(int score, List<String> duelists) {
		crlf();
		logDashedLine();
		log("\t~ The game is tied ! ~");
		StringBuffer sb = new StringBuffer("\t");
		for(int i=0; i<duelists.size(); i++) {
			sb.append(duelists.get(i));
			if(i<duelists.size()-2) sb.append(", ");
			else if(i==duelists.size()-2) sb.append(" and ");
		}
		sb.append((duelists.size()>2)?" all":" both");
		sb.append(" have a score of ");
		sb.append(score);
		sb.append(" and must compete for the first place !");
		log(sb.toString());
	}

	public void logPlayerAndTheirScore(int ranking, boolean exAequo, String name, int score) {
		StringBuffer sb = new StringBuffer();
		if(ranking!=-1) {
			sb.append(ranking);
			sb.append((exAequo)?" (ex aequo)":"");
			sb.append(" : ");
			
		}
		sb.append(name);
		sb.append(' ');
		tabbedLog(sb.toString()+"-".repeat(45-sb.toString().length())+" "+score);
	}

	public void logPlayerRankingMessage(boolean exAequo, int rank, int score) {
		tabbedLog("You are ranked n°"+rank+" "+((exAequo)?"(ex aequo) ":"")+"with a score of "+score+".");
	}

	public void logStrategyChange(String name, String playstyleDesc) {
		log("\t"+name+" opts for "+playstyleDesc+".");
	}

	public void logAddPlayersScreen(int minPlayersNumber,int maxPlayersNumber) {
		logDashedLine();
		log("~ Add "+minPlayersNumber+" to "+maxPlayersNumber+" players : ~\n");
	}

	public void logWrongMenuChoiceMessage(int timesWrong, String helperMsg, int optionsCount) {
		if(timesWrong==2) {
			log("\tAre you doing it on purpose ?");
			log("\t"+helperMsg);
		}
		else if (timesWrong==3) {
			log("\tCome on ! I believe in you ! You can do it !");
			if(optionsCount!=1)log("\t"+helperMsg);
		}
		else {
			if(optionsCount==1) {
				log("\tYou can only choose option #1 here !");
			}
			else log("\tInvalid choice. "+helperMsg);
		}
	}

	public void logAddedPlayersScreen(int n) {
		crlf();
		log("All "+Integer.toString(n)+" players have been successfully added.");
		logDashedLine();
		crlf();
	}

	public void logExitingGameScreen() {
		crlf();
		logStarsLine();
		log("See you soon !");
	}

	public void logInputWasExpectedMessage() {
		log("\tPlease make your choice.");
	}

	public void logInvalidYesNoQuestionAnswerMessage() {
		log("\tInvalid answer. Please type in whether 'y' or 'n' :");
	}

	public void logNoAvailableHuntEffectsMessage() {
		log("\tYou have no more available Hunt! effects.");
	}

	public void logSelectHuntCardMessage() {
		log("\tSelect a card with a valid Hunt! effect :");
	}

	public void logSelectWitchCardMessage() {
		log("\tSelect a card with a valid Witch? effect :");
		
	}

	public void logAddedPlayerMessage(Player p) {
		log("Added "+p.getName()+((p instanceof HumanPlayer)?" (Human)":""));
		crlf();
	}
	

}
