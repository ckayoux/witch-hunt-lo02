package fr.sos.witchhunt.view.gui;

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

public abstract class GridBagPanel extends JPanel {
	protected final int gridWidth;
	protected final int gridHeight;
	
	protected final Insets gbcInsets = new Insets(5,5,5,5);
	
	protected List<GridBagCell> cellsList = new ArrayList<GridBagCell>();
	
	protected final Border defaultCellBorder = BorderFactory.createLineBorder(new Color(41,128,185));
	
	public GridBagPanel(int gridWidth, int gridHeight) {
		this.gridWidth=gridWidth;
		this.gridHeight=gridHeight;
		this.setLayout(new GridBagLayout());
	}
	
	@Override
	public abstract Dimension getPreferredSize();
	
	public void init() {
		cellsList.forEach(c->c.init());
	}
	
	public void buildCustomGridBag () {
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
