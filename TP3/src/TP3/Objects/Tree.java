package TP3.Objects;
import TP3.Core.SubPlot;
import TP3.Logic.LSystem;
import processing.core.PApplet;
import processing.core.PVector;

public class Tree {

    private LSystem Ls;
    private Turtle turtle;
    private PVector pos;

    private final int nSeasons;
    private float len, growthRate, now, tNextSeason;
    private final float scalingFactor, breakBetweenSeasons;

    public Tree(String axiom, Rule[] ruleset, PVector pos,
                float referenceLen, float angle, int nIterations,
                float scaleFactor, float breakSeasons, PApplet p) {

        Ls = new LSystem(axiom, ruleset);

        len = 0;
        growthRate = referenceLen/breakSeasons;
        turtle = new Turtle(0, angle);

        this.pos = pos;
        nSeasons = nIterations;
        this.scalingFactor = scaleFactor;
        this.breakBetweenSeasons = breakSeasons;
        now = p.millis()/1000f;
        tNextSeason = now + breakBetweenSeasons;

    }

    public void grow(float dt) {
        now += dt;
        if(now < tNextSeason) {
            len += growthRate*dt;
            turtle.setLen(len);
        } else if(Ls.getGen() < nSeasons) {
            Ls.nextGen();
            len *= scalingFactor;
            growthRate *= scalingFactor;
            turtle.setLen(len);
            tNextSeason = now + breakBetweenSeasons;
        }
    }

    public void display(PApplet p, SubPlot plt) {
        p.pushMatrix();
        turtle.setPose(pos, (float) Math.PI/2, p, plt);
        turtle.render(Ls, p, plt);
        p.popMatrix();
    }
}
