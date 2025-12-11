package TP3.Scenarios;

import TP3.Objects.*;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import controlP5.*;

public class ChaosGame extends PApplet {
	private ArrayList<Point> corePoints = new ArrayList<>();
	private ArrayList<Point> innerPoints = new ArrayList<>();

	private ControlP5 cp5;

	private PVector lastPositionLine, targetPositionLine;

	private int editingPointIndex = -1;
	private boolean isPaused = false;
	private float proportion = 0.5f;
	private int speed = 1;
	private float lastUpdateTime;
	private float pointAccumulator = 0;
	private int restrictionMode = 0;
	private int previousIndex1, previousIndex2, previousIndex3 = -1;

	private final int DRAWING_AREA_WIDTH = 800;
	private final float CLICK_TOLERANCE = 15;

	public void settings() {
		size(1000, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
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

		cp5.addSlider("speed").setLabel("Velocidade (1 a 500 p/s)").setRange(1, 500).setValue(1)
				.setPosition(xPos, yStart).setSize(160, 20).setNumberOfTickMarks(500).showTickMarks(false);

		yStart += ySpacing;

		cp5.addButton("clearInnerPoints").setLabel("Limpar Pontos Fractais").setValue(0).setPosition(xPos, yStart)
				.setSize(160, buttonHeight);

		frameRate(60);
	}

	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(255);
		stroke(150);
		strokeWeight(1);
		line(DRAWING_AREA_WIDTH, 0, DRAWING_AREA_WIDTH, height);

		drawCoreElements();

		if (!isPaused && corePoints.size() >= 3) {
			pointAccumulator += speed * dt;
			int pointsToGenerate = floor(pointAccumulator);
			for (int k = 0; k < pointsToGenerate; k++)
				generateNewChaosPoint();

			pointAccumulator -= pointsToGenerate;
		}

		if (lastPositionLine != null && targetPositionLine != null) {
			strokeWeight(1);
			stroke(0, 0, 0, 50);
			line(lastPositionLine.x, lastPositionLine.y, targetPositionLine.x, targetPositionLine.y);
		}

		for (Point p : innerPoints)
			p.display();

		drawInterfaceLabels();
	}

	public void generateNewChaosPoint() {
		switch (restrictionMode) {
		case 0:
			noRestrictions();
			break;
		case 1:
			restriction1();
			break;
		case 2:
			restriction2();
			break;
		case 3:
			restriction3();
			break;
		case 4:
			restriction4();
			break;
		case 5:
			restriction5();
			break;
		default:
			break;
		}
	}

	public void noRestrictions() {
		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA)
				indexB = floor(random(corePoints.size()));

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

