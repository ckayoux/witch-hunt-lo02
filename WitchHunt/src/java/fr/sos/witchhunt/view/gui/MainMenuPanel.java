package fr.sos.witchhunt.view.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import fr.sos.witchhunt.InputMediator;
import fr.sos.witchhunt.model.Menu;

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
						b.setTheme(new Notification(NotificationType.WITCH).getFg());
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
		this.choicesPanel.getPrompt().setForeground(new Notification(NotificationType.HUNT).getFg());
		
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
		URL rsc = MainMenuPanel.class.getResource("/images/bg/main-menu.jpg");
		
		BufferedImage image=null;
		try {
			image=ImageIO.read(Paths.get(rsc.toURI()).toFile());
		}
		catch(Exception e) {
			System.err.println("Could not load resource : "+rsc.toString());
			System.out.println("Aborting...");
			System.exit(-1);
		}
		return image;
	}
	
}

