import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8010;

    public void run() throws UnknownHostException, IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Message to send (type 'shutdown' to stop the server): ");
            String message = scanner.nextLine();

            // The socket and its streams close automatically when this block ends.
            try (Socket socket = new Socket(InetAddress.getByName("localhost"), PORT);
                 PrintWriter toServer = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader fromServer = new BufferedReader(
                         new InputStreamReader(socket.getInputStream()))) {

                toServer.println(message);
                String response = fromServer.readLine();
                if (response == null) {
                    System.out.println("The server closed the connection without replying.");
                } else {
                    System.out.println("Server replied: " + response);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Client().run();
        } catch (IOException e) {
            System.err.println("Could not connect to the server: " + e.getMessage());
        }
    }
}
