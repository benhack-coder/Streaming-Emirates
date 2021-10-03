package experimental;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream dataOutput;


    public Client(String address, int port) throws IOException {


        socket = new Socket(address, port);
        dataOutput = new DataOutputStream(socket.getOutputStream());

        String line = "";

        while (!line.equals("Over")) {
            line = new Scanner(System.in).nextLine();
            dataOutput.writeUTF(line);
        }
        input.close();
        dataOutput.close();
        socket.close();
    }

    public static void convertToBytes() {}
    //TODO
}
