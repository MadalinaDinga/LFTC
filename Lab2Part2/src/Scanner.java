import FA.FA;
import util.Constants;
import util.IOActions;

import java.io.IOException;
import java.util.*;

public class Scanner {

	private Map<String,Integer> keywords;
	private Map<String,Integer> ST;
	
	public Scanner() {
		keywords = new LinkedHashMap<>();
		populateKeywordsMap();
		System.out.println(keywords);
	}
	
	public void scan(String input, String output) throws Exception {
		IOActions io = new IOActions(input, output);
		ST = new LinkedHashMap<>();
		int lineIndex=0;
		try {
			// read program
			List<String> content = io.read();

			//parse line by line
			for(String line : content){
				lineIndex++;
				StringBuffer buffer = new StringBuffer();
				/*String[] parts = line.split(" ");*/
				String currentSequence="";
				String longestAcceptedSequence="";

				//parse line char by char
				int i=0;
				while(i < line.length()){
					currentSequence += line.charAt(i);
					//if the current sequence is accepted, then jump to the next character
					if (new FA("resources/FAConstant.txt").checkSequenceString(currentSequence)!=null || new FA("resources/FAIdentifier.txt").checkSequenceString(currentSequence)!=null || new FA("resources/FAOperator.txt").checkSequenceString(currentSequence)!=null ) {
						longestAcceptedSequence = currentSequence;

						// in order to print also the last element in the line
						if (i==line.length()-1){
							if(keywords.containsKey(currentSequence)){
								io.write(keywords.get(currentSequence) + Constants.LINE);
							}else {
								// if the sequence is a constant/ identifier:
								// 		add it to the ST if it does not exist yet, generating an id for it
								// 		add it to the PIF, with the corresponding id
								if (!ST.containsKey(currentSequence)) {
									ST.put(currentSequence, generateValue());
									sort(ST);
								}

								if (new FA("resources/FAConstant.txt").checkSequenceString(currentSequence) != null) {
									io.write(keywords.get("constant").toString() + " " + ST.get(currentSequence).toString());
								} else if (new FA("resources/FAIdentifier.txt").checkSequenceString(currentSequence) != null) {
									io.write(keywords.get("identifier").toString() + " " + ST.get(currentSequence).toString());
								}else{
									io.write(keywords.get(currentSequence).toString() + " " + ST.get(currentSequence).toString());
								}
							}
							//ignore white spaces
							if (Character.toString(line.charAt(i)).trim().length() == 0) {
								System.out.println("EOL: Lexical error at line " + lineIndex + ", column " + i + ", after character " + currentSequence);
								currentSequence="";
								// throw new Exception("Syntax error at line " + lineIndex + " ,column " + i + " " + longestAcceptedSequence);
							}else{
								currentSequence="";
							}
						}
					}
					//if the sequence is not accepted
					else{
						//if the sequence is a keyword add it only to the PIF
						if(keywords.containsKey(longestAcceptedSequence)){
							io.write(keywords.get(longestAcceptedSequence) + Constants.LINE);
						}else {
							// if the sequence is a constant/ identifier:
							// 		add it to the ST if it does not exist yet, generating an id for it
							// 		add it to the PIF, with the corresponding id
							if (!ST.containsKey(longestAcceptedSequence)) {
								ST.put(longestAcceptedSequence, generateValue());
								sort(ST);
							}

							if (new FA("resources/FAConstant.txt").checkSequenceString(longestAcceptedSequence) != null) {
								io.write(keywords.get("constant").toString() + " " + ST.get(longestAcceptedSequence).toString());
							} else {
								io.write(keywords.get("identifier").toString() + " " + ST.get(longestAcceptedSequence).toString());
							}
						}
						currentSequence="";
					}
				i++;
				}
				write();
				System.out.println("Line "+lineIndex+" is correct.");
				}

		io.finishWriting();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void write() {
		IOActions actions = new IOActions(null, Constants.OUTPUT_TABLE);
		for(String key : ST.keySet()){
			try {
				actions.write(key + " " + ST.get(key));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		actions.finishWriting();
	}

	private void sort(Map<String, Integer> variablesTable) {
		Map<String,Integer> unorderedSymbolsTable = new HashMap<String, Integer>();
		unorderedSymbolsTable.putAll(variablesTable);
		
		variablesTable.clear();
		
		List<String> orderedKeys = getSortedKeys(unorderedSymbolsTable);	
		for(String key : orderedKeys){
			variablesTable.put(key, unorderedSymbolsTable.get(key));
		}
	}

	private List<String> getSortedKeys(Map<String, Integer> unorderedSymbolsTable) {
		List<String> orderedKeys = new ArrayList<String>();
		orderedKeys.addAll(unorderedSymbolsTable.keySet());
		Collections.sort(orderedKeys);
		return orderedKeys;
	}


	private Integer generateValue() {
		int number;
		do{
			number = new Random().nextInt(4001);
		}
		while(ST.containsValue(number));
		return number;
	}

	private void populateKeywordsMap() {
		IOActions io = new IOActions(Constants.ATOM_CODES, null);
		
		try {
			List<String> lines = io.read();
			
			Map<String,Integer> unorderedSymbolsTable = new HashMap<String, Integer>();
			for(String line : lines){
				unorderedSymbolsTable.put(line.split(Constants.SEPARATOR)[0], Integer.parseInt(line.split(Constants.SEPARATOR)[1]));
			}
			
			List<String> orderedKeys = getSortedKeys(unorderedSymbolsTable);
			for(String key : orderedKeys){
				keywords.put(key, unorderedSymbolsTable.get(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
