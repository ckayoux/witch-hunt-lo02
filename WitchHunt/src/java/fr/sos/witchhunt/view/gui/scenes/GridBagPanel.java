package fr.sos.witchhunt.view.gui.scenes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import fr.sos.witchhunt.view.gui.Theme;

/**
 * <b>An extension of JPanel managed by a {@link java.awt.GridBagLayout}.</b>
 * <p><b>{@link GridBagPanel this} {@code <#>--} {@link GridBagCell GridBagCells}.</b> The GridBagPanel can create plural GridBagCells with a chosen position in the grid as well as a 
 * chosen size.</p>
 * <p>Once all cells are created, calls its {@link #buildCustomGridBag()} method in order to render the cells.</p>
 * <p>Both the {@link fr.sos.witchhunt.view.gui.scenes.game.GamePanel game scene} and the {@link fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel main menu scene} extend this class.</p>
 * @see GridBagCell
 * @see fr.sos.witchhunt.view.gui.scenes.game.GamePanel GamePanel
 * @see fr.sos.witchhunt.view.gui.scenes.mainmenu.MainMenuPanel MainMenuPanel
 */
public abstract class GridBagPanel extends JPanel {
	/**
	 * The grid's width (number of columns of the grid).
	 * Use an integer lower or equal to this one when setting the {@link GridBagCell GridBagCells'} width.
	 */
	protected final int gridWidth;
	/**
	 * The grid's height (number of rows of the grid).
	 * Use an integer lower or equal to this one when setting the {@link GridBagCell GridBagCells'} height.
	 */
	protected final int gridHeight;
	
	/**
	 * Inter-cell margins.
	 */
	protected final Insets gbcInsets = new Insets(5,5,5,5);
	
	/**
	 * <b>The list of cells to fit in the grid.</b>
	 * @see GridBagCell
	 */
	protected List<GridBagCell> cellsList = new ArrayList<GridBagCell>();
	
	/**
	 * <b>The default border for a {@link GridBagCell} of this GridBagPanel.</b>
	 */
	protected final Border defaultCellBorder = BorderFactory.createLineBorder(new Color(41,128,185));
	
	/**
	 * @param gridWidth The grid's width (number of columns of the grid).
	 * @param gridHeight The grid's heigth (number of rows of the grid).
	 */
	public GridBagPanel(int gridWidth, int gridHeight) {
		this.gridWidth=gridWidth;
		this.gridHeight=gridHeight;
		this.setLayout(new GridBagLayout());
	}
	
	@Override
	public abstract Dimension getPreferredSize();
	
	/**
	 * <b>When called, triggers the {@link GridBagCell#init() init() method} of all GridBagCells within the grid.</b>
	 * <p>Used by GridBagCells to configure themselves only once the cells are rendered with their real size and proportions, which can be affected by the execution environment.</p>
	 */
	public void init() {
		cellsList.forEach(c->c.init());
	}
	
	/**
	 * <b>Renders each cell of the {@link #cellsList list of GridBagCells}</b> by parameterizing the {@link java.awt.GridBagConstraints} based on the cells properties and adding it to JPanel.</b> 
	 * <p>Position, size, insets and borders are this classe's responsibility.</p>
	 */
	protected void buildCustomGridBag () {
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = this.gbcInsets;
		
		Iterator<GridBagCell> it = cellsList.iterator();
		while (it.hasNext()) {
			GridBagCell cell = it.next();
			int w = cell.getWidth();
			int h = cell.getHeight();
			int x = cell.getX();
			int y = cell.getY();
			gbc.gridx = x;
			gbc.gridy = y;
			gbc.gridwidth = w;
			gbc.weightx = w/(float)this.gridWidth;
			gbc.gridheight = h;
			gbc.weighty = h/(float)this.gridHeight;
			this.add(cell.getPan(),gbc);			
		}
	}
	

}
