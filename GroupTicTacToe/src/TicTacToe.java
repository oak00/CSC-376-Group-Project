//import whatever we need to get input from the 

public class TicTacToe {
	private char[][] board;
	private char currentPlayer;
	private int remain;
	private boolean done;
	
	public TicTacToe(){
		board = new char[3][3];
		remain = 9;
		done = false;
		currentPlayer = 'x';
		initializeBoard();
		
		//playGame();
		//^ will this be initiated in the server?
	}	
	//Set and resets the board for new games
	public void initializeBoard(){
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				board[i][j] = ' ';
			}
		}
	}
	
	public char getCurrentToken()
	{
		return currentPlayer;
	}
	
	//Starts and continues game until is winner
	//Again apply to the server
//	public void playGame(){
//		while(!done){
//			applyMove();
//			didWin();
//			didTie();
//			changePlayer();
//		}
//	}
	
	//Changes player
	//Server can use this rather than implementing it in the server
	public void changePlayer(){
		if(currentPlayer == 'x'){
			currentPlayer = 'o';
		} else{
			currentPlayer = 'x';
		}
	}
	
	//Specs said to return true if valid move? 
	//if flase the server will ask for input again
	//and then call this method again until it finds a valid move?
	public boolean applyMove(int row, int col){
		//boolean validMove = false;
		System.out.println(row + " " + col);
		if(row >= 0 && row < 3){
			if(col >= 0 && col < 3){
				if(board[row][col] == ' '){
					board[row][col] = currentPlayer;
					remain--;
					changePlayer(); //if you remove this, remove the ternary operation on the outer while loop in the TicTacToeServer and just leave the function call
					return true;
				}
			}
		}
		return false;
	}
	
	// Checks turn counter and if there is a not a win it returns a tie
	public boolean didTie(){
		if(remain == 0){
			done = true;
			return true;
		}
		return false;
	}
	
	public boolean didWon(char currentPlayer)
	{

	    int i, j;
	    //boolean win = false;

	    // Check win by a row
	    for (i = 0; i < board.length && !done; i++) {
	      for (j = 0; j < board[0].length; j++) {
	        if (board[i][j] != currentPlayer)
	          break;
	      }
	      if (j == board[0].length)
	        done = true;
	    }

	    // Check win by a column
	    for (j = 0; j < board[0].length && !done; j++) {
	      for (i = 0; i < board.length; i++) {
	        if (board[i][j] != currentPlayer)
	          break;
	      }
	      if (i == board.length)
	        done = true;
	    }

	    // Check win by a diagonal (1)
	    if (!done) {
	      for (i = 0; i < board.length; i++) {
	        if (board[i][i] != currentPlayer)
	          break;
	      }
	      if (i == board.length)
	        done = true;
	    }

	    // Check win by a diagonal (2)
	    if (!done) {
	      for (i = 0; i < board.length; i++) {
	        if (board[i][board.length - 1 - i] != currentPlayer)
	          break;
	      }
	      if (i == board.length)
	        done = true;
	    }

	    // Finally return win
	    return done;
	  }
	
	//I'm kind of stuck here
	//Wasn't fully sure how to convert 2d array to string
	//I don't know if this will fully work yet
	//public static String displayBoard(char[][] board){
	public String toString()
	{
		String aString = "";
		for(int row = 0; row < 3; row ++){
			for(int col = 0; col < 3; col++){
				aString += " " + (board[row][col] == ' ' ? '_' : board[row][col]);
			}
			aString += "\n";
		}
		return aString;
	}

	
	
	
}
