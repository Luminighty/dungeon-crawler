/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Level;
import Resources.FrameResource;
import Resources.ResourceLoader;
import Resources.ResourcePack;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Vector;
import java.util.logging.Logger;
import javax.swing.*;

/**
 *
 * @author Luminight
 */
public class MainMenu extends JFrame {

	public static MainMenu main = null;
	public static Image icon;
	
	Vector<Level> levels = new Vector();
	
	JList levelSelector;
	
	public MainMenu() {
		if(main != null)
			return;
		main = this;
		setTitle("Dungeon Crawler");
		setSize(400,300);
		setResizable(false);
		CenterWindow(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(exitEvent);
		AddComponents();
		
		icon = ResourceLoader.LoadImage("/Resources/icon.png");
		setIconImage(MainMenu.icon);
		setVisible(true);
		
	}
	
	void AddComponents() {
	
		JPanel main = new JPanel();
		
		JPanel left = new JPanel();
		JPanel right = new JPanel();
		
		setLayout(new BorderLayout());
		main.setLayout(new GridLayout(1,2,10,5));
		
		AddLeftSide(left);
		AddRightside(right);
		
		main.add(left);
		main.add(right);
		
		add(main, BorderLayout.CENTER);
		add(Box.createVerticalStrut(10),BorderLayout.SOUTH);
		add(Box.createHorizontalStrut(10),BorderLayout.EAST);
		add(Box.createHorizontalStrut(10),BorderLayout.WEST);
		
	}
	
	void AddLeftSide(JPanel p) {
		
		JPanel holder = new JPanel();
		
		holder.setLayout(new BorderLayout());
		JLabel label = new JLabel("Levels:");
		label.setVerticalAlignment(SwingConstants.BOTTOM);
		holder.add(label, BorderLayout.NORTH);
		
		Vector levels = null;
		levels = getLevels();
		JList list = new JList(levels);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(-1);
		list.setLayoutOrientation(JList.VERTICAL);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		list.setSelectedIndex(0);
		levelSelector = list;
		holder.add(listScroller, BorderLayout.CENTER);
		
		p.setLayout(new BorderLayout());
		p.add(holder, BorderLayout.CENTER);
		p.add(Box.createVerticalStrut(22), BorderLayout.NORTH);
	}
	
	Vector getLevels() {
		levels = new Vector<Level>();
		Vector names = new Vector();
		File fold = new File(ResourceLoader.GetRunFolder(Level.MAPFOLDER).getPath());
		File[] files = fold.listFiles();
		for(File f : files) {
			Level l = new Level(f.getName());
			levels.add(l);
			names.add(l.name + " - " + l.difficulty.toString());
		}
		
		return names;
	}
	
	void AddRightside(JPanel p) {
		p.setLayout(new GridLayout(7,1, 10, 5));
		
		JLabel title = new JLabel("Dungeon Crawler");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setAlignmentX(CENTER_ALIGNMENT);
		title.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		p.add(title);
		
		p.add(AddMenuButton("Start", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			StartGame();
			}
		}));
		p.add(AddMenuButton("Scoreboard", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowScoreboard();
			}
		}));
		p.add(AddMenuButton("Options", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowOptions();
			}
		}));
		p.add(AddMenuButton("About", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowAbout();
			}
		}));
		p.add(AddMenuButton("Credits", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ShowCredits();
			}
		}));
		p.add(AddMenuButton("Exit", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tryExitApplication();
			}
		}));
		
		
	}
	
	JButton AddMenuButton(String text, ActionListener listener) {
		JButton btn = new JButton(text);
		btn.addActionListener(listener);
		btn.setMargin(new Insets(5, 10, 5, 10));
		return btn;
	}
	
	void StartGame() {
		int index = levelSelector.getSelectedIndex();
		if(index == -1) {
			showDialog(this, "Level Select", "Please select a level first!", new Object[] {"Ok"});
			return;
		}
		setVisible(false);
		MainWindow window = new MainWindow(levels.get(index));
	}
	
	void ShowScoreboard() {
		int index = levelSelector.getSelectedIndex();
		if(index == -1) {
			showDialog(this, "Level Select", "Please select a level first!", new Object[] {"Ok"});
			return;
		}
		new Dialog_Scoreboard(this, levels.get(index).name);
	}
	
	void ShowOptions() {
		new Dialog_Controls(this);
	}
	
	void ShowAbout() {
		new Dialog_About(this);
		
	}
	
	void ShowCredits() {
		new Dialog_Credits(this);
		
	}
	
	static void CenterWindow(Window w) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int width = w.getSize().width;
		int height = w.getSize().height;
		int x = (dim.width - width) / 2;
		int y = (dim.height - height) / 2;
		w.setLocation(x,y);
	}
	
	public boolean tryExitApplication() {
		System.exit(0);
		return true;
	}
	
	WindowAdapter exitEvent = new WindowAdapter() { @Override public void windowClosing(WindowEvent e) {tryExitApplication();} };
	
    public static boolean showDialog(Component Parent, String Title, String Message, Object[] options) {
		int option = JOptionPane.showOptionDialog(Parent, Message, Title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		return (option == JOptionPane.OK_OPTION);
    }
	
}
