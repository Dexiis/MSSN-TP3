package TP3.Scenarios;

import TP3.Core.SubPlot;
import TP3.Logic.Complex;
import processing.core.PApplet;

public class JuliaMandelbrot extends PApplet {

	private final int MAX_ITER = 300;
	private final double ESCAPE_RADIUS = 2.0;

	private final double[] WINDOW_JULIA = { -1.5, 1.5, -1.5, 1.5 };
	private final float[] VIEWPORT_JULIA = { 0f, 0f, 0.5f, 1f };
	private SubPlot pltJulia;

	private final double[] WINDOW_MANDELBROT = { -2.0, 1.0, -1.2, 1.2 };
	private final float[] VIEWPORT_MANDELBROT = { 0.5f, 0f, 0.5f, 1f };
	private SubPlot pltMandelbrot;

	private Complex C_JULIA = new Complex(-0.7885, 0.140);

	private float mx0, my0, mx1, my1;

	public void settings() {
		size(1500, 800);
	}

	public void setup() {
		noLoop();

		pltJulia = new SubPlot(WINDOW_JULIA, VIEWPORT_JULIA, width, height);
		pltMandelbrot = new SubPlot(WINDOW_MANDELBROT, VIEWPORT_MANDELBROT, width, height);
	}

	public void draw() {
		background(255);
		loadPixels();

		drawJuliaSet();
		drawMandelbrotSet();
		
		updatePixels();
		
		displayNewWindow();
	}

	private void drawJuliaSet() {
		int xStart = (int) (VIEWPORT_JULIA[0] * width);
		int xEnd = (int) (VIEWPORT_JULIA[2] * width + xStart);
		int yStart = (int) (VIEWPORT_JULIA[1] * height);
		int yEnd = (int) (VIEWPORT_JULIA[3] * height + yStart);

		for (int x = xStart; x < xEnd; x++)
			for (int y = yStart; y < yEnd; y++) {

				double[] zrzi = pltJulia.getWorldCoord(x, y);
				double zr = zrzi[0];
				double zi = zrzi[1];

				Complex Z = new Complex(zr, zi);
				Complex C = C_JULIA;

				int iter = 0;

				while (iter < MAX_ITER && Z.norm() <= ESCAPE_RADIUS) {
					Complex Z_copy = new Complex(Z.getA(), Z.getB());
					Z.mult(Z_copy);
					Z.add(C);
					iter++;
				}

				int col;
				if (iter == MAX_ITER)
					col = color(0);
				else
					col = getColorBanded(iter);

				pixels[x + y * width] = col;
			}
	}

	private void drawMandelbrotSet() {
		int xStart = (int) (VIEWPORT_MANDELBROT[0] * width);
		int xEnd = (int) (VIEWPORT_MANDELBROT[2] * width + xStart);
		int yStart = (int) (VIEWPORT_MANDELBROT[1] * height);
		int yEnd = (int) (VIEWPORT_MANDELBROT[3] * height + yStart);

		for (int x = xStart; x < xEnd; x++)
			for (int y = yStart; y < yEnd; y++) {

				double[] crci = pltMandelbrot.getWorldCoord(x, y);

				Complex C = new Complex(crci);
				Complex Z = new Complex(0, 0);

				int iter = 0;

				while (iter < MAX_ITER && Z.norm() <= ESCAPE_RADIUS) {
					Complex Z_copy = new Complex(Z.getA(), Z.getB());
					Z.mult(Z_copy);
					Z.add(C);
					iter++;
				}
				int col;
				if (iter == MAX_ITER)
					col = color(0);
				else
					col = getColorBanded(iter);

				pixels[x + y * width] = col;
			}
	}

	private int getColorBanded(int iter) {
		float t = (float) iter / MAX_ITER;
		t = PApplet.pow(t, 0.4f);

		float hue = 150;
		float sat = 100;
		float bri = PApplet.map(t, 0, 1, 30, 90);

		int colour = color(hue, sat, bri);
		colour = color((iter % 16) * 16);

		return colour;
	}

	private void displayNewWindow() {
		pushStyle();
		noFill();
		strokeWeight(3);
		stroke(255);
		rect(mx0, my0, mx1 - mx0, my1 - my0);
		popStyle();
	}

	public void mousePressed() {
		if (mouseX > width / 2)
			if (mouseButton == LEFT) {
				double[] crci = pltMandelbrot.getWorldCoord(mouseX, mouseY);
				C_JULIA = new Complex(crci[0], crci[1]);
				
				redraw();
			} else if (mouseButton == RIGHT) {
				mx0 = mouseX;
				my0 = mouseY;
				
				redraw();
			}
	}

	public void mouseReleased() {
		if (mouseX > width / 2)
			if (mouseButton == RIGHT) {

				double[] xy0 = pltMandelbrot.getWorldCoord(mx0, my0);
				double[] xy1 = pltMandelbrot.getWorldCoord(mx1, my1);

				double xmin = Math.min(xy0[0], xy1[0]);
				double xmax = Math.max(xy0[0], xy1[0]);
				double ymin = Math.min(xy0[1], xy1[1]);
				double ymax = Math.max(xy0[1], xy1[1]);
				double[] window = { xmin, xmax, ymin, ymax };

				pltMandelbrot = new SubPlot(window, VIEWPORT_MANDELBROT, width, height);
				
				mx0 = my0 = mx1 = my1 = 0;

				redraw();
			}
	}

	public void mouseDragged() {
		if (mouseX > width / 2)
			if (mouseButton == RIGHT) {
				mx1 = mouseX;
				my1 = mouseY;

				redraw();
			}
	}

}