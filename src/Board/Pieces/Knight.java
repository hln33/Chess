package Board.Pieces;
import Board.Board;
import Board.Tile;
import java.awt.*;
import java.util.ArrayList;

public class Knight extends Piece {
    public Knight(Board board, Tile location, piece_color color) {
        super(board, location, color);
        this.name = "Knight";
    }

    @Override
    public ArrayList<Tile> getAvailable_moves() {
        this.available_moves = new ArrayList<>();
        Tile[][] tiles = this.getParent_board().getTiles();
        int x = this.location.getCoordinates().width;
        int y = this.location.getCoordinates().height;
        int newX, newY;

        newX = x + 2;
        newY = y + 1;
        checkAndAddMove(newX, newY, tiles);

        newX = x + 2;
        newY = y - 1;
        checkAndAddMove(newX, newY, tiles);

        newX = x - 2;
        newY = y - 1;
        checkAndAddMove(newX, newY, tiles);

        newX = x - 2;
        newY = y + 1;
        checkAndAddMove(newX, newY, tiles);

        newX = x + 1;
        newY = y + 2;
        checkAndAddMove(newX, newY, tiles);

        newX = x - 1;
        newY = y + 2;
        checkAndAddMove(newX, newY, tiles);

        newX = x + 1;
        newY = y - 2;
        checkAndAddMove(newX, newY, tiles);

        newX = x - 1;
        newY = y - 2;
        checkAndAddMove(newX, newY, tiles);

        return this.available_moves;
    }
}
