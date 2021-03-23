package br.com.sbk.sbking.learning;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

public class HelloWorldClient {
    Socket socket;

    public void connect() throws IOException {
        this.socket = new Socket("localhost", 60000);
    }

    public String askHelloWorldOnServer() {
        InputStream inputStream = null;
        try {
            inputStream = this.socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner in = new Scanner(inputStream);
        String nextLine = in.nextLine();
        in.close();
        return nextLine;
    }

}
