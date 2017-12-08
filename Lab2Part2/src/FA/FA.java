package FA;

import util.IOActions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FA {
	private static final String EQUAL = "=";
	private static final String COMMA = ",";
	private List<String> alphabet;
	private Map<String, State> state;
	private State initialState = null;
	private String FAFileName;

	public FA(String FAFileName) {
		alphabet = new ArrayList<String>();
		state = new HashMap<String, State>();
		this.FAFileName = FAFileName;
		try {
			read(FAFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void read(String fileName) throws Exception {
		IOActions io = new IOActions(fileName);
		try {
			List<String> lines = io.read();
			for (String line : lines) {
				switch (line.split("=")[0]) {
					case "Q": {
						createStates(line);
						break;
					}
					case "A": {
						createAlphabet(line);
						break;
					}
					case "I": {
						setInitialState(line);
						break;
					}
					case "F": {
						setFinalStates(line);
						break;
					}
					case "T": {
						setTranzitions(line);
						break;
					}
					default: {
						throw new Exception("Invalid command!");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setTranzitions(String line) throws Exception {
		for (String transition : line.split(EQUAL)[1].split(";")) {
			if (state.containsKey(transition.split("-")[0])
					&& state.containsKey(transition.split("-")[1]
					.split(COMMA)[0])) {
				state.get(transition.split("-")[1].split(COMMA)[0])
						.addDestination(
								transition.split("-")[1].split(COMMA)[1],
								state.get(transition.split("-")[0]));
			} else {
				throw new Exception("State name is not valid at tranzitions!");
			}
		}
	}

	private void createStates(String line) {
		for (String stateName : line.split(EQUAL)[1].split(COMMA)) {
			if (!state.containsKey(stateName)) {
				state.put(stateName, new State(stateName));
			}
		}
	}

	private void createAlphabet(String line) {
		for (String letter : line.split(EQUAL)[1].split(",")) {
			if (!alphabet.contains(letter)) {
				alphabet.add(letter);
			}
		}
	}

	private void setInitialState(String line) throws Exception {
		if (state.keySet().contains(line.split(EQUAL)[1])) {
			initialState = state.get(line.split(EQUAL)[1]);
		} else {
			throw new Exception("Initial state is not valid!");
		}
	}

	private void setFinalStates(String line) throws Exception {
		for (String stateName : line.split(EQUAL)[1].split(",")) {
			boolean found = false;
			for (String s : state.keySet()) {
				if (s.equals(stateName)) {
					state.get(stateName).setFinalState(true);
					found = true;
					break;
				}
			}
			if (!found) {
				throw new Exception("State " + stateName + " is not valid!");
			}
		}
	}

	public void verify(String fileName) {
		try {
			read(FAFileName);
			printAFDetails();
			checkSequence(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called for sequences corresponding to the lexical atoms( by the Scanner).
	 * @param sequence - the checked sequence
	 * @return
	 * 	true- accepted sequence
	 * 	false- not accepted
	 */
	public String checkSequenceString(String sequence){
		try {
			checkAlphabet(sequence);
			int i = 0;

			 // the result contains the longest prefix( the part of the sequence which was accepted before a character was not ok)
			StringBuilder result = new StringBuilder();
			StringBuilder buffer = new StringBuilder();
			State s = initialState;

			//parse the sequence letter after letter while there is a road from the current character to the initial state
			while (i < sequence.length()
					&& isRoad(Character.toString(sequence.charAt(i)), s)) {
					s = s.getDestinations()
							.get(Character.toString(sequence.charAt(i))).get(0);
					buffer.append(sequence.charAt(i));
					if (s.isFinalState()) {
						result.delete(0, result.length());
						result.append(buffer);
					}
					i++;
			}

			if(result.length() == sequence.length() && s.isFinalState()){
				//accepted
				return result.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//not accepted
		return null;
	}
	/*public boolean checkSequenceString(String sequence){
		try {
			checkAlphabet(sequence);
			int i = 0;

			 // the result contains the longest prefix( the part of the sequence which was accepted before a character was not ok)
			StringBuilder result = new StringBuilder();
			StringBuilder buffer = new StringBuilder();
			State s = initialState;

			//parse the sequence letter after letter while there is a road from the current character to the initial state
			while (i < sequence.length()
					&& isRoad(Character.toString(sequence.charAt(i)), s)) {
					s = s.getDestinations()
							.get(Character.toString(sequence.charAt(i))).get(0);
					buffer.append(sequence.charAt(i));
					if (s.isFinalState()) {
						result.delete(0, result.length());
						result.append(buffer);
					}
					i++;
			}

			if(result.length() == sequence.length() && s.isFinalState()){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}*/

	private boolean isRoad(String string, State s) {
		return s.getDestinations().containsKey(string);
	}
	
	private void checkSequence(String fileName) throws Exception {
		List<String> lines = new IOActions(fileName).read();
		for (String sequence : lines) {
			checkAlphabet(sequence);

			int i = 0;
			StringBuilder result = new StringBuilder();
			StringBuilder buffer = new StringBuilder();
			State s = initialState;
			while (i < sequence.length()
					&& isRoad(Character.toString(sequence.charAt(i)), s)) {
				s = s.getDestinations()
						.get(Character.toString(sequence.charAt(i))).get(0);
				buffer.append(sequence.charAt(i));
				if (s.isFinalState()) {
					result.delete(0, result.length());
					result.append(buffer);
				}
				i++;
			}

			printResult(sequence, result, s);
		}
	}

	private void checkAlphabet(String sequence)
		throws Exception {
			for (int i = 0; i < sequence.length(); i++) {
				if (!alphabet.contains(Character.toString(sequence.charAt(i)))) {
//					System.out.println(FAFileName + " : The character " + sequence.charAt(i)
//							+ " from sequence " + sequence + " does not belong to the alphabet.");
			}
		}
	}

	private void printResult(String sequence, StringBuilder result, State s) {
		if (result.length() == sequence.length() && s.isFinalState()) {
			System.out.println("Sequence:  " + sequence
					+ " accepted by the FA.");
		} else {
			if (result.length() != 0) {
				System.out
						.println("Sequence: "
								+ sequence
								+ " not accepted by the FA, the longest accepted prefix: "
								+ result.toString());
			} else {
				System.out
						.println("Sequence: "
								+ sequence
								+ " not accepted by the FA, there is no accepted prefix.");
			}
		}
	}


	private void printAFDetails() {
		System.out.print("Alphabet: ");
		for (String s : alphabet) {
			System.out.print(s + " ");
		}
		System.out.println();

		System.out.print("States: ");
		for (String s : state.keySet()) {
			System.out.print(s + " ");
		}
		System.out.println();

		System.out.println("Transitions:");
		for (String key : state.keySet()) {
			for (String letter : state.get(key).getDestinations().keySet()) {
				for (State s : state.get(key).getDestinations().get(letter)) {
					System.out.println(key + " -> " + letter + " ->"
							+ s.getDescription());
				}
			}
		}

		System.out.println("Initial state: " + initialState.getDescription());

		System.out.print("Final states: ");
		for (String key : state.keySet()) {
			if (state.get(key).isFinalState()) {
				System.out.print(key + " ");
			}
		}
		System.out.println();

	}


}
