package edu.colostate.cs.cs414.method_men.jungle.client;

import java.util.Arrays;

public class Location {
    private int[] location;

    public Location(int[] location) {
        this.location = new int[2];
        this.location[0] = location[0];
        this.location[1] = location[1];
    }

    public Location(int row, int col) {
        this.location = new int[2];
        this.location[0] = row;
        this.location[1] = col;
    }

    public Location(Location loc) {
        this.location = new int[2];
        this.location[0] = loc.getRow();
        this.location[1] = loc.getCol();
    }

    public int getRow() {
        return this.location[0];
    }

    public int getCol() {
        return this.location[1];
    }

    public int[] getLocation() {return this.location;}

    /**
     * Checks if the next move's location is out of bounds
     * @param row the next move's horizontal location on the board
     * @param col the next move's vertical location on the board
     * @return true if it is OOB, false if in bounds
     */
    public static boolean isOutOfBounds(int row, int col) {
        return (row < 0 || row > 8 || col < 0 || col > 6);
    }

    /**
     * Checks if the next move's location is out of bounds
     * @param row the next move's horizontal location on the board
     * @param col the next move's vertical location on the board
     * @return true if it is OOB, false if in bounds
     */
    public static boolean isOutOfBounds(Location loc) {
        return isOutOfBounds(loc.getRow(), loc.getCol());
    }

    //Checks if the move is 1 up/down/left/right move away. Doesn't care about bounds
    public boolean isAdjacent(Location location){
        if (this.getCol()+1 == location.getCol() && this.getRow()   == location.getRow() || //Moving right
            this.getCol()-1 == location.getCol() && this.getRow()   == location.getRow() || //Moving left
            this.getCol()   == location.getCol() && this.getRow()-1 == location.getRow() || //Moving up
            this.getCol()   == location.getCol() && this.getRow()+1 == location.getRow()){  //Moving down
            return true;
        }
        return false;
    }

    @Override
    public String toString(){
        return "[" + this.getCol() + ", " + this.getRow() + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location1 = (Location) o;

        return Arrays.equals(location, location1.location);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(location);
    }
}
