package TP3.Scenarios;

import TP3.Objects.*;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import controlP5.*;

public class ChaosGame extends PApplet {
	private ArrayList<Point> corePoints = new ArrayList<>();
	private ArrayList<Point> innerPoints = new ArrayList<>();

	private int editingPointIndex = -1;
	private final float CLICK_TOLERANCE = 15;

	private ControlP5 cp5;
	private boolean isPaused = false;
	private float proportion = 0.5f;
	private int speed = 1;

	private final int DRAWING_AREA_WIDTH = 800;

	public void settings() {
		size(1000, 800);
	}

	public void setup() {
		background(255);

		cp5 = new ControlP5(this);

		int xPos = DRAWING_AREA_WIDTH + 20;
		int yStart = 30;
		int ySpacing = 60;
		int buttonHeight = 30;

		cp5.addButton("togglePause").setLabel("Pausar / Continuar").setValue(0).setPosition(xPos, yStart).setSize(160,
				buttonHeight);

		yStart += ySpacing;

		cp5.addSlider("proportion").setLabel("Proporção (0.05 a 1.0)").setRange(0.05f, 1.0f).setValue(0.5f)
				.setPosition(xPos, yStart).setSize(160, 20).setNumberOfTickMarks(20).setDecimalPrecision(2)
				.showTickMarks(false);

		yStart += ySpacing;

		cp5.addSlider("speed").setLabel("Velocidade (1 a 100 p/s)").setRange(1, 100).setValue(1)
				.setPosition(xPos, yStart).setSize(160, 20).setNumberOfTickMarks(100).showTickMarks(false);

		yStart += ySpacing;

		cp5.addButton("clearInnerPoints").setLabel("Limpar Pontos Fractais").setValue(0).setPosition(xPos, yStart)
				.setSize(160, buttonHeight);

		frameRate(60);
	}

	public void draw() {
		background(255);

		stroke(150);
		strokeWeight(1);
		line(DRAWING_AREA_WIDTH, 0, DRAWING_AREA_WIDTH, height);

		drawCoreElements();

		if (!isPaused && corePoints.size() >= 3)
			for (int k = 0; k < speed; k++)
				generateNewChaosPoint();

		drawInnerPoints();

		drawInterfaceLabels();
	}

	public void drawCoreElements() {
		if (corePoints.size() >= 3) {
			stroke(0, 0, 255);
			strokeWeight(2);

			for (int i = 0; i < corePoints.size(); i++) {
				PVector previousPosition = corePoints.get(i).getPosition();

				int nextIndex;
				if (i == corePoints.size() - 1)
					nextIndex = 0;
				else
					nextIndex = i + 1;

				PVector nextPosition = corePoints.get(nextIndex).getPosition();

				line(previousPosition.x, previousPosition.y, nextPosition.x, nextPosition.y);
			}
		}

		for (int i = 0; i < corePoints.size(); i++) {
			Point p = corePoints.get(i);

			if (i == editingPointIndex) {
				stroke(0, 255, 0);
				strokeWeight(12);
				point(p.getPosition().x, p.getPosition().y);
			} else
				p.display();
		}
	}

	public void generateNewChaosPoint() {
		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA) {
				indexB = floor(random(corePoints.size()));
			}

			Point pointA = corePoints.get(indexA);
			Point pointB = corePoints.get(indexB);

			PVector initialPosition = PVector.lerp(pointA.getPosition(), pointB.getPosition(), proportion);

			int initialColour = pointB.getColour();

			Point newInnerPoint = new Point(initialPosition, initialColour, this);
			innerPoints.add(newInnerPoint);
			return;
		}

		Point lastInnerPoint = innerPoints.get(innerPoints.size() - 1);
		PVector lastPosition = lastInnerPoint.getPosition();

		int targetIndex = floor(random(corePoints.size()));
		Point targetVertex = corePoints.get(targetIndex);
		PVector targetPosition = targetVertex.getPosition();

		PVector newPosition = PVector.lerp(lastPosition, targetPosition, proportion);

		int newColour = targetVertex.getColour();

		Point newPoint = new Point(newPosition, newColour, this);
		innerPoints.add(newPoint);
	}

	public void drawInnerPoints() {
		for (Point p : innerPoints) {
			p.display();
		}
	}

	public void drawInterfaceLabels() {
		fill(0);
		textSize(18);
		textAlign(LEFT);
		text("Controles da Simulação", DRAWING_AREA_WIDTH + 20, 15);

		textSize(14);
		text("Botões de Interação (Cliques):", DRAWING_AREA_WIDTH + 20, 260);

		textSize(12);
		text("Mouse Esquerdo:", DRAWING_AREA_WIDTH + 20, 280);
		text("Adicionar Novo Vértice", DRAWING_AREA_WIDTH + 20, 300);
		text("Mouse Direito:", DRAWING_AREA_WIDTH + 20, 330);
		text("Mover Último Vértice", DRAWING_AREA_WIDTH + 20, 350);
		text("Mouse Meio:", DRAWING_AREA_WIDTH + 20, 380);
		text("Remover Vértice Clicado", DRAWING_AREA_WIDTH + 20, 400);

		text("Status da Simulação:", DRAWING_AREA_WIDTH + 20, 440);
		text("Pausado: " + (isPaused ? "SIM" : "NÃO"), DRAWING_AREA_WIDTH + 20, 460);
		text("Proporção: " + proportion, DRAWING_AREA_WIDTH + 20, 480);
		text("Velocidade: " + speed + " p/frame", DRAWING_AREA_WIDTH + 20, 500);
	}

	public void togglePause(int value) {
		isPaused = !isPaused;
	}

	public void clearInnerPoints(int value) {
		innerPoints.clear();
	}

	public void mousePressed() {
		if (mouseX < DRAWING_AREA_WIDTH) {
			if (mouseButton == LEFT) {
				PVector newPosition = new PVector(mouseX, mouseY);
				Point newCorePoint = new Point(newPosition, this);
				corePoints.add(newCorePoint);
			} else if (mouseButton == RIGHT) {
				if (corePoints.size() > 0)
					editingPointIndex = corePoints.size() - 1;

			} else if (mouseButton == CENTER) {
				for (int i = corePoints.size() - 1; i >= 0; i--) {
					Point p = corePoints.get(i);

					float distance = dist(mouseX, mouseY, p.getPosition().x, p.getPosition().y);

					if (distance < CLICK_TOLERANCE) {
						corePoints.remove(i);

						if (i == editingPointIndex)
							editingPointIndex = -1;

						break;
					}
				}
			}
		}
	}

	public void mouseDragged() {
		if (mouseX < DRAWING_AREA_WIDTH) {
			if (editingPointIndex != -1) {
				Point pointToEdit = corePoints.get(editingPointIndex);
				PVector newPosition = new PVector(mouseX, mouseY);

				pointToEdit.setPosition(newPosition);
			}
		}
	}

	public void mouseReleased() {
		if (editingPointIndex != -1)
			editingPointIndex = -1;
	}
}