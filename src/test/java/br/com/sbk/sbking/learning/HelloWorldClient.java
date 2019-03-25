package br.com.sbk.sbking.learning;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class HelloWorldClient {
	Socket socket;

	public void connect() throws IOException {
		this.socket = new Socket("localhost", 60000);
	}

	public String askHelloWorldOnServer() throws IOException {
		Scanner in = new Scanner(this.socket.getInputStream());
		return in.nextLine();
	}

}
