package Board.Pieces;
import Board.Board;
import Board.Tile;
import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
    protected final Board parent_board;
    private Dimension coordinates;
    protected ArrayList<Tile> available_moves;
    protected String name;
    piece_color color;

    protected boolean checkBounds(int X, int Y, Tile[][] tiles) {
        // boundary checks
        if (X < 0 || X > 7) return false;
        if (Y < 0 || Y > 7) return false;

        // blocking check
        Tile candidate = tiles[X][Y];
        return candidate.getPiece() == null || this.color != candidate.getPiece().color;
    }
    protected boolean checkAndAddMove(int X, int Y, Tile[][] tiles) {
        if (checkBounds(X, Y, tiles)) {
            Tile newTile = tiles[X][Y];
            this.available_moves.add(newTile);
            return newTile.getPiece() == null || this.color == newTile.getPiece().color;
        }
        return false;
    }

    Piece(Board board, Dimension coordinates, piece_color color) {
        this.parent_board = board;
        this.coordinates = coordinates;
        this.color = color;
    }

    public Board getParent_board() {
        return this.parent_board;
    }
    public piece_color getColor() {
        return this.color;
    }

    public void setCoordinates(Dimension newCoordinates) {
        this.coordinates = newCoordinates;
    }
    public Dimension getCoordinates() {
        return this.coordinates;
    }

    public abstract ArrayList<Tile> getAvailable_moves();

    public String getName() {
        return this.name;
    }
}
