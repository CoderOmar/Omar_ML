import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class BaseUtils {

	public static HashMap<String, Integer> loadDictFromFile() {
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream("zidian.txt")));
			hash = (HashMap<String, Integer>) in.readObject();
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return hash;
	}

}
