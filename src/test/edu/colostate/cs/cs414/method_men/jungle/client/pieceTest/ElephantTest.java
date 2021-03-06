package edu.colostate.cs.cs414.method_men.jungle.client.pieceTest;

import edu.colostate.cs.cs414.method_men.jungle.client.Game.Board;
import edu.colostate.cs.cs414.method_men.jungle.client.Game.Location;
import edu.colostate.cs.cs414.method_men.jungle.client.Game.piece.Elephant;
import edu.colostate.cs.cs414.method_men.jungle.client.Game.piece.Piece;
import edu.colostate.cs.cs414.method_men.jungle.client.Game.piece.Rat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElephantTest {
    private Elephant testElephant;
    private Board board;
    private Piece elephant;

    @BeforeEach
    void init() {
        board = new Board();
        elephant = board.getTile(new Location(2,6)).getPiece();
    }

    @Test
    void constructor1() {
        testElephant = new Elephant("red");
        Location loc1 = testElephant.getLocation();
        assertTrue(loc1.getRow() == 2 && loc1.getCol() == 6);
    }

    @Test
    void constructor2() {
        testElephant = new Elephant("blue");
        Location loc2 = testElephant.getLocation();
        assertTrue(loc2.getRow() == 6 && loc2.getCol() == 0);
    }

    @Test
    void testValidMoveElephantOntoOpenTileRat(){
        //Move elephant next to enemy rat
        board.move(elephant, new Location(5, 6));
        assertTrue(board.getTile(5,6).getPiece() instanceof Elephant);
        assertTrue(board.getTile(2,6).getPiece() == null);

        //Onto rat is invalid
        assertFalse(elephant.isValidMove(new Location(6,6), board));
    }

    @Test
    void testValidMoveElephantOntoEnemyTrapTileRat(){
        Piece enemyRat = board.getTile(6, 6).getPiece();

        //Move elephant next to enemy trap
        board.move(elephant, new Location(6, 3));
        assertTrue(board.getTile(6,3).getPiece() instanceof Elephant);
        assertTrue(board.getTile(2,6).getPiece() == null);

        //Move enemy rat onto own trap
        board.move(enemyRat, new Location(7, 3));
        assertTrue(board.getTile(7,3).getPiece() instanceof Rat);
        assertTrue(board.getTile(6,6).getPiece() == null);

        //Onto rat is invalid
        assertFalse(elephant.isValidMove(new Location(7,3), board));
    }

    @Test
    void testValidMoveElephantOntoOpenTileEnemyElephant(){
        //Move elephant next to enemy elephant
        board.move(elephant, new Location(5, 0));
        assertTrue(board.getTile(5,0).getPiece() instanceof Elephant);
        assertTrue(board.getTile(2,6).getPiece() == null);

        //Onto elephant is valid
        assertTrue(elephant.isValidMove(new Location(6,0), board));
    }

    @Test
    void testValidMoveElephantOntoEnemyTrapTileElephant(){
        Piece enemyElephant = board.getTile(6, 0).getPiece();

        //Move elephant next to enemy trap
        board.move(elephant, new Location(6, 3));
        assertTrue(board.getTile(6,3).getPiece() instanceof Elephant);
        assertTrue(board.getTile(2,6).getPiece() == null);

        //Move enemy rat onto own trap
        board.move(enemyElephant, new Location(7, 3));
        assertTrue(board.getTile(7,3).getPiece() instanceof Elephant);
        assertTrue(board.getTile(6,0).getPiece() == null);

        //Onto rat is invalid
        assertTrue(elephant.isValidMove(new Location(7,3), board));
    }

}
