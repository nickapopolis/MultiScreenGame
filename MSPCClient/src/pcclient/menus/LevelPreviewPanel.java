package pcclient.menus;
import pcclient.game.*;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
public class LevelPreviewPanel extends JPanel
{
	Level level;
	
	public LevelPreviewPanel(Level lvl)
	{
		level = lvl;
	}
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		level.paint(g2d, this);
	}
}
