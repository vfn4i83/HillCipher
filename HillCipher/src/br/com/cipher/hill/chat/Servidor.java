package br.com.cipher.hill.chat;

import java.net.*;
import java.util.Scanner;
import java.io.*;

import br.com.cipher.hill.Cipher;

public class Servidor {

	static final int serverPort = 2018;
	private static ServerSocket servidor;
	private static Scanner input;

	public static void main(String[] args) throws Exception {
		servidor = new ServerSocket(serverPort);
		System.out.println("»Servidor iniciado..." + '\n');
		Cipher cipher = new Cipher();
		cipher.initParameters();

		// 2. Recebendo (aguardando) conexão de algum cliente
		Socket cliente = servidor.accept();
		// 3. Iniciando canais (streams) de entrada e saída
		DataInputStream entrada = new DataInputStream(cliente.getInputStream());
		DataOutputStream saida = new DataOutputStream(cliente.getOutputStream());	
		
		input = new Scanner(System.in);

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
						System.out.println(">Cliente diz: " + decryptMsg);
					} catch (Exception e) {
					}
				}
			}
		}.start();

		// ...Mensagem de Saída:
		new Thread() {
			public void run() {
				System.out.println("»Chat iniciado..." + '\n');
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
