package fr.sos.witchhunt.model.cards;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import fr.sos.witchhunt.model.Resettable;

public abstract class Card implements Resettable {
	protected boolean revealed=false;
	private static final Dimension IMAGES_SIZE=new Dimension(215,304);
	
	public void reveal() {
		this.revealed=true;
	}
	
	@Override
	public void reset() {
		this.revealed=false;
	}
	
	//GETTERS
	public boolean isRevealed() {
		return this.revealed;
	}
	
	
	public static final BufferedImage getUnrevealedCardImage () {
		URL resource = Card.class.getResource("/images/cards/Unrevealed.png");
		BufferedImage image=null;
		try {
			image=ImageIO.read(Paths.get(resource.toURI()).toFile());
		}
		catch(Exception e) {
			System.err.println("Could not load resource : "+resource.toString());
			System.out.println("Aborting...");
			System.exit(-1);
		}
		
		return resizeCardImage(image);
	}
	
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
