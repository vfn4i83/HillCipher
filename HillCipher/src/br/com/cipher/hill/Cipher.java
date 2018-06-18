package br.com.cipher.hill;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Cipher {

	private Ascii ascii = new Ascii();

	private List<Character> asciiTable = ascii.getASCIITable();

	int n1 = 0;
	int n2 = 0;
	int n3 = 0;
	int n4 = 0;
	int in1 = 0;
	int in2 = 0;
	int in3 = 0;
	int in4 = 0;
	int determinante;

	// double resto;
	String texto;

	private Scanner input;

	public void initParameters() {

		boolean ok;
		int n1 = 0;
		int n2 = 0;
		int n3 = 0;
		int n4 = 0;
		int in1 = 0;
		int in2 = 0;
		int in3 = 0;
		int in4 = 0;
		int determinante;
		double resto;

		input = new Scanner(System.in);

		System.out.println("+Por favor, entre com a Chave!");
		
		do {
			ok = false;
			System.out.print("|-Digite 1º Nº: ");
			n1 = input.nextInt();
			System.out.print("|-Digite 2º Nº: ");
			n2 = input.nextInt();
			System.out.print("|-Digite 3º Nº: ");
			n3 = input.nextInt();
			System.out.print("|-Digite 4º Nº: ");
			n4 = input.nextInt();
			System.out
					.println("****************************************************************" + '\n');

			n1 = 2;
			n2 = 1;
			n3 = -1;
			n4 = 4;

			// matriz inversa.
			in1 = n4;
			in4 = n1;
			in2 = n2 * (-1);
			in3 = n3 * (-1);

			determinante = ((n1 * n4) - (n3 * n2));
			if (determinante != 0) {
				resto = determinante % 2;

				if (resto == 0.0) {
					System.out
							.println("Matriz inválida, Determinante é nr par ");
					ok = true;
				}
			}

			if (!ok) {
				if (determinante == 0) {
					System.out.println("Matriz inválida, Determinante é zero ");
					ok = true;
				}
			}

			if (!ok) {
				if (!isValidMatrix(n1, n2, n3, n4)) {
					System.out.println("Matriz inválida nr. primos. ");
					ok = true;
				}
			}

		} while (ok);

		this.n1 = n1;
		this.n2 = n2;
		this.n3 = n3;
		this.n4 = n4;
		this.in1 = in1;
		this.in2 = in2;
		this.in3 = in3;
		this.in4 = in4;
		this.determinante = determinante;

	}

	public byte[] encrypt(byte[] msgData) throws IOException {

		// rotina para cifrar.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		String texto = new String(msgData);

		char[] arrayChar = texto.toCharArray();

		List<Integer> encrip = new ArrayList<Integer>();
		List<Integer> asci = new ArrayList<Integer>();
		int result1;
		int result2;
		char letter1;
		char letter2;
		int charSc1;
		int charSc2;

		for (int i = 0; i < arrayChar.length; i += 2) {
			letter1 = arrayChar[i];
			charSc1 = asciiTable.indexOf(letter1);

			if (i < arrayChar.length - 1) {
				letter2 = arrayChar[i + 1];
				charSc2 = asciiTable.indexOf(letter2);

			} else {
				letter2 = ' ';
				charSc2 = 32;
			}

			asci.add(charSc1);
			asci.add(charSc2);

			result1 = (n1 * charSc1) + (n2 * charSc2);
			result2 = (n3 * charSc1) + (n4 * charSc2);

			if (result1 < 0) {
				do {
					result1 = result1 + 256;
				} while (result1 <= 0);
			}

			if (result2 < 0) {
				do {
					result2 = result2 + 256;
				} while (result2 <= 0);
			}

			if (result1 > 256) {
				result1 = result1 % 256;
			}
			if (result2 > 256) {
				result2 = result2 % 256;
			}

			encrip.add(result1);
			encrip.add(result2);
		}
		// ...Converte List<Integer> p/ byte[]
		for (Integer integer : encrip) {
			out.writeUTF(Integer.toString(integer));
		}

		return baos.toByteArray();
	}

	public byte[] decrypt(byte[] msgData) throws IOException {

		int cont = 0;
		int resto;
		int total2 = 0;
		List<Character> decryptText = new ArrayList<Character>();
		List<Integer> encryptData = new ArrayList<Integer>();

		// ...Converte byte[] p/ List<Integer>
		ByteArrayInputStream bais = new ByteArrayInputStream(msgData);
		DataInputStream in = new DataInputStream(bais);
		while (in.available() > 0) {
			Integer element = Integer.parseInt(in.readUTF());
			encryptData.add(element);
		}

		do {
			cont++;
			int total1 = 256 * cont + 1;
			resto = total1 % determinante;
			if (resto == 0.0) {
				total2 = total1 / determinante;
			}
		} while (resto != 0);

		int res1 = total2 * in1;
		int res2 = total2 * in2;
		int res3 = total2 * in3;
		int res4 = total2 * in4;

		if (res1 < 0) {
			do {
				res1 = res1 + 256;
			} while (res1 <= 0);
		}

		if (res2 < 0) {
			do {
				res2 = res2 + 256;
			} while (res2 <= 0);
		}

		if (res3 < 0) {
			do {
				res3 = res3 + 256;
			} while (res3 <= 0);
		}

		if (res4 < 0) {
			do {
				res4 = res4 + 256;
			} while (res4 <= 0);
		}

		if (res1 > 256) {
			res1 = res1 % 256;
		}
		if (res2 > 256) {
			res2 = res2 % 256;
		}
		if (res3 > 256) {
			res3 = res3 % 256;
		}
		if (res4 > 256) {
			res4 = res4 % 256;
		}

		// rotina para decifrar de hill.
		for (int i = 0; i < encryptData.size(); i += 2) {
			int charSc1 = encryptData.get(i);
			int charSc2 = 0;
			if (i < encryptData.size() - 1) {
				charSc2 = encryptData.get(i + 1);
			}

			int result1 = (res1 * charSc1) + (res2 * charSc2);
			int result2 = (res3 * charSc1) + (res4 * charSc2);

			if (result1 < 0) {
				do {
					result1 = result1 + 256;
				} while (result1 <= 0);
			}

			if (result2 < 0) {
				do {
					result2 = result2 + 256;
				} while (result2 <= 0);
			}

			if (result1 > 256) {
				result1 = result1 % 256;
			}
			if (result2 > 256) {
				result2 = result2 % 256;
			}

			decryptText.add(asciiTable.get(result1));
			decryptText.add(asciiTable.get(result2));

		}
		return arrayToString(decryptText).getBytes();
	}

	public String arrayToString(List<Character> arrayChar) {
		StringBuilder sb = new StringBuilder();

		for (Character character : arrayChar) {
			sb.append(character);
		}

		return sb.toString();

	}

	public boolean isValidMatrix(int n1, int n2, int n3, int n4) {
		List<Integer> vlr1 = new ArrayList<Integer>();
		List<Integer> vlr2 = new ArrayList<Integer>();
		List<Integer> vlr3 = new ArrayList<Integer>();
		List<Integer> vlr4 = new ArrayList<Integer>();

		boolean ok = false;

		int resto;
		int positivo;

		// montar arreys de deivisores da matriz.
		if (n1 < 0) {
			for (int i = 0; i >= n1; i--) {
				if (i <= -2) {
					resto = n1 % i;
					if (resto == 0.0) {
						positivo = i * (-1);
						vlr1.add(positivo);
					}
				}
			}

		} else {

			for (int i = 0; i <= n1; i++) {
				if (i >= 2) {
					resto = n1 % i;
					if (resto == 0.0) {
						vlr1.add(i);
					}
				}
			}
		}

		if (n2 < 0) {
			for (int i = 0; i >= n2; i--) {
				if (i <= -2) {
					resto = n2 % i;
					if (resto == 0.0) {
						positivo = i * (-1);
						vlr2.add(positivo);
					}
				}
			}

		} else {
			for (int i = 0; i <= n2; i++) {
				if (i >= 2) {
					resto = n2 % i;
					if (resto == 0.0) {
						vlr2.add(i);
					}
				}
			}
		}

		if (n3 < 0) {
			for (int i = 0; i >= n3; i--) {
				if (i <= -2) {
					resto = n3 % i;
					if (resto == 0.0) {
						positivo = i * (-1);
						vlr3.add(positivo);
					}
				}
			}

		} else {

			for (int i = 0; i <= n3; i++) {
				if (i >= 2) {
					resto = n3 % i;
					if (resto == 0.0) {
						vlr3.add(i);
					}
				}
			}
		}

		if (n4 < 0) {
			for (int i = 0; i >= n4; i--) {
				if (i <= -2) {
					resto = n4 % i;
					if (resto == 0.0) {
						positivo = i * (-1);
						vlr4.add(positivo);
					}
				}
			}

		} else {

			for (int i = 0; i <= n4; i++) {
				if (i >= 2) {
					resto = n4 % i;
					if (resto == 0.0) {
						vlr4.add(i);
					}
				}
			}
		}

		// verificar divisores em comum
		for (Integer i1 : vlr1) {
			for (Integer i2 : vlr2) {
				if (i1.equals(i2)) {
					ok = true;
					break;
				}
			}

			if (!ok) {
				for (Integer i3 : vlr3) {
					if (i1.equals(i3)) {
						ok = true;
						break;
					}
				}
			}
			if (!ok) {
				for (Integer i4 : vlr4) {
					if (i1.equals(i4)) {
						ok = true;
						break;
					}
				}
			}
		}

		if (!ok) {

			for (Integer i2 : vlr2) {
				for (Integer i3 : vlr3) {
					if (i2.equals(i3)) {
						ok = true;
						break;
					}
				}

				if (!ok) {
					for (Integer i4 : vlr4) {
						if (i2.equals(i4)) {
							ok = true;
							break;
						}
					}
				}
			}
		}

		if (!ok) {
			for (Integer i3 : vlr3) {
				for (Integer i4 : vlr4) {
					if (i3.equals(i4)) {
						ok = true;
						break;
					}
				}
			}
		} // fim da validação.

		return ok;
	}

}
