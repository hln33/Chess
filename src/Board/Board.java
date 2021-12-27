package Board;
import Board.Pieces.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Board extends JPanel implements ActionListener {
    ArrayList<Piece> pieceList = new ArrayList<>();
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
    public ArrayList<Piece> getPieceList() {
        return this.pieceList;
    }

    // add highlighting and listeners to valid moves
    // returns true if we are able to add highlighting, false o.w
    private void addHighlighting(Tile selectedTile) {
        validMoves = selectedTile.getPiece().getAvailable_moves();

        if (selectedTile.getPiece() instanceof King) {
            removeCheckedMoves((King) selectedTile.getPiece());
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
    // add pieces to board
    private void movePiece(Tile chosenTile) {
        Piece selectedPiece = selectedTile.getPiece();
        Dimension newCoordinates = chosenTile.getCoordinates();
        selectedPiece.setCoordinates(newCoordinates);
        chosenTile.setPiece(selectedPiece);
        selectedTile.removePiece();
        System.out.println(pieceList);
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
        addPieceToGame(newKing);

        // white king
        newKing = new King(this, new Dimension(7, 3), piece_color.white);
        addPieceToGame(newKing);
    }
    private void addPieces() {
//        addPawns();
//        addRooks();
//        addKnights();
//        addBishops();
        addQueens();
        addKings();
    }

    // handle user input
    @Override
    public void actionPerformed(ActionEvent e) {
        Tile clickedTile = (Tile)e.getSource();

        // if a piece has yet to be selected
        if (!selected && clickedTile.getPiece() != null) {
            if (whiteTurn && clickedTile.getPiece().getColor() == piece_color.white) {
                selectedTile = clickedTile;
                addHighlighting(clickedTile);
            }
            if (!whiteTurn && clickedTile.getPiece().getColor() == piece_color.black) {
                selectedTile = clickedTile;
                addHighlighting(clickedTile);
            }
            selected = validMoves.size() != 0;
        }
        // if a piece has previously been selected
        else if (selected) {
            // move piece to tile if clicked tile was a valid move
            if (validMoves.contains(clickedTile)) {
                removePieceFromGame(clickedTile);
                movePiece(clickedTile);
                // check if move caused a king to go into check
                whiteTurn = !whiteTurn;
            }
            removeHighlighting();
            selected = false;
        }
    }
}

