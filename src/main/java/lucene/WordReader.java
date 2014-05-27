package lucene;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import models.Word;

public class WordReader {

	public static List<Word> GetWords() {

		List<Word> words = new ArrayList<Word>();

		try {
			// READ FILE
			FileInputStream fstream = new FileInputStream(
					"/media/Media/wikipedia/scripts/words.txt");
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;

			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {

				Word w = new Word();
				String[] parts = strLine.split(" ");
				w.setWord(parts[0]);
				w.setOcurrence(Integer.parseInt(parts[1]));
				words.add(w);
			}
		} catch (Exception e) {
			System.out.println("" + e.toString());

		}

		return words;

	}

}
