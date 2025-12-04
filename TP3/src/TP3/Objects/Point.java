package TP3.Objects;

import processing.core.PApplet;
import processing.core.PVector;

public class Point {
	private PVector position;
	private int colour;
	private PApplet p;
	private boolean corePoint;

	public Point(PVector position, PApplet p) {
		this.position = position;
		this.colour = p.color(p.random(255), p.random(255), p.random(255));
		this.p = p;
		this.corePoint = true;
	}

	public Point(PVector position, int colour, PApplet p) {
		this.position = position;
		this.colour = colour;
		this.p = p;
		this.corePoint = false;
	}
	
	public PVector getPosition() {
		return position;
	}

	public void setPosition(PVector position) {
		this.position = position;
	}

	public int getColour() {
		return colour;
	}

	public void setColour(int colour) {
		this.colour = colour;
	}

	public void display() {
		p.stroke(this.colour);

		if (corePoint)
			p.strokeWeight(10);
		else
			p.strokeWeight(2);

		p.point(position.x, position.y);
	}

}
