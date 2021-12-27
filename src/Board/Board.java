package Board;
import Board.Pieces.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {
    ArrayList<Piece> pieceList = new ArrayList<>();
    King[] kings = new King[2];
    ArrayList<Tile> validMoves = new ArrayList<>();
    Tile[][] tiles = new Tile[8][8];
    Tile selectedTile;
    boolean selected = false;
    boolean whiteTurn = true;

    // set up board
    public Board() {
        setBounds(100, 150, 500, 500);
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                constraints.gridx = col;
                constraints.gridy = row;

                Color color = (row % 2 == col % 2) ? Color.BLACK : Color.WHITE;
                tiles[row][col] = new Tile(color, new Dimension(row, col));
                tiles[row][col].addActionListener(this);
                add(tiles[row][col], constraints);
            }
        }
        addPieces();
    }

    // getters
    public Tile[][] getTiles() {
        return tiles;
    }

    // add highlighting and listeners to valid moves
    // returns true if we are able to add highlighting, false o.w
    private void addHighlighting(Piece selectedPiece) {
        validMoves = selectedPiece.getAvailable_moves();
        if (selectedPiece instanceof King) {
            removeCheckedMoves((King) selectedPiece);
        }

        for (Tile validMove : validMoves) {
            validMove.setBackground(Color.CYAN);
        }
    }
    // remove highlighting and listeners from valid moves
    private void removeHighlighting() {
        for (Tile validMove : validMoves) {
            validMove.setBackground(validMove.getColor());
        }
        // clear valid move arrayList
        validMoves = new ArrayList<>();
    }

    // removes any moves from the valid move list that would cause a king to go checked
    private void removeCheckedMoves(King king) {
        for (Tile move : king.getAvailable_moves()) {
            int X = move.getCoordinates().width;
            int Y = move.getCoordinates().height;

            for (Piece piece : pieceList) {
                if (piece == king || piece.getColor() == king.getColor()) continue;
                if (piece.getAvailable_moves().contains(tiles[X][Y])) {
                    validMoves.remove(tiles[X][Y]);
                }
            }
        }
    }
    // check if a move caused a king to go checked
    private void checkIfChecked() {
        for (King king : kings) {
            int X = king.getCoordinates().width;
            int Y = king.getCoordinates().height;
            Tile kingTile = tiles[X][Y];

            for (Piece piece : pieceList) {
                if (piece instanceof King || piece.getColor() == king.getColor()) continue;
                if (piece.getAvailable_moves().contains(kingTile)) {
                    kingTile.setBackground(Color.RED);
                    king.setChecked(true);
                    return;
                }
            }
        }

        // if we made it here then no kings are checked
        for (King king : kings) {
            king.setChecked(false);
        }
    }
    // add pieces to board
    private void movePiece(Tile chosenTile) {
        Piece selectedPiece = selectedTile.getPiece();
        Dimension newCoordinates = chosenTile.getCoordinates();

        selectedPiece.setCoordinates(newCoordinates);
        chosenTile.setPiece(selectedPiece);
        selectedTile.removePiece();
        selectedTile.setBackground(selectedTile.getColor());
    }
    private void removePieceFromGame(Tile chosenTile) {
        if (chosenTile.getPiece() == null) return;
        Piece eliminatedPiece = chosenTile.getPiece();
        pieceList.remove(eliminatedPiece);
    }
    private void addPieceToGame(Piece newPiece) {
        int x = newPiece.getCoordinates().width;
        int y = newPiece.getCoordinates().height;

        Tile tile = tiles[x][y];
        tile.setPiece(newPiece);
        pieceList.add(newPiece);
    }
    private void addPawns() {
        Pawn newPawn;
        // black pawns
        for (int col = 0; col < 8; ++col) {
            newPawn = new Pawn(this, new Dimension(1, col), piece_color.black);
            addPieceToGame(newPawn);
        }
        // white pawns
        for (int col = 0; col < 8; ++col) {
            newPawn = new Pawn(this, new Dimension(6, col), piece_color.white);
            addPieceToGame(newPawn);
        }
    }
    private void addRooks() {
        Rook newRook;
        // black rooks
        newRook = new Rook(this, new Dimension(0, 0), piece_color.black);
        addPieceToGame(newRook);
        newRook = new Rook(this, new Dimension(0, 7), piece_color.black);
        addPieceToGame(newRook);

        // white rooks
        newRook = new Rook(this, new Dimension(7, 0), piece_color.white);
        addPieceToGame(newRook);
        newRook = new Rook(this, new Dimension(7, 7), piece_color.white);
        addPieceToGame(newRook);
    }
    private void addKnights() {
        Knight newKnight;
        // black knights
        newKnight = new Knight(this, new Dimension(0, 1), piece_color.black);
        addPieceToGame(newKnight);
        newKnight = new Knight(this, new Dimension(0, 6), piece_color.black);
        addPieceToGame(newKnight);

        // white knights
        newKnight = new Knight(this, new Dimension(7, 1), piece_color.white);
        addPieceToGame(newKnight);
        newKnight = new Knight(this, new Dimension(7, 6), piece_color.white);
        addPieceToGame(newKnight);
    }
    private void addBishops() {
        Bishop newBishop;
        // black bishops
        newBishop = new Bishop(this, new Dimension(0, 2), piece_color.black);
        addPieceToGame(newBishop);
        newBishop = new Bishop(this, new Dimension(0, 5), piece_color.black);
        addPieceToGame(newBishop);

        // white bishops
        newBishop = new Bishop(this, new Dimension(7, 2), piece_color.white);
        addPieceToGame(newBishop);
        newBishop = new Bishop(this, new Dimension(7, 5), piece_color.white);
        addPieceToGame(newBishop);
    }
    private void addQueens() {
        Queen newQueen;
        // black queen
        newQueen = new Queen(this, new Dimension(0, 4), piece_color.black);
        addPieceToGame(newQueen);

        // white queen
        newQueen = new Queen(this, new Dimension(7, 4), piece_color.white);
        addPieceToGame(newQueen);
    }
    private void addKings() {
        King newKing;
        // black king
        newKing = new King(this, new Dimension(0, 3), piece_color.black);
        kings[1] = newKing;
        addPieceToGame(newKing);

        // white king
        newKing = new King(this, new Dimension(7, 3), piece_color.white);
        kings[0] = newKing;
        addPieceToGame(newKing);
    }
    private void addPieces() {
        addPawns();
        addRooks();
        addKnights();
        addBishops();
        addQueens();
        addKings();
    }

    // handle user input
    @Override
    public void actionPerformed(ActionEvent e) {
        Tile clickedTile = (Tile)e.getSource();
        boolean checked = (kings[0].Checked() || kings[1].Checked());

        // if a piece has yet to be selected
        if (!selected && clickedTile.getPiece() != null) {
            Piece clickedPiece = clickedTile.getPiece();
            // if king is checked, then user can only select a king
            if (checked && !(clickedPiece instanceof King)) return;

            if (whiteTurn && clickedPiece.getColor() == piece_color.white) {
                addHighlighting(clickedPiece);
            }
            else if (!whiteTurn && clickedPiece.getColor() == piece_color.black) {
                addHighlighting(clickedPiece);
            }
            selectedTile = clickedTile;
            selected = validMoves.size() != 0;
        }
        // if a piece has previously been selected
        else if (selected) {
            boolean checkedState = checked && !(selectedTile.getPiece() instanceof King);
            // move piece to tile if clicked tile was a valid move
            if (validMoves.contains(clickedTile) && !(checkedState)) {
                removePieceFromGame(clickedTile);
                movePiece(clickedTile);
                checkIfChecked();
                whiteTurn = !whiteTurn;
            }
            removeHighlighting();
            selected = false;
        }
    }
}

