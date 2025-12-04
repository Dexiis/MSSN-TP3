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
		
		// Defined triangle's side
		int triangleSide = 500;
		// Pythagorean Theorem to calculate triangle's height based on side 
		float triangleHeight = sqrt(pow(triangleSide, 2) - pow(triangleSide / 2, 2));
		
		// Base triangle's vertices
		Vertex[0] = new PVector(width / 2, 50);
		Vertex[1] = new PVector(Vertex[0].x - triangleSide / 2, Vertex[0].y + triangleHeight);
		Vertex[2] = new PVector(Vertex[1].x + triangleSide, Vertex[1].y);
		
		float triangleArea = triangleSide * triangleHeight / 2;
		
		while(true) {
			// Random point
			point = new PVector(random(width), random(height));
			
			float bottomTriangle = triangleArea(point, Vertex[1], Vertex[2]);
			float leftTriangle = triangleArea(point, Vertex[0], Vertex[1]);
			float rightTriangle = triangleArea(point, Vertex[0], Vertex[2]);
		
			// Calculates the 3 formed triangles with 2 vertices and the point and compares with the triangle's area
			if(round(triangleArea) == round(bottomTriangle + leftTriangle + rightTriangle))
				break;
		}
		
		strokeWeight(3);
		noFill();
		triangle(Vertex[0].x, Vertex[0].y, Vertex[1].x, Vertex[1].y, Vertex[2].x, Vertex[2].y);
	}
	
	public void draw() {
		point(point.x, point.y);
		
		// Choose a random vertex
		int chosenVertex = (int) random(Vertex.length);
		
		// Midpoint between chosen vertex and latest point
		point.x = (Vertex[chosenVertex].x + point.x) / 2;
		point.y = (Vertex[chosenVertex].y + point.y) / 2;
		
	}
	
	// Calculates a triangle's area based on the vertices' coordinates
	private float triangleArea(PVector A, PVector B, PVector C) {
		// Area = 0.5 * |x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2)|
		return 0.5f * abs(A.x * (B.y - C.y) + B.x * (C.y- A.y) + C.x * (A.y - B.y));
	}
	
}
