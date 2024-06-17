import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        // no ip address, assume the server is running on the local machine
        ServerSocket server = new ServerSocket(2000);

        // the server is running
        System.out.println("Server start to listen: " + server.getLocalSocketAddress());
        System.out.println("Server information: " + server.getInetAddress() + " P: " + server.getLocalPort());

        // the server is running, waiting for the client to connect
        // why using while loop? because we want to handle multiple clients
        while (true) {
            // wait for the client to connect
            Socket client = server.accept();
            // create a new thread to handle the client
            ClientHandler clientHandler = new ClientHandler(client);
            // start the thread
            clientHandler.start();
        }

    }

    // 1. create a different thread to handle the client, why? because we want to
    // handle multiple clients
    private static class ClientHandler extends Thread {
        private Socket socket;
        private boolean flag = true;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        // when clientHandler.start(); it will run this method run()
        @Override
        public void run() {
            super.run();
            // print the client information
            System.out.println("Client information: " + socket.getInetAddress() + " P: " + socket.getPort());
            // print the server information
            try {
                // send the data to the client, socketOutput here is the output stream, it is a
                // sender to the client side
                PrintStream socketOutput = new PrintStream(socket.getOutputStream());
                // receive the data from the client
                BufferedReader socketInput = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
                do {
                    // read the data from the client
                    String str = socketInput.readLine();
                    // if the data is 'bye', then close the connection
                    if ("bye".equalsIgnoreCase(str)) {
                        flag = false;
                        // send the data to the client
                        socketOutput.println("bye");
                    } else {
                        // print the data from the client
                        System.out.println(str);
                        // send the data to the client
                        socketOutput.println("echo: " + str);
                    }
                } while (flag);
                // close the input stream
                socketInput.close();
                // close the output stream
                socketOutput.close();
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
            } finally {
                try {
                    // close the socket
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Client closed: " + socket.getInetAddress() + " P: " + socket.getPort());
        }
    }
}
