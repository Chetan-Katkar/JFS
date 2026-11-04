import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private static final int PORT = 8010;
    private static final String SHUTDOWN_COMMAND = "shutdown";

    public void run() throws IOException {
        // This socket listens for new clients until the server is stopped.
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server listening on port " + PORT);
            boolean running = true;

            while (running) {
                System.out.println("Waiting for a client...");

                // Handle one client at a time. These resources close at the end
                // of this block, before the server accepts the next client.
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader fromClient = new BufferedReader(
                             new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter toClient = new PrintWriter(
                             clientSocket.getOutputStream(), true)) {

                    String message = fromClient.readLine();
                    if (message == null) {
                        System.out.println("Client disconnected without sending a message.");
                        continue;
                    }

                    System.out.println("Client says: " + message);
                    boolean shutdownRequested = SHUTDOWN_COMMAND.equalsIgnoreCase(message.trim());
                    if (shutdownRequested) {
                        toClient.println("Server shutting down.");
                        running = false;
                    } else {
                        toClient.println("Server received: " + message);
                    }
                } catch (IOException e) {
                    System.err.println("Could not handle client connection: " + e.getMessage());
                }
            }

            System.out.println("Server stopped.");
        }
    }
    // entry point of the code.
    public static void main(String[] args) {
        try {
            new Server().run();
        } catch (IOException e) {
            System.err.println("Server could not start or continue: " + e.getMessage());
        }
    }
}
