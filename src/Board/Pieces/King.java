package Board.Pieces;
import Board.Board;
import Board.Tile;
import java.awt.*;
import java.util.ArrayList;

public class King extends Piece {
    boolean checked = false;
    public King(Board board, Tile location, piece_color color) {
        super(board, location, color);
        this.name = "King";
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    public boolean Checked() {
        return this.checked;
    }

    @Override
    public ArrayList<Tile> getAvailable_moves() {
        this.available_moves = new ArrayList<>();
        Tile[][] tiles = this.getParent_board().getTiles();
        int x = this.location.getCoordinates().width;
        int y = this.location.getCoordinates().height;
        int newX, newY;

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
