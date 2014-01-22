package pcclient.menus;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;



public class GameMenuPanel extends JPanel
{
	public ServerSelectionPanel serverSelectionPanel;
	public GameLobbyPanel gameLobbyPanel;
	public MainMenuPanel mainMenuPanel;
	public PlayerLobbyList playersInLobby;
	public JButton exitButton;
	public JButton backButton;
	
	public CardLayout cards;
	public JPanel cardPanel;
	
	public static String MAIN_MENU  = "main menu";
	public static String SERVER_SELECTION = "server selection"; 
	public static String LOBBY = "lobby";
	
	public GameMenuPanel(JFrame parent)
	{
		parent.add(this);
		initializeInterface();
		this.requestFocus();
		setView(MAIN_MENU);
	}
	private void initializeInterface()
	{
		//gridbag layout
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		//constraints for layout
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);
		constraints.fill = GridBagConstraints.BOTH;

		gameLobbyPanel = new GameLobbyPanel();
		serverSelectionPanel = new ServerSelectionPanel(null);
		mainMenuPanel = new MainMenuPanel();
		
		//add a cardlayout panel so that we can flip back and forth between views on the left side
		cards = new CardLayout();
		cardPanel = new JPanel(cards);
		cardPanel.add(mainMenuPanel, MAIN_MENU);
		cardPanel.add(serverSelectionPanel, SERVER_SELECTION);
		cardPanel.add(gameLobbyPanel, LOBBY);
		
		constraints.gridwidth = 1;
		constraints.gridheight = 4;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.weightx = 1;
		constraints.weighty = 1;
		add(cardPanel, constraints);
		
		
		//add an exit button on the top right of the right side
		exitButton = new JButton("");
		try {
		    Image img = ImageIO.read(getClass().getResource("Images/exit.png"));
		    img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		    exitButton.setIcon(new ImageIcon(img));
		  } catch (IOException ex) {
		  }
		constraints.gridheight = 1;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.weightx = 0;
		constraints.weighty = 0.1;
		add(exitButton, constraints);
		
		
		JLabel networkInfo = new JLabel();
		networkInfo.setBackground(Color.red);
		networkInfo.setFont(new Font("Arial", Font.PLAIN, 25));
		constraints.gridheight = 1;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.weightx = 0;
		constraints.weighty = 0.2;
		constraints.ipadx = 50;
		try {
			//get local ip
			String localIP = InetAddress.getLocalHost().getHostAddress();
			//get public ip
			URL url = new URL("http://myip.xname.org/");
		    InputStream is = url.openStream();  // throws an IOException
		    BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		    String publicIP = buf.readLine();
		    
		    networkInfo.setText("<HTML>Local IP: " +localIP+ "<BR>"+
					   "Public IP: " + publicIP +"<BR>"+
					   "Port: </HTML>");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		add(networkInfo, constraints);
		
		playersInLobby = new PlayerLobbyList();
		playersInLobby.setFont(new Font("Arial", Font.PLAIN, 25));
		constraints.gridheight = 1;
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.weightx = 0;
		constraints.weighty = 0.6;
		add(playersInLobby, constraints);
		
		//add a back button on the bottom right 
		backButton = new JButton("");
		try {
		    Image img = ImageIO.read(getClass().getResource("Images/back.png"));
		    img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		    backButton.setIcon(new ImageIcon(img));
		  } catch (IOException ex) {
		  }
		backButton.setFont(new Font("Arial", Font.PLAIN, 25));
		constraints.gridheight = 1;
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.weightx = 0;
		constraints.weighty = 0.1;
		add(backButton,constraints);
		
	}
	
	public void setObservers(Observable lobby)
	{
		gameLobbyPanel.setObservers(lobby);
		lobby.addObserver(playersInLobby);
	}
	public void setListener(ActionListener listener)
	{
		mainMenuPanel.setListener(listener);
		exitButton.addActionListener(listener);
		backButton.addActionListener(listener);
	}
	
	public void setView(String view)
	{
		cards.show(cardPanel, view);
	}
	
}
