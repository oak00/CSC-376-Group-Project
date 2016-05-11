
/*
 * Tic Tac Toe Server Communicator
 * Osman Ak, Cark Kruk, Manuel Medina, Jack Piontek
 * Server Communicator that contains methods for server operation
 * v1.0
 * 
 */

import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ServerCommunicator implements Runnable {
	int port_number;
	ServerSocket server_socket;
	Socket client_socket;

	public ServerCommunicator( int port ) throws IOException {
		port_number= port;
		server_socket= new ServerSocket( port_number );
		System.out.println( "Listening on port " + Integer.toString( port_number ) );
	}
	
	public void run() {
		try{
			this.listen();
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	public void listen() throws IOException
	{
		// listen for a connection
		client_socket= server_socket.accept();
		
		// grab the input and output streams
		BufferedReader reader= new BufferedReader( 
				 new InputStreamReader(client_socket.getInputStream()) );
		PrintWriter output= new PrintWriter( client_socket.getOutputStream(), true );
		
		// read the input
		String input_line= reader.readLine();
		System.out.println( "Received from client: " );
		System.out.println( input_line );

		// send the message back to the client
		output.println( input_line );
		
		/*
		 * The above code is the basic server listen set up that we used in class (actually this code is almost directly from
		 * our previous class assignments. If Manuel's comments are to be used, then this listen() method is probably where
		 * we will initialized the tic tac toe constructor and game. Once the server class will call the listen class, 
		 * which will initialize the game, then either shut off once its done or reprompt to play again.
		 * 
		 */	
	}
	
	void close()
	{
		try
		{
			client_socket.close();
			System.out.println( "Listening concluded; shutting down..." );
			server_socket.close();
		}
		catch( Exception e )
		{
			// ignore
		}
	}
}
