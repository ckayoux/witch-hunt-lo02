package fr.sos.witchhunt.model;

/**
 * <p><b>This interface is implemented by classes that can be reset.</b></p>
 * <p>Players and cards, notably, have most of their attributes reset between two {@link fr.sos.witchhunt.model.flow.Round rounds} rounds.</p>
 * <p>A revealed Rumour Card can also be reset when a player takes it back or steals it from another player, which makes it usable again.</p>
 * <p>Contains only the <i>{@link #reset()</i> method.</p>
 * <p>This notably avoids creating new cards or new players each round, which saves performances and allows keeping unique references of each element.</p>
 */
public interface Resettable {
	
	/**
	 * By defining this method, classes can determine which ones among their attributes should be reset.
	 */
	public void reset();
}
