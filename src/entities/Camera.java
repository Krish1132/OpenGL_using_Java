package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(0,0,0);
	private float pitch = 10;
	private float yaw ;
	private float roll;
	
	private Player player;
	
	public Camera(Player player){
		this.player = player;
	}
	
	public void move(){
//		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
//			position.z-=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
//			position.z+=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
//			position.x+=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
//			position.x-=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
//			position.y+=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
//			position.y-=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
//			this.yaw-=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
//			this.yaw+=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_L)){
//			this.pitch-=0.7f;
//		}
//		if(Keyboard.isKeyDown(Keyboard.KEY_M)){
//			this.pitch+=0.7f;
//		}
		calculateZoom();
		calculatePitch();
		calculateAngleAroundPlayer();
		float horizontalDistance = calculateHorizontalDistance();
		float verticalDistance = calculateVerticalDistance();
		calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (player.getRotY() + angleAroundPlayer);
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}

	public float getRoll() {
		return roll;
	}
	
	private void calculateCameraPosition(float horizDistance, float verticDistance) {
		float theta = player.getRotY() + angleAroundPlayer;
		float offsetX = (float) (horizDistance * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horizDistance * Math.cos(Math.toRadians(theta)));
		position.x = player.getPosition().x - offsetX;
		position.z = player.getPosition().z - offsetZ;
		position.y = player.getPosition().y + verticDistance;
	}
	
	private float calculateHorizontalDistance() {
		return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
	}
	
	private float calculateVerticalDistance() {
		return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		if(distanceFromPlayer >= 30 && distanceFromPlayer <= 130) {			
			distanceFromPlayer -= zoomLevel;
		} else if(distanceFromPlayer >= 130){
			distanceFromPlayer = 130;
		} else if(distanceFromPlayer <= 30) {
			distanceFromPlayer = 30;
		}
	}
	
	private void calculatePitch() {
		if(Mouse.isButtonDown(1)) {
			float pitchChange = Mouse.getDY() * 0.1f;
			if(pitch >= 1.7f && pitch <= 18) {				
				pitch -= pitchChange;
			} else if(pitch < 1.7f) {
				pitch = 1.7f;
			} else if(pitch > 18) {
				pitch = 18;
			}

		}
	}
	
	private void calculateAngleAroundPlayer() {
		if(Mouse.isButtonDown(0)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
		}
	}

}
