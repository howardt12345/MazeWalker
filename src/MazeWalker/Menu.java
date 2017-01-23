package MazeWalker;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.*;

import javafx.stage.Stage;

public class Menu extends JPanel{
	private static final long serialVersionUID = 1L;
	public static long startTime, totalTime; //to get total time program running
	public enum GameState {Menu, Running, Stats, Exit}; //enum for state game is in
	public static GameState game = GameState.Menu; //gamestate
    public static boolean alreadyLaunched = false; //if program already launched
    public static boolean createdLog = false; //so that log file isn't overwritten when main called more than one times
    public static boolean runWithHints = false; //if player wants to run game with hints
    public static boolean night; //if game is in night
    public static String name;
    
	public static JFrame f = new JFrame(); //JFrame
	static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss"); //Date format, for logfile
    static Date date = new Date(); //Get date
	static GameController main = new GameController(); //instance of GameController
	
	public static void main(String[] args) throws FileNotFoundException
	{ 
		if (!createdLog){
	        File dir = new File("Game logs"); //directory ofww game logs
			if(dir.mkdir()) 
			    System.out.println("Directory created successfully"); 
			else
			    System.out.println("Directory already exists");
		    PrintStream out = new PrintStream(new FileOutputStream("Game logs/" + sdf.format(date) + "-gamelog.txt")); //Create log file
		    System.setOut(out); //Sets console output to logfile for debugging 
		    System.out.println("Log file created: " + sdf.format(date) + "-gamelog.txt"); //Notifies user that log file created
		    createdLog = true;
			Leaderboard.Load();
			Leaderboard.Sort();
			System.out.println("JavaFX version: " + com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
		}
		Menu menu = new Menu(); //Create new instance of Menu
		f.setSize(GameController.width, GameController.height); //Set menu width and height 
		f.setTitle("Maze Walker"); //Set title to "Maze Walker"
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Set default close operation
		f.setExtendedState(JFrame.MAXIMIZED_BOTH); //set frame to be mazimized
		f.setAlwaysOnTop(true); //set frame to be always on top of other programs
		
		switch (game){ //Switch statement for managing menu according to game state 
		case Menu:
			MainMenu(args); //Run Starting menu
			f.repaint();
			System.out.println("Game in main menu");
			break;
		case Running:
			startTime = System.nanoTime();
			System.out.println("Game running");
			if (!alreadyLaunched){
				GameController.main(args); //Executs main program
				alreadyLaunched = true;
			}
			else {
				try {
					main.restart(new Stage()); //restarts program
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			break;
		case Stats:
			System.out.println("Game on stats menu");
			Stats(args); //open stats menu
			break;
		case Exit:
			System.out.println("Game exiting");
			Exit();
			break;
		}
		f.add(menu); //add menu to jframe
		f.setVisible(true); //set visible
	}
	public void paintComponent(Graphics g)
	{
		try {
			BufferedImage img = ImageIO.read(new File("Resources/MenuBG.jpg"));
			Image dimg = img.getScaledInstance(GameController.width, GameController.height, Image.SCALE_SMOOTH);
			g.drawImage(dimg, 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Font font = new Font("verdana", Font.PLAIN, 20); //font
		Font titlefont = new Font("verdana", Font.PLAIN, 90); //title font
		g.setColor(Color.BLACK);
		g.setFont(titlefont);
		g.drawString("MAZE WALKER", GameController.width/2 - GameController.width/4,(int)Math.rint(GameController.width/35*5)); //title

		switch(game){ //Switch statement for drawing menu according to game state 
		case Menu: 
			g.setFont(font);
			g.drawString("Controls: WASD/Arrow Keys to move and use mouse to look around", 
					GameController.width/2 - GameController.width/4,(int)Math.rint(GameController.width/35*6));
			g.drawString("In Auto Solve, hit SPACE and watch the program solve the mazes", 
					GameController.width/2 - GameController.width/4,(int)Math.rint(GameController.width/35*7));
			g.drawString("Use Backspace to exit at any time", 
					GameController.width/2 - (int)Math.rint(GameController.width/7),(int)Math.rint(GameController.width/35*8));
			g.drawString("Name:", 
					GameController.width/2 - GameController.width/8,(int)Math.rint(GameController.width/35*9.5));
			Leaderboard.Paint(g);
			break;
		case Stats:
			g.setFont(font);
			if (GenerateMaze.levelct == 0){
				g.drawString("You exited the game before you even completed any mazes.", 
						GameController.width/2 - GameController.width/4,(int)Math.rint(GameController.width/35*7));
			}
			else if (GenerateMaze.levelct != 0 && GenerateMaze.levelct < GenerateMaze.totalLevels)
			g.drawString("You finished " + (GenerateMaze.levelct) + " of the mazes. Game has been running for " + totalTime + " seconds", 
					GameController.width/2 - GameController.width/4,(int)Math.rint(GameController.width/35*7));
			else
				g.drawString("Congratulations! You completed all the mazes in " + totalTime + " seconds!", 
						GameController.width/2 - GameController.width/4,(int)Math.rint(GameController.width/35*7));
			break;
		default:
			break;
		}
	}
	public static void MainMenu(String[] args) //main menu
	{
		JTextField playerName = new JTextField("player"); //textfield for player name
		playerName.setBounds(GameController.width/2 - GameController.width/16, (int)Math.rint(GameController.width/35*8.75), 
				(int)Math.rint(GameController.width/5.25), (int) Math.rint(GameController.width/35)); 
		playerName.addFocusListener(new FocusListener(){
			public void focusGained(FocusEvent arg0) {
				playerName.setText("");
			}
			public void focusLost(FocusEvent e) {
				playerName.setText("player");
			}
		});
		JCheckBox autoSolveCheckBox = new JCheckBox ("Auto Solve"); //checkbox for autosolve
		autoSolveCheckBox.setBounds(GameController.width/2 - GameController.width/8, (int)Math.rint(GameController.width/35*10), 
				GameController.width/8, (int) Math.rint(GameController.width/35));
		JCheckBox highlightPath = new JCheckBox ("Highlight Path"); //checkbox for highlight path
		highlightPath.setBounds(GameController.width/2, (int)Math.rint(GameController.width/35*10), 
				GameController.width/8, (int) Math.rint(GameController.width/35)); 
		JCheckBox music = new JCheckBox("Music"); //checkbox for music
		music.setBounds(GameController.width/2 - GameController.width/8, (int)Math.rint(GameController.width/35*11.25), 
				GameController.width/8, (int) Math.rint(GameController.width/35)); 	
		JCheckBox Night = new JCheckBox("Night"); //checkbox for music
		Night.setBounds(GameController.width/2, (int)Math.rint(GameController.width/35*11.25), 
				GameController.width/8, (int) Math.rint(GameController.width/35)); 
		JButton startButton = new JButton("Start"); //create start button
		startButton.setBounds(GameController.width/2 - GameController.width/8,(int)Math.rint(GameController.width/35*12.5), 
				GameController.width/4, (int) Math.rint(GameController.width/35));
		JButton exitButton = new JButton("Exit"); //create exit button
		exitButton.setBounds(GameController.width/2 - GameController.width/8, (int)Math.rint(GameController.width/35*13.75), 
				GameController.width/4, (int) Math.rint(GameController.width/35));
		startButton.addActionListener(new ActionListener() //Add actionlistener to the start button
		{ 
			public void actionPerformed(ActionEvent arg0) 
			{
				game = GameState.Running; //Switch gamestate to running
				//set values
				AutoSolve.autoSolve = autoSolveCheckBox.isSelected(); 
				runWithHints = highlightPath.isSelected();
				if (music.isSelected()) SoundManager.Play("Resources/Hans Zimmer - Time.wav");
				night = Night.isSelected();
				name = playerName.getText().replaceAll("\\s", ""); 
				//remove everything
				f.remove(playerName);
				f.remove(startButton);
				f.remove(autoSolveCheckBox);
				f.remove(highlightPath);
				f.remove(music);
				f.remove(Night);
				f.remove(exitButton);
				f.dispose(); 
				try {
					main(args); //run main
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		exitButton.addActionListener(new ActionListener() //Add actionlistener to the exit button
		{ 
			public void actionPerformed(ActionEvent arg0) 
			{
				game = GameState.Exit; //switch gamestage to exit
				try {
					main(args); //run main
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		f.add(playerName);
		f.add(startButton); //add start button
		f.add(autoSolveCheckBox); //add auto solve checkbox
		f.add(highlightPath); //add highlight path checkbox
		f.add(music); //add music checkbox
		f.add(Night); //add Night checkbox
		f.add(exitButton); //add exit button
	}
	public static void Stats(String[] args)
	{ //stats menu
		totalTime = (System.nanoTime() - startTime)/1000000000; //calculate total time running in seconds
		System.out.println("Player completed " + GenerateMaze.levelct + " mazes in "+ totalTime + " seconds"); //debug
		System.out.println("Stats menu"); //debug
		if (GenerateMaze.levelct != 0){
			String aid = "other"; //for leaderboard coloring
			if (AutoSolve.autoSolve) aid = "autosolve";
			else if (Menu.runWithHints) aid = "wHints";
			Leaderboard.write(Menu.name, aid, GenerateMaze.levelct, (int) Menu.totalTime);
		}
		JButton continueButton = new JButton("Close Program"); //continue button
		continueButton.setBounds(GameController.width/2 - GameController.width/8,(int)Math.rint(GameController.width/35*8.25), 
				GameController.width/6, (int) Math.rint(GameController.width/35)); //set bounds
		continueButton.addActionListener(new ActionListener() ////Add actionlistener to the button
		{
			public void actionPerformed(ActionEvent arg0) {
				f.remove(continueButton);
				game = GameState.Exit; //switch gamestage to exit
				try {
					main(args); //run main
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		f.add(continueButton); //addbutton
	}
	public static void Exit() //Exit
	{
		System.out.println("Exiting program"); //logs exit command
		System.exit(0); //exits program with no errors
	}
}
