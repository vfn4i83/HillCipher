package br.com.cipher.hill.chat;

import java.net.*;
import java.io.*;
import java.util.Scanner;

import br.com.cipher.hill.Cipher;

public class Cliente {
	static final String serverAddress = "127.0.0.1";
	static final int serverPort = 2018;
	private static Socket servidor;

	public static void main(String[] args) throws Exception {

		servidor = new Socket(serverAddress, serverPort);
		System.out.println("»Cliente iniciado..." + '\n');
		
		// Objeto p/ criptografia.
		Cipher cipher = new Cipher();
		cipher.initParameters();

		// Criar os canais (streams) de entrada e saída
		DataInputStream entrada = new DataInputStream(servidor.getInputStream());
		DataOutputStream saida = new DataOutputStream(
				servidor.getOutputStream());
				
		// ...Mensagem de entrada:
		new Thread() {
			public void run() {
				while (true) {
					try {
						int tamanho = entrada.readInt();
						byte[] encryptedData = new byte[tamanho];
						entrada.readFully(encryptedData, 0, encryptedData.length);
						byte[] decryptData = cipher.decrypt(encryptedData);
						String decryptMsg = new String(decryptData);
						System.out.println(">Servidor diz: " + decryptMsg);
					} catch (Exception e) {
					}
				}
			}
		}.start();

		// ...Mensagem de Saída:
		new Thread() {
			private Scanner input;

			public void run() {
				System.out.println("»Chat iniciado..." + '\n');
				input = new Scanner(System.in);
				while (true) {
					try {
						String resposta = input.nextLine();
						byte[] encryptData = cipher.encrypt(resposta.getBytes());
						saida.writeInt(encryptData.length);
						saida.write(encryptData);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}

}
