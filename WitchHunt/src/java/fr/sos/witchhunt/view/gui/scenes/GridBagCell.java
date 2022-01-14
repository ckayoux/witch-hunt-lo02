package fr.sos.witchhunt.view.gui.scenes;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * <b>A custom graphical component used to make non-homogeneous grids out of plural components.</b>
 * <p><b>{@link GridBagPanel} {@code <#>--} {@link GridBagCell this}.</b> A GridBagPanel creates GridBagCells with their x and y coordinates as well as their width and heigth.</p>
 * <p>The cell's content pane is a JPanel. It supports any kind of layout managers.</p>
 * @see GridBagPanel
 */
public class GridBagCell{
	/**
	 * The cell's width (number of boxes occupied horizontally in the grid).
	 * Must be lower or equal to the master {@link GridBagPanel}'s grid width. 
	 */
	private int gwidth;
	/**
	 * The cell's height (number of boxes occupied vertically in the grid).
	 * Must be lower or equal to a {@link GridBagPanel}'s grid height. 
	 */
	private int gheight;
	/**
	 * The cell's top-left corner's x position in the grid.
	 * Must be lower or equal to the master {@link GridBagPanel}'s grid width. 
	 */
	private int gx;
	/**
	 * The cell's top-left corner's y position in the grid.
	 * Must be lower or equal to the master {@link GridBagPanel}'s grid heigth. 
	 */
	private int gy;
	
	/**
	 * The cell's content pane.
	 * Supports any layout manager.
	 */
	private JPanel pan;
	
	/**
	 * <b>This constructor should be called from {@link GridBagPanel GridBagPanels} only.</b>
	 * <p>A master GridBagPanel determines the cell's position and size and its border.</p>  
	 * @param x x position of the top-left corner in the grid
	 * @param y y position of the top-left corner in the grid
	 * @param w Cell width in arbitrary units 
	 * @param h Cell height in arbitrary units
	 * @param cellBorder Border displayed around the cell.
	 * @param cellsList A reference to the master {@link GridBagPanel}'s list of {@link GridBagCell GridBagCells}. When instantiated, automatically appended to this list.
	 * @see GridBagPanel
	 */
	public GridBagCell (int x, int y,int w, int h, Border cellBorder,List<GridBagCell> cellsList) {
		pan=new JPanel();
		pan.setBorder(cellBorder);
		this.gx=x;
		this.gy=y;
		this.gwidth=w;
		this.gheight=h;
		cellsList.add(this);
	}

	public int getX() {
		return gx;
	}
	public int getY() {
		return gy;
	}
	
	public int getWidth() {
		return this.gwidth;
	}
	public int getHeight() {
		return this.gheight;
	}
	public JPanel getPan() {
		return pan;
	}
	
	/**
	 * <b>This method is called when the master {@link GridBagPanel#init() GridBagPanel's init method} is called.</b>
	 * <p>Used to configure the cells depending on their real size once they are rendered.</p>
	 * <p>Override if a special configuration is needed only after rendering the Grid.</p>
	 * @see GridBagPanel#init() 
	 */
	public void init() {
		
	}

	
}