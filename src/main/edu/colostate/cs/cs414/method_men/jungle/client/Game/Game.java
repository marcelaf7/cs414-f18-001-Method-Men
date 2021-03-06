package edu.colostate.cs.cs414.method_men.jungle.client.Game;

import edu.colostate.cs.cs414.method_men.jungle.client.socket.ClientSend;
import edu.colostate.cs.cs414.method_men.jungle.client.Game.piece.*;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Game class is responsible for game-specific behavior as well as state. This includes:
 * 1. Knowing whose turn it is, and changing the turn.
 * 2. Knowing the state of the board, and relaying commands to Board.java to change the board state.
 * 3. Checking if the game is won, who won it, and ending the game.
 */
public class Game {
    private Player[] players;
    private int turn;
    private Board board;
    private Socket socket;
    int moveCount = 0;
    private String username;
    boolean onlineGame = false;
    private String redName;
    private String blueName;
    private String blue;
    private String red;


    /**
     * Create a new Game object.
     * Instantiates two players,
     * chooses which player goes first,
     * instantiates a new board.
     */
    public Game (Socket socket, String blue, String red) {
        this.blueName = blue;
        this.redName = red;
        this.socket = socket;
        this.blue = blue;
        this.red  =red;
        //this.username = username;
        players = new Player[2];
        players[0] = new Player("red", red);
        players[1] = new Player("blue", blue);
        turn = 1; // 1 means that Bottom Player makes the first move
        board = new Board();
        this.onlineGame = true;
    }

    public String getBluePlayer(){
        return this.blue;
    }

    public String getRedPlayer(){
        return this.red;
    }

    public Game (Socket socket, String username) {
        this.socket = socket;
        this.username = username;
        players = new Player[2];
        players[0] = new Player("red");
        players[1] = new Player("blue");
        turn = 1; // 1 means that Bottom Player makes the first move
        board = new Board();
    }

    //Constructor without socket for tests
    public Game () {
        players = new Player[2];
        players[0] = new Player("red");
        players[1] = new Player("blue");
        turn = 1; // 1 means that Bottom Player makes the first move
        board = new Board();
    }

    /**
     * Takes a starting location and end location as arguments. Returns true if piece at start is moved to end location.
     * @param start location the user has selected, intending to move the piece on it. (May not have a piece).
     * @param end location the user wishes to move their selected piece.
     * @return whether the move was successful or not.
     */
    //online game
    public boolean makeMove(Location start, Location end, Long ID, String currUser){
        System.out.println("Game.makeMove()");
        Piece piece = board.getTile(start).getPiece();

        System.out.println("Controlling user: " + currUser);

        //Check if we are trying to move not a piece.
        if (piece == null){
            System.out.println(players[turn].getColor() + "'s trying to move a fricking empty spot");
            return false;
        }

        System.out.println(players[turn].getColor() + "'s trying to move " + piece.getColor() + " " + piece.getName());

        System.out.println("turn username " + players[turn].getUsername());
        System.out.println("condition " + !currUser.equals(players[turn].getUsername()));

        if(!currUser.equals(players[turn].getUsername())){
           System.out.println("Not your turn");
           return false;
        }else if (!piece.getColor().equals(players[turn].getColor())){
            System.out.println("Not your piece");
            return false;
        }else if (piece.isValidMove(end, board)){
            System.out.println(players[turn].getColor() + "'s move is valid ");
            board.move(piece, end);
            turn = (turn + 1)%2;

            //send game state
            moveCount++;
            ArrayList<Piece> red = this.getBoard().getPieces("red");
            ArrayList<Piece> blue = this.getBoard().getPieces("blue");
            int winner = this.winnerCheck();
            String state = GameState.makeGameState(this.username, winner, this.turn, moveCount, red, blue, ID);
            try{
                ClientSend cSend = new ClientSend(this.socket);
                cSend.sendState(state);
            }catch(Exception e){}
            return true;
        }
        System.out.println(players[turn].getColor() + "'s move is Invalid ");
        return false;
    }

