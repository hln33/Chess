package Board;
import Board.Pieces.Piece;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class Tile extends JButton {
    Piece piece = null;
    Color color;
    Dimension coordinates;

    Tile(Color color, Dimension coordinates) {
        this.color = color;
        this.coordinates = coordinates;
        setBorder(new LineBorder(Color.BLACK, 1));
        setOpaque(true);
        setBackground(color);
        setForeground(Color.ORANGE);
        setPreferredSize(new Dimension(70, 70));
    }

    public Color getColor() {
        return this.color;
    }

    public Dimension getCoordinates() {
        return this.coordinates;
    }

    protected void setPiece(Piece piece) {
        String name = piece != null ? piece.getName() : "";

        setText(name);
        this.piece = piece;
    }
    protected void removePiece() {
        setText("");
        this.piece = null;
    }
    public Piece getPiece() {
        return this.piece;
    }
}