		lastPositionLine = lastPosition;
		targetPositionLine = targetPosition;
	}

	public void restriction1() { // The current vertex cannot be the previous vertex
		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA)
				indexB = floor(random(corePoints.size()));

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

		while (previousIndex1 == targetIndex)
			targetIndex = floor(random(corePoints.size()));
		previousIndex1 = targetIndex;

		Point targetVertex = corePoints.get(targetIndex);
		PVector targetPosition = targetVertex.getPosition();
		
		PVector newPosition = PVector.lerp(lastPosition, targetPosition, proportion);
		int newColour = targetVertex.getColour();
		Point newPoint = new Point(newPosition, newColour, this);
		innerPoints.add(newPoint);

		lastPositionLine = lastPosition;
		targetPositionLine = targetPosition;
	}

	public void restriction2() { // The current vertex cannot be any of the 3 previous vertexes
		if (corePoints.size() < 4)
			return;

		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA)
				indexB = floor(random(corePoints.size()));

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

		while (previousIndex1 == targetIndex || previousIndex2 == targetIndex || previousIndex3 == targetIndex)
			targetIndex = floor(random(corePoints.size()));
		previousIndex3 = previousIndex2;
		previousIndex2 = previousIndex1;
		previousIndex1 = targetIndex;

		Point targetVertex = corePoints.get(targetIndex);
		PVector targetPosition = targetVertex.getPosition();

		PVector newPosition = PVector.lerp(lastPosition, targetPosition, proportion);
		int newColour = targetVertex.getColour();
		Point newPoint = new Point(newPosition, newColour, this);
		innerPoints.add(newPoint);

		lastPositionLine = lastPosition;
		targetPositionLine = targetPosition;
	}

	public void restriction3() { // The current vertex cannot be one place away clockwise from the previous
									// vertex
		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA)
				indexB = floor(random(corePoints.size()));

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
		int indexCheck = previousIndex1 + 1;
		if (indexCheck > corePoints.size())
			indexCheck = 0;

		while (indexCheck == targetIndex)
			targetIndex = floor(random(corePoints.size()));
		previousIndex1 = targetIndex;

		Point targetVertex = corePoints.get(targetIndex);
		PVector targetPosition = targetVertex.getPosition();

		PVector newPosition = PVector.lerp(lastPosition, targetPosition, proportion);
		int newColour = targetVertex.getColour();
		Point newPoint = new Point(newPosition, newColour, this);
		innerPoints.add(newPoint);
		
		lastPositionLine = lastPosition;
		targetPositionLine = targetPosition;
	}

	public void restriction4() { // The current vertex cannot be one place away from the previous vertex
		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA)
				indexB = floor(random(corePoints.size()));

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
		int indexCheck1 = previousIndex1 + 1;
		int indexCheck2 = previousIndex1 - 1;
		if (indexCheck1 > corePoints.size())
			indexCheck1 = 0;
		if (indexCheck2 < 0)
			indexCheck2 = corePoints.size();

		while (indexCheck1 == targetIndex || indexCheck2 == targetIndex)
			targetIndex = floor(random(corePoints.size()));

		previousIndex1 = targetIndex;

		Point targetVertex = corePoints.get(targetIndex);
		PVector targetPosition = targetVertex.getPosition();

		PVector newPosition = PVector.lerp(lastPosition, targetPosition, proportion);
		int newColour = targetVertex.getColour();
		Point newPoint = new Point(newPosition, newColour, this);
		innerPoints.add(newPoint);

		lastPositionLine = lastPosition;
		targetPositionLine = targetPosition;
	}

	public void restriction5() { // The current vertex cannot be 2 places away counter clockwise from the
									// previously chosen vertex
		if (innerPoints.isEmpty()) {
			int indexA = floor(random(corePoints.size()));
			int indexB = floor(random(corePoints.size()));

			while (corePoints.size() > 1 && indexB == indexA)
				indexB = floor(random(corePoints.size()));

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
		int indexCheck1 = previousIndex1 - 1;
		int indexCheck2 = previousIndex1 - 2;
		if (indexCheck1 < 0)
			indexCheck1 = corePoints.size();
		if (indexCheck2 == -1)
			indexCheck2 = corePoints.size();
		else if (indexCheck2 == -2)
			indexCheck2 = corePoints.size() - 1;

		while (indexCheck1 == targetIndex || indexCheck2 == targetIndex)
			targetIndex = floor(random(corePoints.size()));
		previousIndex1 = targetIndex;

		Point targetVertex = corePoints.get(targetIndex);
		PVector targetPosition = targetVertex.getPosition();

		PVector newPosition = PVector.lerp(lastPosition, targetPosition, proportion);
		int newColour = targetVertex.getColour();
		Point newPoint = new Point(newPosition, newColour, this);
		innerPoints.add(newPoint);

		lastPositionLine = lastPosition;
		targetPositionLine = targetPosition;
	}

	public void drawCoreElements() {
		if (corePoints.size() >= 3) {
			stroke(0, 0, 0);
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

	public void addNewPoint(Point newPoint) {
		corePoints.add(newPoint);
		reorderCorePoints();
	}

	public void reorderCorePoints() {
		if (corePoints.size() < 3)
			return;

		PVector centroid = new PVector(0, 0);
		for (Point p : corePoints)
			centroid.add(p.getPosition());

		centroid.div(corePoints.size());

		corePoints.sort((p1, p2) -> {
			PVector pos1 = p1.getPosition();
			PVector pos2 = p2.getPosition();

			float angle1 = atan2(pos1.y - centroid.y, pos1.x - centroid.x);
			float angle2 = atan2(pos2.y - centroid.y, pos2.x - centroid.x);

			if (angle1 < angle2)
				return -1;
			if (angle1 > angle2)
				return 1;
			return 0;
		});
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

	public void clearInnerPoints() {
		innerPoints.clear();
	}

	public void mousePressed() {
		if (mouseX < DRAWING_AREA_WIDTH) {
			if (mouseButton == LEFT) {
				PVector newPosition = new PVector(mouseX, mouseY);
				Point newCorePoint = new Point(newPosition, this);
				addNewPoint(newCorePoint);
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

	public void keyPressed() {
		if (key == 'M' || key == 'm') {
			restrictionMode++;
			if (restrictionMode > 5)
				restrictionMode = 0;
			clearInnerPoints();
		}
	}
}