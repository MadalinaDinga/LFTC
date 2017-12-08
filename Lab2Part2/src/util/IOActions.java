package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IOActions {

	private final String INPUT_FILE;
	private final String OUTPUT_FILE;
	private BufferedWriter writer;

	public IOActions(String inputFile, String outputFile) {
		super();
		INPUT_FILE = inputFile;
		OUTPUT_FILE = outputFile;
		if (outputFile != null) {
			initWriter();
		}
	}

	public IOActions(String inputFile) {
		super();
		INPUT_FILE = inputFile;
		OUTPUT_FILE = null;
	}

	private void initWriter() {
		if (OUTPUT_FILE != null) {
			try {
				writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//write to the PIF
	public void write(String text) throws IOException {
		List<String> content = Files.readAllLines(Paths.get(OUTPUT_FILE));
		try {
			for (String line : content) {
				writer.write(line);
			}
			writer.write(text);
			writer.newLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finishWriting() {
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> read() throws IOException {
		return Files.readAllLines(Paths.get(INPUT_FILE));
	}
}
