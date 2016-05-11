import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


/*
 * Tic Tac Toe Server
 * Osman Ak, Cark Kruk, Manuel Medina, Jack Piontek
 * Server that opens, listens for client connection, then closes
 * v1.0
 * 
 */

public class TicTacToeServer {

	private Socket clientSocket;
	private static Socket socket = null;	// Initializes socket so that it can be used in loop
	private static ServerSocket serverSocket;
	private static int port_number;
	private static Object[][] clientList = new Object[2][4];	// Array that will store player information. Used Object class for polymorphism
	private final static int MAXCONNECTIONS = 2;
	private static int currentConnections = 0;
	
	private static TicTacToe game = null;	// Creates tic tac toe game variable to be initialized when both players join
	
	static boolean flag = true; 	// Flag to determine when the game stops. I have not included a false signal yet.
	private static int playerFlag = 0; // This variable determines which player's turn it is. If its 0, then its X's turn, if its 1, then its O's turn

	static boolean winFlag = false;
	static boolean tieFlag = false;
	
	public TicTacToeServer(Socket cSocket) throws IOException{
		this.clientSocket = cSocket;
	}
	
	//Closes server
	void close()
	{
		try
		{
			clientSocket.close();
			System.out.println( "Listening concluded; shutting down..." );
			serverSocket.close();
		}
		catch( Exception e )
		{
			System.out.println("Error closing clients/server");
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		//Checks to see if user inputed proper command line
		if ( args.length != 1 )
		{
			System.out.println( "Usage:" );
			System.out.println( "   TicTacToeServer <port number>");
			return;
		}
		
		//Takes port number from the command line
		port_number = Integer.parseInt( args[0] );
		
		//Creates a new server Socket using the port number
		serverSocket = new ServerSocket(port_number);
		System.out.println("Waiting for players on port " + port_number);
		
		/*
		 * Listens for clients trying to connect to the Server and
		 * adds them to a 2d array. Then
		 * assigns each client a player number.
		 */
		try{
			
		
			//Ensures that only 2 players can join.
			while(currentConnections < MAXCONNECTIONS){
			
				//For each player, creates a socket, reader, writer, and flag, then adds it to the array
				for(int i = 0; i < clientList.length; i++){
				
					socket = serverSocket.accept();
					clientList[i][0] = socket;															  // Player's socket
					clientList[i][1] = 	new BufferedReader( new InputStreamReader(socket.getInputStream()) ); // Player's buffered reader
					clientList[i][2] = 	new PrintWriter( socket.getOutputStream(), true );					  // Players print writer
					
					
					
					if(i == 0){																				  // A flag variable that determines whether the player is X or O
						clientList[i][3] = "X"; 
					} else {
						clientList[i][3] = "O";
					}
 																	  
					currentConnections ++;	// Updates number of people that are connected to the server
				}
				
				//Determines which player has joined
				assign();
			}
			
			// Lets player know if they are X or O
			for(int i = 0; i < clientList.length; i++){
				if(clientList[i][3].equals("X")){
					((PrintWriter) clientList[i][2]).println("You are X");
				} else {
					((PrintWriter) clientList[i][2]).println("You are O");
				}
			}
			
			//When both players have joined, initialize the game
			if(currentConnections == 2){
				game = new TicTacToe();		// Initializes the game
				System.out.println("Both players connected, starting game...");
			}
			
			System.out.println(game.toString());
			
			// This is where the server accepts alternating inputs from the clients. 
			while(flag == true){									// TODO Exception handling for bad input values
																	
				if(clientList[playerFlag][3].equals("X")){
	
					((PrintWriter) clientList[playerFlag][2]).println("Your turn to make a valid move ( 0-2 )");

					String input_line = ((BufferedReader) clientList[playerFlag][1]).readLine();
					
					while(input_line.length() != 2 ||  !(game.applyMove( Character.getNumericValue(input_line.charAt(0)), Character.getNumericValue(input_line.charAt(1))))){
						((PrintWriter) clientList[playerFlag][2]).println("Bad move, please try again");
						((PrintWriter) clientList[playerFlag][2]).println("Your turn to make a valid move ( 0-2 )");
						input_line = ((BufferedReader) clientList[playerFlag][1]).readLine();
					}
					
					System.out.println( "Received from client: " + input_line);
					
					int row = (int)input_line.charAt(0)-48; // For some reason the charAt returns the integer plus 48...
					int col = (int)input_line.charAt(1)-48;

					game.applyMove(row, col);
					System.out.println(playerFlag);
					System.out.println(game.toString());
					
					winFlag = game.didWon('x');
					tieFlag = game.didTie();
					
					flag = gameResults();
					playerFlag = 1;

				} else {
					
					((PrintWriter) clientList[playerFlag][2]).println("Your turn to make a valid move ( 0-2 )");

					String input_line = ((BufferedReader) clientList[playerFlag][1]).readLine();
					
					while(input_line.length() != 2 ||  !(game.applyMove( Character.getNumericValue(input_line.charAt(0)), Character.getNumericValue(input_line.charAt(1))))){
						((PrintWriter) clientList[playerFlag][2]).println("Bad move, please try again");
						((PrintWriter) clientList[playerFlag][2]).println("Your turn to make a valid move ( 0-2 )");
						input_line = ((BufferedReader) clientList[playerFlag][1]).readLine();
					}
					
					System.out.println( "Received from client: " + input_line);
					
					int row = (int)input_line.charAt(0)-48; // For some reason the charAt returns the integer plus 48...
					int col = (int)input_line.charAt(1)-48;

					game.applyMove(row, col);
					System.out.println(playerFlag);
					System.out.println(game.toString());
					
					winFlag = game.didWon('o');
					tieFlag = game.didTie();
					
					flag = gameResults();
					playerFlag = 0;

				}
			}
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
		
		//Closes sockets for safety
		serverSocket.close();
	}
	
	public static boolean gameResults(){
		
		if(winFlag == true || tieFlag == true){
			
			if ( winFlag == true) {
				System.out.println("Player " + (playerFlag + 1) + " won the game!");
				return false;
			}
			else {
				System.out.println("Tie game! No one wins.");
				return false;
			}
		}
		return true;
	}
	
	public static void assign(){
		
		if(clientList[0][0] == socket){
			System.out.println("Player 1 joined");
			System.out.println("Waiting for player 2...");
		}
		else{
			System.out.println("Player 2 joined");
		}	
	}
}

/*
 * PROTOCOL:
 * 
 * Server : input into console - java TicTacToeServer 1
 * 
 * Client 1: input into console - java TicTacToeClient 0 1
 * 
 * Client 2: input into console - java TicTacToeClient 0 1
 * 
 * Server receives both players and assigned X or O
 * 
 * Clients take turns alternating inputs into the server.
 * 
 * 
 */

