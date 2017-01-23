package MazeWalker;
import javafx.scene.PerspectiveCamera;

public class Collision extends PathRecord{
	public static void Collide(PerspectiveCamera camera, double wallSize, int x, int y, boolean collided) {
		System.out.println("Collided");
		if (!collided){
			if (GenerateMaze.maze[lastLocY][lastLocX] == 0 || GenerateMaze.maze[lastLocY][lastLocX] == 3){
				System.out.println("Collision Recognized");
				if (lastLocX < x && lastLocY == y){ //Collision with left part of wall
					System.out.println("Player collided with left");
					collided = true;
					GameController.camZ = Movement.camZ = ((wallSize * lastLocX) + ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocX > x && lastLocY == y){ //Collision with right part of wall
					System.out.println("Player collided with right");
					collided = true;
					GameController.camZ = Movement.camZ = ((wallSize * lastLocX) - ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocY < y && lastLocX == x){ //Collision with top part of wall
					System.out.println("Player collided with up");
					collided = true;
					GameController.camX = Movement.camX = ((wallSize * lastLocY) + ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocY > y && lastLocX == x){ //Collision with bottom part of wall
					System.out.println("Player collided with down");
					collided = true;
					GameController.camX = Movement.camX = ((wallSize * lastLocY) - ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocY > y && lastLocX < x){ //Collision with top right corner
					System.out.println("Player collided with top right corner");
					collided = true;
					GameController.camZ = Movement.camZ = ((wallSize * lastLocX) + ((wallSize/2) - (wallSize/33)));
					GameController.camX = Movement.camX = ((wallSize * lastLocY) - ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocY > y && lastLocX > x){ //Collision with top left corner
					System.out.println("Player collided with top left corner");
					collided = true;
					GameController.camZ = Movement.camZ = ((wallSize * lastLocX) - ((wallSize/2) - (wallSize/33)));
					GameController.camX = Movement.camX = ((wallSize * lastLocY) - ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocY < y && lastLocX < x){ //Collision with bottom right corner
					System.out.println("Player collided with bottom right corner");
					collided = true;
					GameController.camZ = Movement.camZ = ((wallSize * lastLocX) + ((wallSize/2) - (wallSize/33)));
					GameController.camX = Movement.camX = ((wallSize * lastLocY) + ((wallSize/2) - (wallSize/33)));
				}
				if (lastLocY < y && lastLocX > x){ //Collision with bottom left corner
					System.out.println("Player collided with bottom left corner");
					collided = true;
					GameController.camZ = Movement.camZ = ((wallSize * lastLocX) - ((wallSize/2) - (wallSize/33)));
					GameController.camX = Movement.camX = ((wallSize * lastLocY) + ((wallSize/2) - (wallSize/33)));
				}
			}
		}
		collided = false;
	}
}
