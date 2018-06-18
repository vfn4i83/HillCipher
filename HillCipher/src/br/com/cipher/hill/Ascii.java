package br.com.cipher.hill;

import java.util.ArrayList;
import java.util.List;

public class Ascii {
	public List<Character> getASCIITable() {
		List<Character> asciiTable = new ArrayList<Character>();
		for (int i = 0; i < 256; i++) {
			Character c = new Character((char) i);
			asciiTable.add(c);
		}
		return asciiTable;
	}
}