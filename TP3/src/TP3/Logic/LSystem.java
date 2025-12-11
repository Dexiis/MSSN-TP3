package TP3.Logic;

import TP3.Objects.Rule;

public class LSystem {
	private String sequence;
	private final Rule[] ruleset;
	private int generation;

	public LSystem(String axiom, Rule[] ruleset) {
		sequence = axiom;
		this.ruleset = ruleset;
		generation = 0;
	}

	public int getGen() {
		return generation;
	}

	public String getSequence() {
		return sequence;
	}

	public void nextGen() {
		generation++;
		String nextGeneration = "";

		for (int i = 0; i < sequence.length(); i++) {

			char c = sequence.charAt(i);
			String replace = "" + c;

			for (Rule rule : ruleset)
				if (c == rule.getSymbol()) {
					replace = rule.getString();
					break;
				}

			nextGeneration += replace;
		}

		this.sequence = nextGeneration;
	}
}