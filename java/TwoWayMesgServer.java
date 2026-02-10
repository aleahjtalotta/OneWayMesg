/*
 * Implementation of a one-way message server in java
 */

// Package for I/O related stuff
import java.io.*;

// Package for socket related stuff
import java.net.*;

/*
 * This class does all the server's job
 * It receives the connection from client
 * and prints messages sent from the client
 */
public class TwoWayMesgServer {
	/*
	 * The server program starts from here
	 */
	public static void main(String args[]) {
		// Server needs the port number to listen on
		if (args.length != 2) { //CHANGED
			System.out.println("usage: java TwoWayMesgServer <port> <serverName>");
			System.exit(1);
		}

		// Get the port on which server should listen */
		int serverPort = Integer.parseInt(args[0]);
		String serverName = args[1]; // ADDED

		// Be prepared to catch socket related exceptions
		try {
			// Create a server socket with the given port
			ServerSocket serverSock = new ServerSocket(serverPort);
			System.out.println("Waiting for a client ...");

			// Wait to receive a connection request
			Socket clientSock = serverSock.accept();
			System.out.println("Connected to a client at ('" +
												((InetSocketAddress) clientSock.getRemoteSocketAddress()).getAddress().getHostAddress()
												+ "', '" +
												((InetSocketAddress) clientSock.getRemoteSocketAddress()).getPort()
												+ "')"
												);

			// No other clients, close the server socket
			serverSock.close();

			// Prepare to read from client
			BufferedReader fromClientReader = new BufferedReader(
					new InputStreamReader(clientSock.getInputStream()));

			PrintWriter toClientWriter = new PrintWriter(                 // ADDED
					clientSock.getOutputStream(), true);                  // ADDED

			BufferedReader fromUserReader = new BufferedReader(           // ADDED
					new InputStreamReader(System.in));                    // ADDED

			Thread receiver = new Thread(() -> {                          // ADDED
				try {                                                    // ADDED
					while (true) {                                       // ADDED
						String message = fromClientReader.readLine();    // ADDED
						if (message == null) {                           // ADDED
							System.out.println("Client closed connection"); // ADDED
							clientSock.close();                           // ADDED
							break;                                        // ADDED
						}                                                // ADDED
						System.out.println(message);        // ADDED
					}                                                    // ADDED
				} catch (Exception e) {}                                  // ADDED
			});                                                          // ADDED
			receiver.start(); //added
        
			while (true) {                                                // ADDED
				String line = fromUserReader.readLine();                 // ADDED
				if (line == null) {                                      // ADDED
					System.out.println("Closing connection");            // ADDED
					clientSock.close();                                  // ADDED
					break;                                               // ADDED
				}                                                       // ADDED
				toClientWriter.println(serverName + ": " + line);               // ADDED
			}                                                            // ADDED
			
		}
		catch(Exception e) {
			// Print the exception message
			System.out.println(e);
		}
	}
}
