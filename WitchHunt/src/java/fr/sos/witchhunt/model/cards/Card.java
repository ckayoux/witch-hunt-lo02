package fr.sos.witchhunt.model.cards;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import fr.sos.witchhunt.model.Resettable;

/**
 * <p><b>This abstract class is extended by both {@link RumourCard Rumour cards} and {@link IdentityCard Identity cards}.</b></p>
 * <p>A card can be revealed or not.</p>
 * <p>A card can be {@link fr.sos.witchhunt.model.Resettable#reset() reset} to its original state.</p>
 * <p>All unrevealed cards are represented with the same picture.</p>
 * 
 * @see RumourCard
 * @see IdentityCard
 * 
 * @see #reveal()
 * @see #isRevealed()
 * @see #getUnrevealedCardImage()
 */
public abstract class Card implements Resettable {
	/**
	 * <i>true</i> if the card is revealed, <i>false</i> otherwise.
	 */
	protected boolean revealed=false;
	/**
	 * The fixed size for all card pictures.
	 */
	public static final Dimension IMAGES_SIZE=new Dimension(215,304);
	
	/**
	 * Reveals the card.
	 */
	public void reveal() {
		this.revealed=true;
	}
	
	/**
	 * When reset, a card is set back to "unrevealed".
	 * @see fr.sos.witchhunt.model.Resettable
	 */
	@Override
	public void reset() {
		this.revealed=false;
	}
	
	//GETTERS
	public boolean isRevealed() {
		return this.revealed;
	}
	
	/**
	 * Unrevealed cards (that includes {@link RumourCard Rumour cards} and {@link IdentityCard Identity cards}, the latter ones are not currently displayed, but could be in the future)
	 * share the same picture.
	 * @return The unrevealed cards' picture, loaded as a {@link java.awt.image.BufferedImage BufferedImage} and resized to the chosen {@link #IMAGES_SIZE normalized size}.
	 */
	public static final BufferedImage getUnrevealedCardImage () {
		String absPath = "/images/cards/Unrevealed.png";
		InputStream is = Card.class.getResourceAsStream(absPath);
		
		BufferedImage image=null;
		try {
			image=ImageIO.read(is);
		}
		catch(Exception e) {
			System.err.println("Could not load resource : "+absPath);
			System.out.println("Aborting...");
			System.exit(-1);
		}
		return resizeCardImage(image);
	}
	
	/**
	 * Resizes an image to the chosen {@link #IMAGES_SIZE normalized size}.
	 * @param original The original card's picture
	 * @return The resized card's picture
	 */
	public static final BufferedImage resizeCardImage(BufferedImage original) {
		int newWidth = (int)IMAGES_SIZE.getWidth();
		int newHeight = (int)IMAGES_SIZE.getHeight();
		BufferedImage resized = new BufferedImage(newWidth,newHeight, original.getType());
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(),
		    original.getHeight(), null);
		g.dispose();
		return resized;

	}
}
