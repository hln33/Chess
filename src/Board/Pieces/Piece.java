package Board.Pieces;
import Board.Board;
import Board.Tile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.util.ArrayList;

public abstract class Piece {
    protected final Board parent_board;
    protected Tile location;
    protected ArrayList<Tile> available_moves;
    protected Image image;
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

    Piece(Board board, Tile location, piece_color color) {
        this.parent_board = board;
        this.location = location;
        this.color = color;
    }

    public void setImage(String piece) {
        try {
            String name = this.color == piece_color.white ? "white_" + piece : "black_" + piece;
            Image img = ImageIO.read(getClass().getResource("../assets/" + name + ".png"));
            this.image = img;
        }
        catch (Exception e) {
            this.image = null;
            System.out.println(e);
        }
    }
    public Image getImage() {
        return this.image;
    }

    public Board getParent_board() {
        return this.parent_board;
    }
    public piece_color getColor() {
        return this.color;
    }

    public void setLocation(Tile newLocation) {
        this.location = newLocation;
    }
    public Tile getLocation() {
        return this.location;
    }

    public abstract ArrayList<Tile> getAvailable_moves();
}
