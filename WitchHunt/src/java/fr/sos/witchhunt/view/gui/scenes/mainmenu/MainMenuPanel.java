package fr.sos.witchhunt.view.gui.scenes.mainmenu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import fr.sos.witchhunt.controller.InputMediator;
import fr.sos.witchhunt.controller.interactions.Menu;
import fr.sos.witchhunt.view.gui.Notification;
import fr.sos.witchhunt.view.gui.Theme;
import fr.sos.witchhunt.view.gui.scenes.ActionButton;
import fr.sos.witchhunt.view.gui.scenes.ChoicesPanel;
import fr.sos.witchhunt.view.gui.scenes.GridBagPanel;

public class MainMenuPanel extends GridBagPanel{
	private InputMediator inputMediator;

	
	private final static BufferedImage bgImage = loadBgImage();
	private ChoicesPanel choicesPanel;
	
	public MainMenuPanel() {
		super(1,1);
		this.choicesPanel = new ChoicesPanel(0,0,1,1,defaultCellBorder,cellsList) {
			@Override
			public void displayMenu(Menu m) {
				this.resetPane();
				this.prompt.setText(m.getName());
				for(Object o : m.getOptions()) {
					String buttonText = ActionButton.makeButtonText(o);
					if(buttonText!=null) {
						ActionButton b =  new ActionButton(buttonText);
						b.setTheme(new Notification(Theme.WITCH).getFg());
						b.setForeground(Color.WHITE);
						this.actionButtonsList.add(b);
					}
				}
				this.renderPane();
			}
		};
		this.setOpaque(false);
		this.choicesPanel.getPan().setOpaque(false);
		this.choicesPanel.getPan().setBorder(null);
		this.choicesPanel.getPan().setAlignmentY(Component.CENTER_ALIGNMENT);
		this.choicesPanel.getPrompt().setFont(new Font("Arial",Font.BOLD,46));
		this.choicesPanel.getPrompt().setForeground(new Notification(Theme.HUNT).getFg());
		
		buildCustomGridBag();
		this.repaint();
	}
	
	@Override
	public void init() {
		this.choicesPanel.init();
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(bgImage.getWidth(),bgImage.getHeight());
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bgImage,0,0,null);
		super.paintComponent(g);
	}

	public void makeChoice(Menu m) {
		this.choicesPanel.makeChoice(m, inputMediator);
	}
	

	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
		
	}

	public void displayMenu(Menu m) {
		choicesPanel.displayMenu(m);
	}

	public void resetChoicesPanel() {
		choicesPanel.resetPane();
	}
	
	public static final BufferedImage loadBgImage () {
		String absPath = "/images/bg/main-menu.jpg";
		InputStream is = MainMenuPanel.class.getResourceAsStream(absPath);
		
		BufferedImage image=null;
		try {
			image=ImageIO.read(is);
		}
		catch(Exception e) {
			e.printStackTrace();
			System.err.println("Could not load resource : "+absPath);
			System.out.println("Aborting...");
			System.exit(-1);
		}
		finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return image;
	}
	
}

