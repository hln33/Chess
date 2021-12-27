package Board.Pieces;
import Board.Board;
import Board.Tile;
import java.awt.*;
import java.util.ArrayList;

public class Rook extends Piece {
    public Rook(Board board, Dimension dimension, piece_color color) {
        super(board, dimension, color);
        this.name = "R";
    }

    @Override
    public ArrayList<Tile> getAvailable_moves() {
        this.available_moves = new ArrayList<>();
        Tile[][] tiles = this.getParent_board().getTiles();
        int x = getCoordinates().width;
        int y = getCoordinates().height;
        int newX, newY;

        // up
        for (int i = 1; i < 8; ++i) {
            newX = x - i;
            if (!checkAndAddMove(newX, y, tiles)) break;
        }
            // down
        for (int i = 1; i < 8; ++i) {
            newX = x + i;
            if (!checkAndAddMove(newX, y, tiles)) break;
        }
        // left
        for (int i = 1; i < 8; ++i) {
            newY = y - i;
            if (!checkAndAddMove(x, newY, tiles)) break;
        }
        // right
        for (int i = 1; i < 8; ++i) {
            newY = y + i;
            if (!checkAndAddMove(x, newY, tiles)) break;
        }

        return this.available_moves;
    }
}
