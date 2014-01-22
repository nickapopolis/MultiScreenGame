package pcclient.game;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JPanel;


public abstract class GameEngine extends JPanel implements KeyListener, MouseListener
{

	boolean running = true;
	long lastTime;
	JFrame frame;
	int viewWidth = 1024;
	int viewHeight = 768;
	public GameEngine(JFrame parentFrame)
	{
		frame = parentFrame;
		//frame.setSize(viewWidth,viewHeight);
		frame.addKeyListener(this);
		frame.addMouseListener(this);
	}
	public abstract void loadContent();
	
	public void initialize()
	{
		Thread runThread = new Thread(){
			public void run()
			{
				lastTime = System.currentTimeMillis();
				while(running)
				{
					long currentTime = System.currentTimeMillis();
					long elapsedTime = currentTime-lastTime;
					updateGame(elapsedTime);
					repaint();
					lastTime = currentTime;
				}
			}
		};
		runThread.start();
	}
	public abstract void updateGame(long elapsedTime);
	@Override
	public void keyPressed(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
	
	public void stop()
	{
		running = false;
	}
}
