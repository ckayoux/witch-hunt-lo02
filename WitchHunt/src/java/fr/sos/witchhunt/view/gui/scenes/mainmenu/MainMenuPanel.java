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
import fr.sos.witchhunt.view.gui.scenes.ChoiceButton;
import fr.sos.witchhunt.view.gui.scenes.ChoicesPanel;
import fr.sos.witchhunt.view.gui.scenes.GridBagPanel;
import fr.sos.witchhunt.view.gui.scenes.game.Notification;
import fr.sos.witchhunt.view.gui.scenes.game.Theme;

/**
 * <p><b>The Main Menu scene.</b></p>
 * <p>Is a {@link fr.sos.witchhunt.view.gui.scenes.GridBagPanel GridBagPanel} composed of only one {@link fr.sos.witchhunt.view.gui.scenes.GridBagCell GridBagCell}, a {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel}.</p>
 * <p>Created and controlled by the {@link fr.sos.witchhunt.view.gui.GUIView GUI View's central class}. {@link fr.sos.witchhunt.view.gui.GUIView#gotoMainMenuPanel() GUIView::gotoMainMenuPanel()}</p>
 * <p>Is a visual representation of the {@link fr.sos.witchhunt.controller.core.Game#gotoMainMenu() state induced by Game::gotoMainMenu()}.</p>
 * @see fr.sos.witchhunt.view.gui.scenes.GridBagPanel GridBagPanel
 * @see fr.sos.witchhunt.view.gui.scenes.GridBagCell GridBagCell
 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
 * @see fr.sos.witchhunt.view.gui.GUIView#gotoMainMenuPanel() GUIView::gotoMainMenuPanel()
 * @see fr.sos.witchhunt.controller.core.Game#gotoMainMenu() Game::gotoMainMenu()
 */
public class MainMenuPanel extends GridBagPanel{
	/**
	 * <p><b>The instance of {@link fr.sos.witchhunt.controller.InputMediator InputMediator} in charge of sending input requests to this object.</b></p>
	 * <p>Collects input on the active {@link fr.sos.witchhunt.controller.interactions.Menu Menu} from this classe's {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel} in order to navigate into the Application.</p>
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 * @see fr.sos.witchhunt.controller.ConcreteInputMediator#makeChoice(Menu) ConcreteInputMediator::makeChoice(Menu)
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
	 * @see fr.sos.witchhunt.controller.interactions.ChoicesPanelController 
	 */
	private InputMediator inputMediator;

	/**
	 * <p><b>The background image for the Main Menu scene.</b></p>
	 * <p>{@link #loadBgImage()} the first time the Main Menu is displayed, only once, as a BufferedImage.</p>
	 * @see #loadBgImage()
	 */
	private final static BufferedImage bgImage = loadBgImage();
	/**
	 * <b>This extension of {@link fr.sos.witchhunt.view.gui.scenes.GridBagPanel GridBagPanel} is only composed of one {@link fr.sos.witchhunt.view.gui.scenes.GridBagCell GridBagCell}, a {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel}.</b>
	 * <p>Used to display a {@link fr.sos.witchhunt.controller.interactions.Menu Menu} and collect user-input.</p>
	 * @see fr.sos.witchhunt.view.gui.scenes.GridBagCell GridBagCell
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel ChoicesPanel
	 */
	private ChoicesPanel choicesPanel;
	
	/**
	 * <b>At instantiation, specializes the grid's parameters as well as the unique cell's and renders the scene.</b>
	 */
	public MainMenuPanel() {
		super(1,1);
		this.choicesPanel = new ChoicesPanel(0,0,1,1,defaultCellBorder,cellsList) {
			@Override
			public void displayMenu(Menu m) {
				this.resetPane();
				this.prompt.setText(m.getName());
				for(Object o : m.getOptions()) {
					String buttonText = ChoiceButton.makeButtonText(o);
					if(buttonText!=null) {
						ChoiceButton b =  new ChoiceButton(buttonText);
						b.setTheme(new Notification(Theme.WITCH).getFg());
						b.setForeground(Color.WHITE);
						this.choiceButtonsList.add(b);
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init() {
		this.choicesPanel.init();
	}
	
	/**
	 * <b>The preferred panel (and {@link fr.sos.witchhunt.view.gui.Window Window}) size will be that of the {@link #bgImage background image}. The window won't be resizable for this scene.</b>
	 * @see #bgImage
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(bgImage.getWidth(),bgImage.getHeight());
	}
	
	/**
	 * <b>Renders the {@link #bgImage background image} in the background of the JPanel when painting the component.</b>
	 * @see #bgImage
	 */
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(bgImage,0,0,null);
		super.paintComponent(g);
	}

	/**
	 * <b>Calls the {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#makeChoice(InputMediator) ChoicesPanel::makeChocie(InputMediator) method} on the {@link #choicesPanel choices panel}
	 * in order to start collecting user-input from it.</b>
	 * Not in charge of the Menu's display, for that, see {@link #displayMenu(Menu)}.
	 * @see fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#makeChoice(InputMediator)
	 */
	public void makeChoice() {
		this.choicesPanel.makeChoice(inputMediator);
	}
	
	/**
	 * @param inputMediator Value for field {@link #inputMediator}
	 * @see fr.sos.witchhunt.controller.InputMediator InputMediator
	 */
	public void setInputMediator(InputMediator inputMediator) {
		this.inputMediator=inputMediator;
		
	}

	/**
	 * <b>Calls the {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#displayMenu(Menu) ChoicesPanel::displayMenu(Menu) method} on the {@link #choicesPanel choices panel} in order to display
	 * the given Menu.</b>
	 * Not in charge of collecting user-input, for that, see {@link #makeChoice()}.
	 * @param m The Menu to be displayed.
	 */
	public void displayMenu(Menu m) {
		choicesPanel.displayMenu(m);
	}
	
	/**
	 * <b>Cleans the {@link #choicesPanel choices panel} by calling its {@link fr.sos.witchhunt.view.gui.scenes.ChoicesPanel#resetPane() ChoicesPanel::resetPane()} method.</b>
	 */
	public void resetChoicesPanel() {
		choicesPanel.resetPane();
	}
	
	/**
	 * <b>Loads the Main Menu's {@link #bgImage background image}. Called only once.</b>
	 * <p>Application will exited if this method fails.</p>
	 * @return <code>/images/bg/main-menu.jpg</code>, loaded as a BufferedImage.
	 */
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

