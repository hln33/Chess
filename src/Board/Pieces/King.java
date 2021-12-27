package Board.Pieces;
import Board.Board;
import Board.Tile;
import java.awt.*;
import java.util.ArrayList;

public class King extends Piece {
    public King(Board board, Dimension dimension, piece_color color) {
        super(board, dimension, color);
        this.name = "King";
    }

    // return true if a move does not cause king to go into check
    private boolean checkIfCheck(int X, int Y) {
        ArrayList<Piece> pieceList = this.parent_board.getPieceList();
        Tile[][] tiles = this.getParent_board().getTiles();

        for (Piece piece : pieceList) {
            // if a piece is a king then this loop becomes infinite because checkIfCheck will be called infinite times
            if (!(piece instanceof King) && piece.getColor() != this.color && piece.getAvailable_moves().contains(tiles[X][Y])) {
                return false;
            }
        }
        return true;
    }
    @Override
    protected boolean checkAndAddMove(int X, int Y, Tile[][] tiles) {
        if (checkBounds(X, Y, tiles) && checkIfCheck(X, Y)) {
            Tile newTile = tiles[X][Y];
            this.available_moves.add(newTile);
            return newTile.getPiece() == null || this.color == newTile.getPiece().color;
        }
        return false;
    }

    @Override
    public ArrayList<Tile> getAvailable_moves() {
        this.available_moves = new ArrayList<>();
        Tile[][] tiles = this.getParent_board().getTiles();
        int x = getCoordinates().width;
        int y = getCoordinates().height;
        int newX, newY;

        // if move causes king to go into check, it is not valid

        // down
        newX = x + 1;
        checkAndAddMove(newX, y, tiles);
        // up
        newX = x - 1;
        checkAndAddMove(newX, y, tiles);
        // right
        newY = y + 1;
        checkAndAddMove(x, newY, tiles);
        // left
        newY = y - 1;
        checkAndAddMove(x, newY, tiles);
        // up-right
        newX = x - 1;
        newY = y + 1;
        checkAndAddMove(newX, newY, tiles);
        // up-left
        newX = x - 1;
        newY = y - 1;
        checkAndAddMove(newX, newY, tiles);
        // down-right
        newX = x + 1;
        newY = y + 1;
        checkAndAddMove(newX, newY, tiles);
        // down-left
        newX = x + 1;
        newY = y - 1;
        checkAndAddMove(newX, newY, tiles);

        return this.available_moves;
    }
}
