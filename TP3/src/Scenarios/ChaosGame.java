package Scenarios;

import processing.core.PApplet;
import processing.core.PVector;

public class ChaosGame extends PApplet {
	
	private PVector V1, V2, V3;
	
	public void settings() {
		size(600, 600);
	}
	
	public void setup() {
		background(255);
		
		V1 = new PVector(width / 2, 50);
		V2 = new PVector(50, height - 50);
		V3 = new PVector(width - 50, height - 50);
	}
	
	public void draw() {
		triangle(V1.x, V1.y, V2.x, V2.y, V3.x, V3.y);
	}
	
}
