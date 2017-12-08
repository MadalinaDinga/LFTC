import util.Constants;

public class Launcher {

	public static void main(String[] argv){
		Scanner scanner = new Scanner();
		try {
			scanner.scan(Constants.INPUT_PB1,Constants.OUTPUT_PB1);
			//scan.scan(Constants.INPUT_PB2,Constants.OUTPUT_PB2);
			//scan.scan(Constants.INPUT_PB3,Constants.OUTPUT_PB3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
