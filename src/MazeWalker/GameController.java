package MazeWalker;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileNotFoundException;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;

public class GameController extends Application{
	
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //Dimension
	//primitives
    public static boolean alreadyLaunched = false, reloaded = false;
	public static double camX = 0, camY = -2, camZ = -25; //Camera location
	public static double rotationH, rotationV; //Camera rotation for movement stored here
	public static int width = (int) screenSize.getWidth(), height = (int) screenSize.getHeight(); //Dimensions of frame
	public static boolean moveForward, moveLeft, moveBack, moveRight;
	//JavaFX
	public static Scene scene; //scene
	public static PerspectiveCamera camera = new PerspectiveCamera(true); //camera
	public static Group root = new Group();     
	public static SubScene subScene = new SubScene(root, width,height, true, SceneAntialiasing.DISABLED);
    
	public static Parent createContent() throws Exception {
		//Material of ground
		Image groundTex = new Image ("file:Resources/Ground.jpg", false);
		PhongMaterial groundMat = new PhongMaterial();
		if (Menu.night) groundMat = new PhongMaterial (Color.web("#757575"));
		else groundMat = new PhongMaterial (Color.web("#BCAAA4"));
		groundMat.setDiffuseMap(groundTex);
		//Creates a grid of boxes with the texture for the ground
		for (int a = 0; a < GenerateMaze.wallSize * GenerateMaze.rows; a += GenerateMaze.wallSize){
			for (int b = 0; b <  GenerateMaze.wallSize * GenerateMaze.columns; b += GenerateMaze.wallSize){
				Box ground = new Box(GenerateMaze.wallSize, 0, GenerateMaze.wallSize);
				ground.setCache(true); //cache the note
				ground.setCacheHint(CacheHint.SPEED); //optimize node
				ground.setMaterial(groundMat);
				ground.setDrawMode(DrawMode.FILL);
				ground.getTransforms().setAll(new Translate(a, 0, b));
				root.getChildren().add(ground);
			}
		}
		//Material of sky 
		Image skyTex = null;
		if (Menu.night) skyTex = new Image ("file:Resources/SkyNight.PNG", false);
		else skyTex = new Image ("file:Resources/SkyDay.PNG", false);
		PhongMaterial skyMat = new PhongMaterial();
		skyMat.setDiffuseMap(skyTex);
		//Create grid of sky
		for (int a = -10000; a < 10000; a += 500){
			for (int b = -10000; b < 10000; b += 500){
				Box Sky = new Box(500, 0, 500);
				Sky.setCache(true); //cache the note
				Sky.setCacheHint(CacheHint.SPEED); //optimize node
				Sky.setMaterial(skyMat);
				Sky.setDrawMode(DrawMode.FILL);
				Sky.getTransforms().setAll(new Translate(a, -50, b));
				root.getChildren().add(Sky);
			}
		}
		//set camera view clips
		camera.setFarClip(1000); 
		camera.setNearClip(0.01);
		camera.setFieldOfView(40.0); //set field of view of camera
		
		AmbientLight light = new AmbientLight(); //Light
		light.setColor(Color.FLORALWHITE);
		light.setRotate(10);
		
		//Add components
		root.getChildren().add(light); 
		root.getChildren().add(camera);
		GenerateMaze.Generate(root); //Generate the maze
		
		if (Menu.night)
		subScene.setFill(Color.web("#02041b"));
		else subScene.setFill(Color.SKYBLUE);
		subScene.setCamera(camera);
		Group group = new Group();
		group.getChildren().add(subScene); //add group to subscene
		return group;
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		StartGame(primaryStage); //Start game
	}
	public void StartGame(Stage primaryStage) throws Exception {
		moveForward = false; moveBack = false; moveLeft = false; moveRight = false;
		GenerateMaze.FileRead("Mazes/" + "maze" + GenerateMaze.levelct + ".txt");
		GenerateMaze.PlaceCamera(); //finds start and places camera
		Movement.Update(); //Set camera position
		
		scene = new Scene(createContent()); //Create new scene
		scene.setCursor(Cursor.NONE); //Get rid of cursor on screen
		//Set camera position:
		primaryStage.setResizable(false); //prohibits user from resizing application
		primaryStage.setFullScreen(true); //make program fullscreen
		primaryStage.setTitle("Maze Walker"); //sets title
		primaryStage.setAlwaysOnTop(true); //Set window to always be on top
		
		if (!AutoSolve.autoSolve) Movement.MouseMove(); //if autosolve is false allow user to rotate using mouse
       	scene.setOnKeyPressed(e ->{ //Set key events
        	if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.W){ //if up pressed
        		Movement.MoveForward(); //move forward
        	}
        	if (e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.A){ //if left pressed
        		Movement.MoveLeft(); //move left 
        	}
        	if (e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.S){ //if down pressed
        		Movement.MoveBack(); //move down
        	}
        	if (e.getCode() == KeyCode.RIGHT || e.getCode() == KeyCode.D ){ //if right pressed
        		Movement.MoveRight(); //move right
        	}
        	if (e.getCode() == KeyCode.Q) {
        		Movement.camY++;
        	}
        	if (e.getCode() == KeyCode.E) {
        		Movement.camY--;
        	}
			if (e.getCode() == KeyCode.BACK_SPACE){ //if backspace
				Menu.game = Menu.GameState.Stats; //go to stats menu
				primaryStage.close(); //close stage
				try {
					Menu.main(null);
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				}
			}
    		Movement.Update(); //Update position
			if (PathRecord.Finished){ //if program has solved maze
				if (GenerateMaze.levelct < GenerateMaze.totalLevels - 1){ //if program has not run out of programs
					System.out.println("Loading next level"); //debug loading next level
					root.getChildren().removeAll(); //remove everything
					GenerateMaze.levelct++; //increment level
					primaryStage.close(); //close stage
					try {
						restart(new Stage()); //restart program
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					PathRecord.Finished = false;
				}
				else {
					GenerateMaze.levelct++; //increment level
					Menu.game = Menu.GameState.Stats; //set menu to stats
					primaryStage.close(); //close stage
					try {
						Menu.main(null); //run menu 
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
				
		primaryStage.setScene(scene); //set stage to scene
		primaryStage.show(); //show stage
	}
	public void cleanup() {
		root.getChildren().clear(); //clear everything in group
	}
	public void restart(Stage stage) throws Exception {
	    cleanup(); //cleanup
	    StartGame(stage); //start game
	}

	public static void main(String[] args) {
		if (!alreadyLaunched){ //if program has not already launched
			launch(args); //launch program
			alreadyLaunched = true; //set alreadyLaunched to true 
		}
		else {
			GameController main = new GameController(); //new instance of GameController
			try {
				main.restart(new Stage()); //restart 
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
