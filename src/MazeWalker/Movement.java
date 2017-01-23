package MazeWalker;
import javafx.scene.transform.*;

import java.awt.AWTException;
import java.awt.Robot;

public class Movement extends GameController{
	public static double camX, camY = -2, camZ; //Camera location
	public static double speed = 0.6, sensitivity = 0.1;
    public static double oldX, newX, oldY, newY;
    public static double dx = 0, dy = 0;
    
	public static void MouseMove()
	{//mouse movement
        scene.setOnMouseMoved(event -> {
			if (event.getSceneX() <= 100 || event.getSceneX() >= width - 100
					|| event.getSceneY() <= 100 || event.getSceneY() >= height - 100){ //if mouse is near edge
				CenterMouse(); //center the mouse
				oldX = newX = width/2; //reset
				oldY = newY = height/2; //reset
				dx = 0; 
				dy = 0;
			}
			else {
				//get old mouse position
				oldX = newX; 
				oldY = newY;
				//get new mouse position
				newX = event.getSceneX();
				newY = event.getSceneY();
				//get distance mouse moved
				dx = (newX -oldX)*sensitivity;
				dy = (newY -oldY)*sensitivity;
			}
			rotationH += dx; //add x to camera rotation X
			rotationV -= dy; //subtract y to camera rotation Y (invert)
			System.out.println("current camera rotation: " + rotationH + ", " + rotationV);
			if (rotationV >= 90) rotationV = 90; //if rotationV is over 90 prevent from going over
			if (rotationV <= -90) rotationV = -90; //if rotationV is under 90 prevent from going under
			UpdateRotation();
        });
	}
	public static void MoveForward()
	{ //move forward
		camZ += (Math.cos(Math.toRadians(rotationH)))*speed;
		camX += (Math.sin(Math.toRadians(rotationH)))*speed;
		System.out.println("Forward");
	}
	public static void MoveLeft()
	{ //move left
		camZ += (Math.cos(Math.toRadians(rotationH)+90)*-1)*speed;
		camX += (Math.sin(Math.toRadians(rotationH)+90)*-1)*speed;
		System.out.println("Left");
	}
	public static void MoveBack()
	{ //move back
		camZ += (Math.cos(Math.toRadians(rotationH))*-1)*speed;
		camX += (Math.sin(Math.toRadians(rotationH))*-1)*speed;
		System.out.println("Down");
	}
	public static void MoveRight()
	{ //move right
		camZ += (Math.cos(Math.toRadians(rotationH)+90))*speed;
		camX += (Math.sin(Math.toRadians(rotationH)+90))*speed;
		System.out.println("Right");
	}
	public static void UpdateRotation()
	{ //update camera rotation
		camera.getTransforms().setAll(
				new Rotate (rotationH, Rotate.Y_AXIS),
				new Rotate (rotationV, Rotate.X_AXIS));
	}
	public static void Update()
	{ //update camera position
    	PathRecord.CheckArea(GenerateMaze.wallSize, scene, root); //check player position in maze
    	//set csmera translates
		camera.setTranslateX(camX); 
		camera.setTranslateY(camY);
		camera.setTranslateZ(camZ);
		System.out.println("Player location: " + camX + ", " + camZ); //debug player location
	}
	public static void CenterMouse()
	{ //center mouse
		try {
			Robot r = new Robot(); //new Robot
			r.mouseMove((int)width/2, (int)height/2); //move mouse to center of screen
			System.out.println("Centered mouse"); //debug centered mouse
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
}
