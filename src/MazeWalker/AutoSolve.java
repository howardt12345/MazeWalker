package MazeWalker;

public class AutoSolve extends Movement{
	
	public static GameController main; //new instance of GameController
	public static enum Facing {UP, DOWN, LEFT, RIGHT} //For orientation in the maze
	public static Facing facing = Facing.RIGHT; //direction player is facing
	public static Maze maze;
	public static int[][] mazeArray;
	public static int lastLocX, lastLocY;
	public static boolean solved = false;
	public static boolean autoSolve = false;
	public static boolean setupDone = false;
	public static int refreshRate = 10;
	
	public static void LoadValues() //load array into database
	{
		mazeArray = new int[GenerateMaze.rows][GenerateMaze.columns];
		for (int a = 0; a < GenerateMaze.rows; a++){ 
			for (int b = 0; b < GenerateMaze.columns; b++){
            	mazeArray[a][b] = Integer.parseInt(""+Maze.matrix[a][b]);
			}
		}
	}
	public static boolean CheckLeft(int locX, int locY) //to rotate camera
	{
		if (locX-1 >=0 && (mazeArray [locY][locX - 1] !=1) && (mazeArray [locY][locX - 1] !=8)){ //Looks to the LEFT
			facing = Facing.LEFT;
			mazeArray[locY][locX] = 8;
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean CheckUp(int locX, int locY) //to rotate camera
	{
		if (locY-1 >=0 && (mazeArray [locY - 1][locX] !=1) && (mazeArray [locY - 1][locX] !=8)){ //Looks to the up
			facing = Facing.UP;
			mazeArray[locY][locX] = 8;
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean CheckRight(int locX, int locY) //to rotate camera
	{
		if (locX+1 < mazeArray[0].length && (mazeArray [locY][locX + 1] !=1) && (mazeArray [locY][locX + 1] !=8)){ //Looks to the RIGHT
			facing = Facing.RIGHT;
			mazeArray[locY][locX] = 8;
			return true;
		}
		else {
			return false;
		}
	}
	public static boolean CheckDown(int locX, int locY) //to rotate camera
	{
		if (locY+1 < mazeArray.length && (mazeArray [locY + 1][locX] !=1) && (mazeArray [locY + 1][locX] !=8)){ //Looks to the down
			mazeArray[locY][locX] = 8;
			facing = Facing.DOWN;
			return true;
		}
		else {
			return false;
		}
	}
	public static void UpdatePosition(int locX, int locY){
		switch (facing){ //to rotate camera
		case DOWN:
			System.out.println("Player facing down");
			rotationH = 90;
			if (CheckLeft(locX, locY)) break;
			else if (CheckDown(locX, locY)) break; 
			else if (CheckRight(locX, locY)) break;
			else if (CheckUp(locX, locY)) break;
			break;
		case UP:
			System.out.println("Player facing up");
			rotationH = -90;
			if (CheckRight(locX, locY)) break;
			else if (CheckUp(locX, locY)) break; //
			else if (CheckLeft(locX, locY)) break;
			else if (CheckDown(locX, locY)) break;
			break;
		case LEFT:
			System.out.println("Player facing left");
			rotationH = 180;
			if (CheckUp(locX, locY)) break;
			else if (CheckLeft(locX, locY)) break; //
			else if (CheckDown(locX, locY)) break;
			else if (CheckRight(locX, locY)) break;
			break;
		case RIGHT:
			System.out.println("Player facing right");
			rotationH = 0;
			if (CheckDown(locX, locY)) break;
			else if (CheckRight(locX, locY)) break; //
			else if (CheckUp(locX, locY)) break;
			else if (CheckLeft(locX, locY)) break;
			break;
		}
		Movement.MoveForward(); //move camera
		Movement.Update(); //update camera position
		Movement.UpdateRotation(); //update camera rotation
	}
}
