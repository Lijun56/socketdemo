import java.io.BufferedReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.print.attribute.standard.OutputDeviceAssigned;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

public class Client {
    public static void main(String[] args) throws IOException {
        // Create a new socket
        // what is socket? A socket is one endpoint of a two-way communication link
        // between two programs running on the network.
        Socket socket = new Socket();
        // timeout
        socket.setSoTimeout(3000);
        // connect to the server
        // 1. why use Inet4Address.getLocalHost()? because we are using IPv4
        // 2. why use 2000? because we are using port 2000
        // 3. why use 3000? because we are setting the timeout to 3000
        // 4. why use InetSocketAddress? because it is a socket address
        // 5. what is socket address? A socket address is the combination of an IP
        // address and a port number
        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 2000), 3000);

        System.out.println("Client start to connect server(sent connection request to server)");
        System.out.println("Client information: " + socket.getLocalAddress() + " P: " + socket.getLocalPort());
        System.out.println("Server information: " + socket.getInetAddress() + " P: " + socket.getPort());

        try {
            // send Data to server
            todo(socket);
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        // close the socket,
        // release the resources
        socket.close();
        System.out.println("Client closed");
    }

    private static void todo(Socket client) throws IOException {
        // get the input stream
        // simulating the input from the keyboard
        InputStream in = System.in;
        // 1. create a buffer reader, why use buffer reader? because it is more
        // efficient,
        // 2. why use input stream reader? because it is a bridge from byte streams to
        // character streams(bufferreader)
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        // get the socket output stream and transform it to print stream
        // why use print stream? because it is more convenient, it can print the data,
        // it is a decorator of output stream
        OutputStream outputStream = client.getOutputStream();
        PrintStream socketPrintStream = new PrintStream(outputStream);

        // get the socket input stream and transform it to buffer reader
        InputStream inputStream = client.getInputStream();
        BufferedReader socketBufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        // continue to send the data to the server, and receive the data from the
        // server, until the message data is 'bye', then close the connection
        boolean flag = true;
        do {

            // get the socket from the Client input(keyboard) and transform it to string
            String str = input.readLine();
            // send the data to the server
            socketPrintStream.println(str);
            // get the data from the server
            String echo = socketBufferedReader.readLine();
            // if the message data is 'bye', then close the connection
            if ("bye".equalsIgnoreCase(echo)) {
                flag = false;
            } else {
                System.out.println("Client received the message data from server: " + echo);
            }
        } while (flag);
        // close the input stream
        socketPrintStream.close();
        socketBufferedReader.close();
    }

}
