package TP3.Logic;

import TP3.Objects.Rule;

public class LSystem {
	private String sequence;
	private final Rule[] ruleset;
	private int gen;

	public LSystem(String axiom, Rule[] ruleset) {
		sequence = axiom;
		this.ruleset = ruleset;
		gen = 0;
	}

	public int getGen() {
		return gen;
	}

	public String getSequence() {
		return sequence;
	}

	public void nextGen() {
		gen++;
		String nextGen = "";

		for (int i = 0; i < sequence.length(); i++) {

			char c = sequence.charAt(i);
			String replace = "" + c;

			for (Rule r : ruleset) {
				if (c == r.getSymbol()) {
					replace = r.getString();
					break;
				}
			}
			nextGen += replace;
		}

		this.sequence = nextGen;
	}
}