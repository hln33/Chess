package Board.Pieces;
import Board.Board;
import Board.Tile;

import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(Board board, Tile location, piece_color color) {
        super(board, location, color);
        setImage("bishop");
    }

    @Override
    public ArrayList<Tile> getAvailable_moves() {
        this.available_moves = new ArrayList<>();
        Tile[][] tiles = this.getParent_board().getTiles();
        int x = this.tile.getCoordinates().width;
        int y = this.tile.getCoordinates().height;
        int newX, newY;

        // down-right
        for (int i = 1; i < 8; ++i) {
            newX = x + i;
            newY = y + i;
            if (!checkAndAddMove(newX, newY, tiles)) break;
        }
        // down-left
        for (int i = 1; i < 8; ++i) {
            newX = x + i;
            newY = y - i;
            if (!checkAndAddMove(newX, newY, tiles)) break;
        }
        // up-right
        for (int i = 1; i < 8; ++i) {
            newX = x - i;
            newY = y + i;
            if (!checkAndAddMove(newX, newY, tiles)) break;
        }
        // up-left
        for (int i = 1; i < 8; ++i) {
            newX = x - i;
            newY = y - i;
            if (!checkAndAddMove(newX, newY, tiles)) break;
        }

        return this.available_moves;
    }
}
