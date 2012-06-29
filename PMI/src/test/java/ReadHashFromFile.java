import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class ReadHashFromFile {

	/**
	 * @param args
	 *            Jun 29, 2012
	 */
	public static void main(String[] args) {
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream("zidian.txt")));
			hash = (HashMap<String, Integer>) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int num = hash.get("²Æ¾­");
		System.out.println(num);

	}

}
