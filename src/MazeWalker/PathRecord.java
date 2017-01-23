package MazeWalker;
import javafx.scene.Group;
import javafx.scene.Scene;

public class PathRecord extends Movement {
	public static int lastLocX, lastLocY, locX, locY; //location of player
	public static boolean Finished = false; //if user finished maze
	public static void CheckArea(double wallSize, Scene scene, Group root)
	{ //check player area
		for (int a = 0; a < GenerateMaze.rows; a++){ 
			for (int b = 0; b < GenerateMaze.columns; b++){			
				if (GenerateMaze.maze[a][b] == 0){ //if player is in open
					if (camX >= (a*wallSize) - (wallSize/2) && camX <= (a*wallSize) + (wallSize/2)
							&& camZ >= (b*wallSize) - (wallSize/2) && camZ <= (b*wallSize) + (wallSize/2)){
						System.out.println("Open"); //debug open
						//set record of positions
						locX = b;
						locY = a;
						if (lastLocX != locX) lastLocX = locX;
						if (lastLocY != locY) lastLocY = locY;
					}
				}
				if (GenerateMaze.maze[a][b] == 1){ //if player is in wall
					if (camX >= (a*wallSize) - (wallSize/2) && camX <= (a*wallSize) + (wallSize/2)
							&& camZ >= (b*wallSize) - (wallSize/2) && camZ <= (b*wallSize) + (wallSize/2) && camY >=-wallSize/2){
						System.out.println("Wall");//debug wall
						//set record of positions
						locX = b;
						locY = a;
						Collision.Collide(GameController.camera, wallSize, b, a, false);
						System.out.println("Wall Collided at: " + b + ", " + a);
					}
				}
				if (GenerateMaze.maze[a][b] == 3){ //if player is at start
					if (camX >= (a*wallSize) - (wallSize/2) && camX <= (a*wallSize) + (wallSize/2)
							&& camZ >= (b*wallSize) - (wallSize/2) && camZ <= (b*wallSize) + (wallSize/2)){
						System.out.println("At Start");//debug start
						//set record of positions
						locX = b;
						locY = a;
						if (lastLocX != locX) lastLocX = locX;
						if (lastLocY != locY) lastLocY = locY;
					}
				}
				if (GenerateMaze.maze[a][b] == 9){ //if player is at finish
					if (camX >= (a*wallSize) - (wallSize/2) && camX <= (a*wallSize) + (wallSize/2)
							&& camZ >= (b*wallSize) - (wallSize/2) && camZ <= (b*wallSize) + (wallSize/2)){
						System.out.println("At Finish, maze completed!");//debug finish
						//set record of positions
						locX = b;
						locY = a;
						if (lastLocX != locX) lastLocX = locX;
						if (lastLocY != locY) lastLocY = locY;
						Finished = true; //set finish to true
					}
				}
			}
		}
		if (camX < 0 && camZ < 0) {
			System.out.println("Player isn't even on map"); //debug player isn't on map
		}
		else{
			System.out.println("Current location: " + locX + ", " + locY + ". "
					+ "Last Recorded Position: " + lastLocX + ", " + lastLocY); //debug player current position in maze
		}
	}
}
