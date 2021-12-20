package fr.sos.witchhunt.view.gui;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.Border;

public class GridBagCell{
	private int gwidth;
	private int gheight;
	private int gx;
	private int gy;
	private JPanel pan;
	
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
	
	public void init() {
		
	}

	
}