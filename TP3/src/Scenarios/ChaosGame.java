package Scenarios;

import processing.core.PApplet;
import processing.core.PVector;

public class ChaosGame extends PApplet {
	
	private PVector[] Vertex = new PVector[3];
	private PVector point;
	
	public void settings() {
		size(600, 600);
	}
	
	public void setup() {
		background(255);
		
		Vertex[0] = new PVector(width / 2, 50);
		Vertex[1] = new PVector(50, height - 50);
		Vertex[2] = new PVector(width - 50, height - 50);
		
		// TODO: Generate inside the triangle
		point = new PVector(300, 300);
		
		strokeWeight(3);
		noFill();
		triangle(Vertex[0].x, Vertex[0].y, Vertex[1].x, Vertex[1].y, Vertex[2].x, Vertex[2].y);
	}
	
	public void draw() {
		point(point.x, point.y);
		
		int chosenVertex = (int) random(Vertex.length);
		
		point.x = (Vertex[chosenVertex].x + point.x) / 2;
		point.y = (Vertex[chosenVertex].y + point.y) / 2;
		
	}
	
}
