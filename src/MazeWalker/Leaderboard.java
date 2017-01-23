package MazeWalker;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Leaderboard {
	public static String array[][];
	public static String file = "leaderboards.txt";
	public static FileReader fileR = null;
	public static FileWriter fw;
	public static Scanner scIn = new Scanner (System.in);
	public static int rows, columns;
	
	public static void Load(){ //Loads database
		try {
			fileR = new FileReader (file); //New FileReader
			@SuppressWarnings("resource")
			Scanner input = new Scanner (fileR); //Sets scanner input to file
			rows = input.nextInt(); //Sets rows
			columns = input.nextInt(); //Sets columns
			array = new String [rows+rows][columns]; //Sets size of array
			for (int a = 0; a < rows; a++){
				for (int b = 0; b < columns; b++){
					array [a][b] = input.next(); //Loads values to array
				}
			}
		}
		catch (FileNotFoundException e){
			System.err.println(e); 
		}
	}
	public static void Sort () { //Sorts database
		boolean timeSort = false, mazeSort = false;
		int [] mazeCompleteTemp = new int[array.length], mazeTimeTemp = new int[array.length];
		for (int a = 0; a < rows; a++){
			mazeCompleteTemp[a] = Integer.parseInt(array[a][2]); //sets values in mazeCompleteTemp to sort
			mazeTimeTemp[a] = Integer.parseInt(array[a][3]); //ets value of mazeTimeTemp to sort
		}
		timeSort = true;
		while (timeSort){ //Sorts array for time taken from least to greatest
			timeSort = false;
			for (int ctr = array.length-1; ctr > 0; ctr--){
				if (mazeTimeTemp[ctr] < mazeTimeTemp[ctr-1]){
					int tmp = mazeTimeTemp[ctr];
					int tmp1 = mazeCompleteTemp[ctr];
					String temp1 = array[ctr][0];
					String temp2 = array[ctr][1];
					String temp3 = array[ctr][2];
					String temp4 = array[ctr][3];
					mazeTimeTemp[ctr] = mazeTimeTemp[ctr-1];
					mazeCompleteTemp[ctr] = mazeCompleteTemp[ctr-1];
					array[ctr][0] = array[ctr-1][0];
					array[ctr][1] = array[ctr-1][1];
					array[ctr][2] = array[ctr-1][2];
					array[ctr][3] = array[ctr-1][3];
					mazeTimeTemp[ctr-1] = tmp;
					mazeCompleteTemp[ctr-1] = tmp1;
					array[ctr-1][0] = temp1;
					array[ctr-1][1] = temp2;
					array[ctr-1][2] = temp3;
					array[ctr-1][3] = temp4;
					timeSort = true;
				}
			}	
		}
		mazeSort = true;
		while (mazeSort){ //Sorts array for mazes complete from greatest to least
			mazeSort = false;
			for (int ctr = 0; ctr < array.length-1; ctr++){
				if (mazeCompleteTemp[ctr] < mazeCompleteTemp[ctr+1]){
					int tmp = mazeCompleteTemp[ctr];
					String temp1 = array[ctr][0];
					String temp2 = array[ctr][1];
					String temp3 = array[ctr][2];
					String temp4 = array[ctr][3];
					mazeCompleteTemp[ctr] = mazeCompleteTemp[ctr+1];
					array[ctr][0] = array[ctr+1][0];
					array[ctr][1] = array[ctr+1][1];
					array[ctr][2] = array[ctr+1][2];
					array[ctr][3] = array[ctr+1][3];
					mazeCompleteTemp[ctr+1] = tmp;
					array[ctr+1][0] = temp1;
					array[ctr+1][1] = temp2;
					array[ctr+1][2] = temp3;
					array[ctr+1][3] = temp4;
					mazeSort = true;
				}
			}	
		}
	}
	public static void write(String str, String aid, int mazect, int time){ //Writes into database
		array[rows][0] = str; 
		array[rows][1] = aid;
		array[rows][2] = ""+mazect;
		array[rows][3] = ""+time;
		String in = null;
		try {
			fw = new FileWriter(file);
			fw.write(""+(rows+1)+"\r\n");
			fw.write(""+columns+"\r\n");
			for (int i = 0; !(array[i][0] == null); i++){
				in = array[i][0] + " " + array[i][1] + " " + array[i][2] + " " + array[i][3] + "\r\n";
				fw.write(in);
			}
			scIn.close();
			fw.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	public static void Print(){
		for (int a = 0; a < rows; a++){
			for (int b = 0; b < columns; b++){
				System.out.print(" " + array [a][b]); //prints out array
			}
			System.out.println("");
		}
	}
	
	public static void Paint(Graphics g){ //Paints score
		g.drawString("LeaderBoards", 20, 20);
		g.drawString("Name:", 20, 50);
		g.drawString("Mazes:", 175, 50);
		g.drawString("Time:", 250, 50);
		for (int a = 0; a < (rows < 6 ? rows : 6); a++){
			g.setColor(Color.blue);
			if (array[a][1].equals("autosolve")) g.setColor(Color.gray); //gray if autosolve
			if (array[a][1].equals("wHints")) g.setColor(Color.red); //red if with hints
			g.drawString(array[a][0], 20, (a*25)+75);
			g.drawString(array[a][2], 175, (a*25)+75);
			g.drawString(array[a][3], 250, (a*25)+75);
		}
	}
}