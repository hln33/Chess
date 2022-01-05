package Board.Pieces;
import Board.Board;
import Board.Tile;

import java.util.ArrayList;

public class Pawn extends Piece {
    boolean unmoved = true;

    public Pawn(Board board, Tile location, piece_color color) {
        super(board, location, color);
        setImage("pawn");
    }

    @Override
    public ArrayList<Tile> getAvailable_moves() {
        this.available_moves = new ArrayList<>();
        Tile[][] tiles = this.getParent_board().getTiles();
        Tile candidate;
        int x = this.tile.getCoordinates().width;
        int y = this.tile.getCoordinates().height;
        int direction = (this.color == piece_color.black) ? 1 : -1;
        int newX, newY;

        // one step forward
        newX = x + direction;
        if (checkBounds(newX, y, tiles) && tiles[newX][y].getPiece() == null) {
            candidate = tiles[newX][y];
            this.available_moves.add(candidate);
        }

        // two steps forward
        newX = x + (2*direction);
        if (unmoved && checkBounds(newX, y, tiles) && tiles[newX][y].getPiece() == null) {
            candidate = tiles[newX][y];
            this.available_moves.add(candidate);
        }

        // diagonal move
        newX = x + direction;
        newY = y + 1;
        if (checkBounds(newX, newY, tiles) && tiles[newX][newY].getPiece() != null) {
            candidate = tiles[newX][newY];
            this.available_moves.add(candidate);
        }
        newY = y - 1;
        if (checkBounds(newX, newY, tiles) && tiles[newX][newY].getPiece() != null) {
            candidate = tiles[newX][newY];
            this.available_moves.add(candidate);
        }

        return this.available_moves;
    }

    public ArrayList<Tile> getEliminating_moves() {
        ArrayList<Tile> eliminatingMoves = new ArrayList<>();
        Tile[][] tiles = this.parent_board.getTiles();
        Tile candidate;
        int x = this.tile.getCoordinates().width;
        int y = this.tile.getCoordinates().height;
        int direction = (this.color == piece_color.black) ? 1 : -1;
        int newX, newY;

        newX = x + direction;
        newY = y + 1;
        if (checkBounds(newX, newY, tiles)) {
            candidate = tiles[newX][newY];
            eliminatingMoves.add(candidate);
        }
        newY = y - 1;
        if (checkBounds(newX, newY, tiles)) {
            candidate = tiles[newX][newY];
            eliminatingMoves.add(candidate);
        }

        return eliminatingMoves;
    }

    @Override
    public void setTile(Tile newLocation) {
        super.setTile(newLocation);
        unmoved = false;
    }
}
