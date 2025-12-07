import TP3.Scenarios.*;
import processing.core.PApplet;

public class MainMenu extends PApplet {

	private String title = "Choose what to open:";
	private String option1 = "Chaos Game";
	private String option2 = "Lindenmayer Systems";
	private String option3 = "-";
	private String option4 = "-";
	
	public void settings() {
		size(360, 300);
	}

	public void setup() {
		textAlign(CENTER, CENTER);
		textSize(16);
		rectMode(CENTER);
	}

	public void draw() {
		background(200);

		fill(0);
		text(title, width / 2, 30);

		drawButton(width / 2, 80, 300, 40, option1, 1);
		drawButton(width / 2, 140, 300, 40, option2, 2);
		drawButton(width / 2, 200, 300, 40, option3, 3);
		drawButton(width / 2, 260, 300, 40, option4, 4);
	}

	void drawButton(float x, float y, float w, float h, String label, int id) {
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y - h / 2 && mouseY < y + h / 2)
			fill(150, 200, 255);
		else
			fill(180);

		rect(x, y, w, h, 5);

		fill(0);
		text(label, x, y);
	}

	public void mousePressed() {
		float x = width / 2;
		float w = 300;
		float h = 40;

		if (checkButton(80, x, w, h)) {
			PApplet.main(ChaosGame.class);
		} else if (checkButton(140, x, w, h)) {
			PApplet.main(LindenmayerSystems.class);
		} else if (checkButton(200, x, w, h)) {
			//PApplet.main(Flocking.class);
		} else if (checkButton(260, x, w, h)) {
			//PApplet.main(NormandyLanding.class);
		}
	}
	
	private boolean checkButton(float buttonY, float x, float w, float h) {
        return mouseX > x - w / 2 && mouseX < x + w / 2 && 
               mouseY > buttonY - h / 2 && mouseY < buttonY + h / 2;
    }

	public static void main(String[] args) {
		PApplet.main(MainMenu.class.getName());
	}
}