package br.com.sbk.sbking.learning;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CapitalizeToManyClientsServer {

	public static void main(String[] args) throws Exception {
		try (ServerSocket listener = new ServerSocket(60000)) {
			System.out.println("The capitalization server is running...");
			ExecutorService pool = Executors.newFixedThreadPool(20);
			while (true) {
				pool.execute(new Capitalizer(listener.accept()));
			}
		}
	}

	private static class Capitalizer implements Runnable {
		private Socket socket;

		Capitalizer(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			System.out.println("Connected: " + socket);
			Scanner in = null;
			InputStream socketInputStream;
			try {
				socketInputStream = socket.getInputStream();
				in = new Scanner(socketInputStream);
				PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
				while (in.hasNextLine()) {
					out.println(in.nextLine().toUpperCase());
				}
			} catch (Exception e) {
				System.out.println("Error:" + socket);
			} finally {
				if (in != null) {
					in.close();
				}
				try {
					socket.close();
				} catch (IOException e) {
				}
				System.out.println("Closed: " + socket);
			}
		}
	}

}
