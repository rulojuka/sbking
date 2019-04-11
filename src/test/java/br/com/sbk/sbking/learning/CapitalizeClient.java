package br.com.sbk.sbking.learning;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CapitalizeClient {
	Socket socket;

	public void connect() throws IOException {
		this.socket = new Socket("localhost", 60000);
	}

	public String capitalize(String text) {
		System.out.println("Enter lines of text then Ctrl+D or Ctrl+C to quit");
		Scanner scanner = new Scanner(text);
		PrintWriter out;
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(scanner.nextLine());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

		String nextLine = "";
		Scanner in;
		try {
			in = new Scanner(socket.getInputStream());
			nextLine = in.nextLine();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return nextLine;

	}
}