    //Simply another way to access the above method, makeMove(Location, Location).
    public boolean makeMoveLocalGame(int currentRow, int currentCol, int nextRow, int nextCol){
        return makeMoveLocal(new Location(currentRow, currentCol), new Location(nextRow, nextCol));
    }

    //Local Game
    private boolean makeMoveLocal(Location start, Location end){
        System.out.println("Game.makeMove()");
        Piece piece = board.getTile(start).getPiece();

        //Check if we are trying to move not a piece.
        if (piece == null){
            System.out.println(players[turn].getColor() + "'s trying to move a fricking empty spot");
            return false;
        }

        System.out.println(players[turn].getColor() + "'s trying to move " + piece.getColor() + " " + piece.getName());

        if (!(piece.getColor().equals(players[turn].getColor()))){
            System.out.println("Not your piece");
            return false;
        }

        else if (piece.isValidMove(end, board)){
            System.out.println(players[turn].getColor() + "'s move is valid ");
            board.move(piece, end);
            turn = (turn + 1)%2;
            return true;
        }
        System.out.println(players[turn].getColor() + "'s move is Invalid ");
        return false;
    }

    //Simply another way to access the above method, makeMove(Location, Location).
    public boolean makeMove(int currentRow, int currentCol, int nextRow, int nextCol, Long ID, String currUser){
        return makeMove(new Location(currentRow, currentCol), new Location(nextRow, nextCol), ID, currUser);
    }

    /**
     * There are two ways to win in Jungle:
     * 1. One player moves a piece onto the opposing players Den.
     * 2. One player has no pieces left.
     * @return 0 for top/red Player, 1 for bottom/blue Player, -1 for no Winners yet.
     */
    public int winnerCheck(){
        Player redPlayer = players[0];
        Player bluePlayer = players[1];

        //Look to see if blue piece on red den
        if (board.getTile(0, 3).getPiece() != null) {
            if (!(board.getTile(0, 3).getPiece().getColor().equals(redPlayer.getColor()))) {
                return 1; //Blue victory aka bot victory
            }
        }
        //Look to see if no red pieces
        if (board.getPieces("red").isEmpty()){
           return 1; //Blue victory aka bot victory
        }

        //Look to see if red piece on blue den
        if (board.getTile(8, 3).getPiece() != null) {
            if (!(board.getTile(8, 3).getPiece().getColor().equals(bluePlayer.getColor()))) {
                return 0; //Red victory aka top victory
            }
        }
        //Look to see if no blue pieces
        if (board.getPieces("blue").isEmpty()){
            return 0; //Red victory aka top victory
        }

        //No winner yet
        return -1;
    }

    /**
     * Match has concluded. Prints who won this match to command line.
     */
    public void endGame() {
        if (turn == 0) {
            System.out.println("\tBottom Player is the winner!");
        } else {
            System.out.println("\tTop Player is the winner!");
        }
    }

    /**
     * Get the moves the piece on location can make.
     * @param location the location which has the piece.
     * @return the possible moves the piece can make.
     */
    private ArrayList<Location> getValidMoves(Location location) {
        Piece piece = board.getBoard().get(location).getPiece();

        //There is a piece there
        if (piece != null) {
            //..., and the current player owns it.
            if (piece.getColor().equals(players[turn].getColor())) {
                //Get the valid moves for that piece
                return piece.getAllValidMoves(board);
            }
        }
        return new ArrayList<>();
    }

    //A way to access the above method, getValidLocations(Location).
    public ArrayList<Location> getValidMoves(int row, int col) {
        return getValidMoves(new Location(row, col));
    }

    public int getTurn() {
        return this.turn;
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Makes it the next player's turn.
     */
    public void incrementTurn() {
        this.turn = (this.turn + 1) % 2;
    }


    public void setTurn(int turn) {
        this.turn = turn;
    }
}
