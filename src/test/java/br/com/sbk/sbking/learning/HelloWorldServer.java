package br.com.sbk.sbking.learning;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HelloWorldServer {
    private ServerSocket listener = null;
    private Socket socket = null;

    public void initialize() {
        try {
            this.listener = new ServerSocket(60000);
            System.out.println("The server is running...");
            try {
                this.socket = this.listener.accept();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("Hello world from server.");
            } catch (Exception e) {
                // TODO: handle exception
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
