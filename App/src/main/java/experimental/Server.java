package experimental;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private Socket socket;
    private ServerSocket server;
    private DataInputStream inputStream;

    public Server(int port) throws IOException {

        server = new ServerSocket(port);

        System.out.println("Waiting for client...");

        socket = server.accept();
        inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));


        byte line;
        while (true) {
            line = inputStream.readByte();
            System.out.println(line);
        }
    }
}
