import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class Client {
	
	private InetAddress address;
	private int port;
	private Socket socket;
	
	BufferedReader in;
	PrintWriter out;
	
	public Client(InetAddress address, int port) //create client socket and connect to server
	{
		System.out.println( "Client looking for server..." );
		try
		{
			socket = new Socket(address, port);
			this.address = address;
			this.port = port;
			
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			
			System.out.println( "Client found server\n" );
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
	}
	
	public void close() //close socket
	{
		System.out.println("Closing client");
		try
		{
			socket.close();
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
		System.out.println("Client closed\n");
	}
	
	public void receiveGame() throws IOException
	{
		System.out.println();
		String s = null;
		System.out.println( "Waiting for update game state" );
		while ( (s = in.readLine()) != null )
		{
			System.out.println(in.readLine());
		}
		//System.out.println("Received from server game: \n" + s);
		System.out.println();
	}
	
	//------------------------------------------------------------------------------
	
	public void transmitStandardInput() //default option
	{
		try
		{
			System.out.println("Begin typing message to server: ");
			String line = null;
			BufferedReader in = new BufferedReader(new InputStreamReader( System.in ));
			line = in.readLine();
			this.sendMessage( line );
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
	}
	
	public void sendMessage( String s ) //send string
	{
		System.out.println( "Client sending message..." );
		
		try
		{
			out.println( s );
		}
		catch ( Exception e )
		{
			System.out.println( e.getMessage() );
		}
		
		System.out.println( "Message sent\n" );
	}
	
	//------------------------------------------------------------------------------
	
	public boolean waitForAction() throws IOException
	{

		String s;
		System.out.println( "Waiting" );
		while ( (s = in.readLine()) == null )
		{
			
		}
		System.out.println("Received from server: " + s);
		
		if ( s.equals("Your turn to make a valid move ( 0-2 )") )
		{
			transmitStandardInput();
			return true;
		}
		else if ( s.equals("Board: ") )
		{
			receiveGame();
			return true;
		}
		return false;
	}
	
}
