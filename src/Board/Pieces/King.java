package Board.Pieces;
import Board.Board;
import Board.Tile;
import java.awt.*;
import java.util.ArrayList;

public class King extends Piece {
    boolean flag = false;
    public King(Board board, Dimension dimension, piece_color color) {
        super(board, dimension, color);
        this.name = "King";
    }

    @Override
    protected boolean checkAndAddMove(int X, int Y, Tile[][] tiles) {
        if (checkBounds(X, Y, tiles)) {
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
