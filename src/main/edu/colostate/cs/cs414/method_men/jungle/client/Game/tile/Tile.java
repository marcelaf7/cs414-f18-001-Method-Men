package edu.colostate.cs.cs414.method_men.jungle.client.Game.tile;

import edu.colostate.cs.cs414.method_men.jungle.client.Game.piece.Piece;

/**
 * Tile class is responsible for tile-specific behavior. This includes:
 * 1. Knowing what type of piece it is.
 * 2. Knowing what piece is on it, or if there is no piece on it.
 */
public class Tile {
    private char attribute;
    private Piece piece;

    /**
     * Create a Tile object with a given attribute, of a set of 63 within the board.
     * @param attribute : '.', '~', 'T', 'D'. Describes the type of tile it is.
     * Note: Piece is set to null initially because, upon creation, we don't know if it has a piece or not.
     */
    public Tile(char attribute) {
        this.attribute = attribute;
        this.piece = null;
    }


    /*** Getters and Setters ***/

    public char getAttribute() {
        return attribute;
    }

    public Piece getPiece() { return piece;}

    public void setPiece(Piece piece){
        this.piece = piece;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tile tile = (Tile) o;

        return attribute == tile.attribute;
    }

    @Override
    public int hashCode() {
        return (int) attribute;
    }
}