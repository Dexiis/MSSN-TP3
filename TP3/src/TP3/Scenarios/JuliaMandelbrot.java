package TP3.Scenarios;

import TP3.Core.SubPlot;
import TP3.Logic.Complex;
import processing.core.PApplet;

public class JuliaMandelbrot extends PApplet {

	private final int MAX_ITER = 256;
	private final double ESCAPE_RADIUS = 2.0;

	private final double[] WINDOW_JULIA = { -1.5, 1.5, -1.5, 1.5 };
	private final float[] VIEWPORT_JULIA = { 0f, 0f, 0.5f, 1f };
	private SubPlot pltJulia;

	private final double[] WINDOW_MANDELBROT = { -2.0, 1.0, -1.2, 1.2 };
	private final float[] VIEWPORT_MANDELBROT = { 0.5f, 0f, 0.5f, 1f };
	private SubPlot pltMandelbrot;

	private Complex C_JULIA = new Complex(-0.7885, 0.140);

	private boolean smoothColoring = true;

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
				else if (smoothColoring)
					col = getColorSmooth(iter, Z.norm());
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
				double cr = crci[0];
				double ci = crci[1];

				Complex C = new Complex(cr, ci);
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
				else if (smoothColoring)
					col = getColorSmooth(iter, Z.norm());
				else
					col = getColorBanded(iter);

				pixels[x + y * width] = col;
			}

	}

	private int getColorBanded(int iter) {
		int hue = (int) map(iter, 0, MAX_ITER, 0, 255);
		return color(hue, 255, 255);
	}

	private int getColorSmooth(int iter, double norm) {
		double logZn = Math.log(norm);
		double logLogZn = Math.log(logZn);
		double fractionalIter = iter + 1 - logLogZn / Math.log(2.0);

		int hue = (int) map((float) fractionalIter, 0, MAX_ITER, 0, 255);

		return color(hue, 255, 255);
	}

	public void mousePressed() {
		if (mouseX > width / 2) {
			double[] crci = pltMandelbrot.getWorldCoord(mouseX, mouseY);
			C_JULIA = new Complex(crci[0], crci[1]);
		}

		redraw();
	}
}