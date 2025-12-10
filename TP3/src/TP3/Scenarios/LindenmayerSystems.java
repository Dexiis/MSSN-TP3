package TP3.Scenarios;

import TP3.Core.SubPlot;
import TP3.Logic.LSystem;
import TP3.Objects.Rule;
import TP3.Objects.Turtle;
import processing.core.PApplet;
import processing.core.PVector;

public class LindenmayerSystems extends PApplet {

	private LSystem Lsystem;
	private final double[] window = { -10, 10, 0, 10 };
	private final float[] viewport = { 0f, 0f, 1f, 1f };
	private final PVector startPos = new PVector(0, 0);
	private SubPlot plt;
	private Turtle turtle;

	private int choice = 1; 

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		plt = new SubPlot(window, viewport, width, height);

		Rule[] ruleset;
		String axiom;
		float angle;

		if (choice == 1) {
			ruleset = new Rule[2];
			ruleset[0] = new Rule('X', "F+[[X]-X]-F[-FX]+X");
			ruleset[1] = new Rule('F', "FF");
			axiom = "X";
			angle = 22.5f;

		} else if (choice == 2) {
			ruleset = new Rule[2];
			ruleset[0] = new Rule('F', "G[+F]-F");
			ruleset[1] = new Rule('G', "GG");
			axiom = "F";
			angle = 25.7f;

		} else {
			ruleset = new Rule[1];
			ruleset[0] = new Rule('F', "F[+F][-F]F");
			axiom = "F";
			angle = 22.5f;
		}

		Lsystem = new LSystem(axiom, ruleset);
		turtle = new Turtle(5, PApplet.radians(angle));
	}

	public void draw() {
		background(255);
		fill(0);
		textSize(14);
		textAlign(LEFT);
		text("Mudar de Modo: Botão direito", 10, 20);
		float[] bb = plt.getBoundingBox();
		noFill();
		stroke(0);
		rect(bb[0], bb[1], bb[2], bb[3]);

		turtle.setPose(startPos, PApplet.radians(90), this, plt);
		turtle.render(Lsystem, this, plt);
		
	}

	public void mousePressed() {
		if (mouseButton == LEFT) {
			Lsystem.nextGen();
			turtle.scaling(0.5f);
		} else if (mouseButton == RIGHT) {
			choice++;
			if (choice > 3)
				choice = 1;

			setup();
		}
	}
}