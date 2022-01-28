package fr.sos.witchhunt.view;

import fr.sos.witchhunt.model.players.Player;
/**
 * <p><b>Classes implementing this interface are supposed to be able to {@link #post() transmit} data to an implementation of {@link fr.sos.witchhunt.controller.InputMediator InputMediator}.</b></p>
 * <p>Producer of the {@link fr.sos.witchhunt.controller.InputMediator InputMediator} - InputSource connexion, where the InputMediator is an intermediary consumer.</p>
 * <p>Implemented by classes able to collect user-input directly, like {@link fr.sos.witchhunt.view.std.InterruptibleStdInput InterruptibleStdInput} and the
 * {@link fr.sos.witchhunt.controller.interactions controllers instantiated by classes of the view level}.</p>
 * <p>Call a variant of {@link fr.sos.witchhunt.controller.InputMediator#receive() InputMediator::receive()} when overriding the <code>post</code> methods.</p>
 * 
 *  @see fr.sos.witchhunt.controller.InputMediator InputMediator
 */
public interface InputSource {
	/**
	 * <p><b>Transmits the given String to a known input consumer (like {@link fr.sos.witchhunt.controller.InputMediator InputMediator}).</b>
	 * @param str The String to be transmitted
	 * @see fr.sos.witchhunt.controller.InputMediator#receive(String) InputMediator::receive(String)
	 */
	public void post(String str);
	/**
	 * <p><b>Transmits an empty signal</b>, containing no information but a notification, <b>to a known input consumer (like {@link fr.sos.witchhunt.controller.InputMediator InputMediator}).</b></p>
	 * @see fr.sos.witchhunt.controller.InputMediator#receive() InputMediator::receive()
	 */
	public void post();
	/**
	 * <p><b>Transmits an instance of {@link fr.sos.witchhunt.model.players.Player Player} or <code>null</code> to a known input consumer (like {@link fr.sos.witchhunt.controller.InputMediator InputMediator}).</b></p>
	 * @see fr.sos.witchhunt.controller.InputMediator#receive(Player) InputMediator::receive(Player)
	 */
	public void post(Player p);
}
