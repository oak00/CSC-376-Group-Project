import java.net.InetAddress;

//C:\Users\Carl\Desktop\JuniorYear\CSC_376_DistributedSystems\Project\Project\bin>
public class TicTacToeClient {

	public static void main(String[] args) {
		if ( args.length != 2 )
		{
			System.out.println( "Usage:" );
			System.out.println( "   java TicTacToeClient <host name> <port number>");
			return;
		}
		
		try
		{
			Client client = new Client( ( args[0] == "localhost" ? InetAddress.getLoopbackAddress() : InetAddress.getByName( args[0] ) ), Integer.parseInt( args[1] ) );
			//client.close();
			//client.receiveToken();
			client.waitForAction(); //Receive game token
			
			boolean x = true;
			
			while (x)
			{
				x = TicTacToeServer.flag;
				client.waitForAction();
			}
			client.close();
		}
		catch ( Exception e )
		{
			
		}
	}

}
