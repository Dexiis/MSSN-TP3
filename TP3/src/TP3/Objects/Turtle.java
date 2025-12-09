package TP3.Objects;

import TP3.Core.SubPlot;
import TP3.Logic.LSystem;
import processing.core.PApplet;
import processing.core.PVector;

public class Turtle {

	private float len;
	private final float angle;

	public Turtle(float len, float angle) {
		this.len = len;
		this.angle = angle;
	}

	public void setPose(PVector pos, float orientation, PApplet p, SubPlot plt) {
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		p.translate(pp[0], pp[1]);
		p.rotate(-orientation);
	}

	public void scaling(float s) {
		len *= s;
	}

	public float getLen() {
		return len;
	}

	public void setLen(float len) {
		this.len = len;
	}

	public void render(LSystem Ls, PApplet p, SubPlot plt) {
		p.stroke(0);
		float[] lenPix = plt.getVectorCoord(len, len);

		for (int i = 0; i < Ls.getSequence().length(); i++) {
			char c = Ls.getSequence().charAt(i);
			if (c == 'F' || c == 'G') {
				p.line(0, 0, lenPix[0], 0);
				p.translate(lenPix[0], 0);
			} else if (c == 'f')
				p.translate(lenPix[0], 0);
			else if (c == '+')
				p.rotate(angle);
			else if (c == '-')
				p.rotate(-angle);
			else if (c == '[')
				p.pushMatrix();
			else if (c == ']')
				p.popMatrix();
		}
	}
}
