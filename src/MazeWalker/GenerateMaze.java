package MazeWalker;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.transform.Translate;

public class GenerateMaze extends GameController{
	public static double wallSize = 8;
	public static int columns, rows;
	public static int levelct = 0, totalLevels = 10;
	public static int [][] maze;
	
	public static void FileRead (String filename)
	{ //This reads the File
		try {
			FileReader file = new FileReader (filename); //Finds the file. Make sure that the file has the necessary requirements beforehand
			@SuppressWarnings("resource")
			Scanner input = new Scanner (file); //Scanner for file reading
			rows = input.nextInt(); //Scanning file and assigning variables
			columns = input.nextInt(); //Scanning file and assigning variables
			maze = new int [rows][columns]; //Scanning file and assigning variables
			for (int a = 0; a <rows; a++){ 
				for (int b = 0; b < columns; b++){
					maze [a][b] = input.nextInt(); //Scanning file and assigning variables
				}
			}
			for (int a = 0; a <rows; a++){ //Prints out *unsolved* maze 
				for (int b = 0; b < columns; b++){
					System.out.print(" " + maze[a][b]);
				}
				System.out.println(" ");
			}
			System.out.println(filename + " has been generated");
		}
		catch (FileNotFoundException e){
			System.err.println(e); //Spits out an error if it can't read the file
		}
		if (AutoSolve.autoSolve || Menu.runWithHints){
			MazePathFinder.SolveMaze(filename); //solves maze
			AutoSolve.LoadValues(); //load values to AutoSolve
		}
	}
	public static void PlaceCamera()
	{ //place camera in scene
		for (int a = 0; a < rows; a++){ 
			for (int b = 0; b < columns; b++){
				if (maze[a][b] == 3){
					camX = Movement.camX = (wallSize * a);
					camZ = Movement.camZ = (wallSize * b);
					PathRecord.locX = b;
					PathRecord.locY = a;
				}
			}
		}
	}
	public static void Generate (Group level)
	{ //Generate maze
		if (Menu.runWithHints){ //if running program with hints
			for (int a = 0; a < rows; a++){ 
				for (int b = 0; b < columns; b++){
					if (AutoSolve.mazeArray[a][b] == 0){
						//start highlighted ground material
						PhongMaterial mat = null;
						if (Menu.night) mat = new PhongMaterial(Color.web("#313075"));
						else mat = new PhongMaterial(Color.web("#7270ff"));
						mat.setDiffuseMap(new Image ("file:Resources/Ground.jpg", false));
						Box highlightedGround = new Box(wallSize, 0, wallSize); //create highlighted ground
						highlightedGround.setCache(true); //cache the note
						highlightedGround.setCacheHint(CacheHint.SPEED); //optimize node
						highlightedGround.setMaterial(mat);
						highlightedGround.setDrawMode(DrawMode.FILL);
						highlightedGround.getTransforms().setAll(new Translate(wallSize * a, -0.01, wallSize * b));
						level.getChildren().add(highlightedGround); //add highlighted ground
					}	
				}
			}
		}
		for (int a = 0; a < rows; a++){  //generate actual maze
			for (int b = 0; b < columns; b++){
				if (maze[a][b] == 1){
					//wall material
					PhongMaterial mat = null;
					if (Menu.night) mat = new PhongMaterial (Color.web("#757575"));
					else mat = new PhongMaterial (Color.web("#BDBDBD"));
					mat.setDiffuseMap(new Image ("file:Resources/Wall.jpg", false));
					Box wall = new Box(wallSize, wallSize/2, wallSize); //create wall
					wall.setCache(true); //cache the note
					wall.setCacheHint(CacheHint.SPEED); //optimize node
					wall.setMaterial(mat);
					wall.setDrawMode(DrawMode.FILL);
					wall.getTransforms().setAll(new Translate(wallSize * a, -wallSize/4, wallSize * b));
					level.getChildren().add(wall); //add wall
				}
				else if (maze[a][b] == 3){
					//start ground material
					PhongMaterial mat = new PhongMaterial(Color.GREEN);
					mat.setDiffuseMap(new Image ("file:Resources/Ground.jpg", false));
					Box start = new Box(wallSize, 0, wallSize); //create start
					start.setCache(true); //cache the note
					start.setCacheHint(CacheHint.SPEED); //optimize node
					start.setMaterial(mat);
					start.setDrawMode(DrawMode.FILL);
					start.getTransforms().setAll(new Translate(wallSize * a, -0.02, wallSize * b));
					level.getChildren().add(start); //add start 
				}
				else if (maze[a][b] == 9){
					//finish ground material
					PhongMaterial mat = new PhongMaterial(Color.RED);
					mat.setDiffuseMap(new Image ("file:Resources/Ground.jpg", false));
					Box finish = new Box(wallSize, 0, wallSize); //create finish
					finish.setCache(true); //cache the note
					finish.setCacheHint(CacheHint.SPEED); //optimize node
					finish.setMaterial(mat);
					finish.setDrawMode(DrawMode.FILL);
					finish.getTransforms().setAll(new Translate(wallSize * a, -0.02, wallSize * b));
					level.getChildren().add(finish); //add finish
				}
			}
		}
	}
}