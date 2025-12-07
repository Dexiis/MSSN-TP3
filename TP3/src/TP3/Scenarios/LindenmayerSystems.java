package TP3.Scenarios;

import CodigoDoStor.LSystem;
import CodigoDoStor.Rule;
import CodigoDoStor.SubPlot;
import CodigoDoStor.Turtle;
import processing.core.PApplet;
import processing.core.PVector;

public class LindenmayerSystems extends PApplet {
	
	private LSystem Ls;
    private final double[] window = {-15, 15, 0, 15};
    private final float[] viewport = {0f, 0f, 1f, 1f};
    private final PVector startPos = new PVector();
    private SubPlot plt;
    private Turtle turtle;

    public void settings() {
    	size(800, 800);
    }
    
    public void setup() {
        plt = new SubPlot(window, viewport, width, height);
        Rule[] ruleset = new Rule[1];
        //ruleset[0] = new Rule('X', "F+[[X]-X]-F[-FX]+X");
        //ruleset[1] = new Rule('F', "FF");                           // AXIOMA: X,
        ruleset[0] = new Rule('F', "FF+[+F-F-F]-[-F+F+F]");         // AXIOMA: F
        //ruleset[0] = new Rule('F', "G[+F]-F");
        //ruleset[1] = new Rule('G', "GG");                           // AXIOMA: F

        Ls = new LSystem("F", ruleset);
        turtle = new Turtle(5, PApplet.radians(22.5f));
    }

    @Override
    public void draw() {
        float[] bb = plt.getBoundingBox();
        rect(bb[0], bb[1], bb[2], bb[3]);

        turtle.setPose(startPos, PApplet.radians(90), this, plt);
        turtle.render(Ls, this, plt);
    }

    @Override
    public void keyPressed() {}

    @Override
    public void mousePressed() {
        //System.out.println(Ls.getSequence());
        Ls.nextGen();
        turtle.scaling(0.5f);
    }
	
}
