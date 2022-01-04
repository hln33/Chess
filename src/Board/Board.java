package Board;
import Board.Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// TO-DO: (descending importance)
// 1. clean up code
// 2. add checkmate detectionN

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

                Color color = (row % 2 == col % 2) ? Color.darkGray : Color.WHITE;
                tiles[row][col] = new Tile(color, new Dimension(row, col));
                tiles[row][col].addActionListener(this);
                add(tiles[row][col], constraints);
            }
        }
        addPieces();
    }
    private void addPawns() {
        Pawn newPawn;
        // black pawns
        for (int col = 0; col < 8; ++col) {
            newPawn = new Pawn(this, tiles[1][col], piece_color.black);
            addPieceToGame(newPawn);
        }
        // white pawns
        for (int col = 0; col < 8; ++col) {
            newPawn = new Pawn(this, tiles[6][col], piece_color.white);
            addPieceToGame(newPawn);
        }
    }
    private void addRooks() {
        Rook newRook;
        // black rooks
        newRook = new Rook(this, tiles[0][0], piece_color.black);
        addPieceToGame(newRook);
        newRook = new Rook(this, tiles[0][7], piece_color.black);
        addPieceToGame(newRook);

        // white rooks
        newRook = new Rook(this, tiles[7][0], piece_color.white);
        addPieceToGame(newRook);
        newRook = new Rook(this, tiles[7][7], piece_color.white);
        addPieceToGame(newRook);
    }
    private void addKnights() {
        Knight newKnight;
        // black knights
        newKnight = new Knight(this, tiles[0][1], piece_color.black);
        addPieceToGame(newKnight);
        newKnight = new Knight(this, tiles[0][6], piece_color.black);
        addPieceToGame(newKnight);

        // white knights
        newKnight = new Knight(this, tiles[7][1], piece_color.white);
        addPieceToGame(newKnight);
        newKnight = new Knight(this, tiles[7][6], piece_color.white);
        addPieceToGame(newKnight);
    }
    private void addBishops() {
        Bishop newBishop;
        // black bishops
        newBishop = new Bishop(this, tiles[0][2], piece_color.black);
        addPieceToGame(newBishop);
        newBishop = new Bishop(this, tiles[0][5], piece_color.black);
        addPieceToGame(newBishop);

        // white bishops
        newBishop = new Bishop(this, tiles[7][2], piece_color.white);
        addPieceToGame(newBishop);
        newBishop = new Bishop(this, tiles[7][5], piece_color.white);
        addPieceToGame(newBishop);
    }
    private void addQueens() {
        Queen newQueen;
        // black queen
        newQueen = new Queen(this, tiles[0][4], piece_color.black);
        addPieceToGame(newQueen);

        // white queen
        newQueen = new Queen(this, tiles[7][4], piece_color.white);
        addPieceToGame(newQueen);
    }
    private void addKings() {
        King newKing;
        // black king
        newKing = new King(this, tiles[0][3], piece_color.black);
        kings[1] = newKing;
        addPieceToGame(newKing);

        // white king
        newKing = new King(this, tiles[7][3], piece_color.white);
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

    // getters
    public Tile[][] getTiles() {
        return tiles;
    }

    // add highlighting to represent valid moves
    private void addHighlighting(Piece selectedPiece) {
        validMoves = selectedPiece.getAvailable_moves();

        removeCheckedMoves(selectedPiece);
        if (detectCheck()) {
            // remove all valid moves except those that would block a check
            validMoves = blockCheckMoves(selectedPiece);
        }

        for (Tile validMove : validMoves) {
            validMove.setBackground(Color.CYAN);
        }
    }
    private void removeHighlighting() {
        for (Tile validMove : validMoves) {
            validMove.setBackground(validMove.getColor());
        }
    }

    // detect if a king has been checked
    private boolean detectCheck() {
        for (King king : kings) {
            Tile kingTile = king.getTile();

            for (Piece piece : pieceList) {
                if (piece instanceof King || piece.getColor() == king.getColor()) continue;
                if (piece.getAvailable_moves().contains(kingTile)) {
                    king.setChecked(true);
                    return true;
                }
            }
        }

        // if we made it here then no kings are checked
        for (King king : kings) {
            king.setChecked(false);
        }
        return false;
    }
    // if a king is checked then color its tile RED, O.W original color
    private void markChecked() {
        for (King king : kings) {
            Tile kingTile = king.getTile();
            Color highlighting = king.Checked() ? Color.RED : kingTile.getColor();

            kingTile.setBackground(highlighting);
        }
    }
    private void detectCheckmate() {
        for (King king : kings) {
            if (king.Checked() && king.getAvailable_moves().isEmpty()) {
                //CHECKMATE
            }
        }
    }
    // removes any moves from the valid move list that would cause a king to go checked
    private void removeCheckedMoves(Piece selectedPiece) {
        if (selectedPiece instanceof King) {
            for (Tile move : selectedPiece.getAvailable_moves()) {
                for (Piece piece : pieceList) {
                    if (piece == selectedPiece || piece.getColor() == selectedPiece.getColor()) continue;
                    if (piece.getAvailable_moves().contains(move)) {
                        validMoves.remove(move);
                    }
                }
            }
            return;
        }

        Tile position = selectedPiece.getTile();
        position.removePiece();
        if (detectCheck()) validMoves.clear();
        position.setPiece(selectedPiece);
    }
    // returns list of tiles that would block a check (will only be called if a game is in checked state)
    private ArrayList<Tile> blockCheckMoves(Piece selectedPiece) {
        Tile originalTile = selectedPiece.getTile();
        ArrayList<Tile> blockingMoves = new ArrayList<>();
        boolean isKing = selectedPiece instanceof King;

        // put piece on a valid tile then check if it blocked a check
        for (Tile move : selectedPiece.getAvailable_moves()) {
            if (isKing) selectedPiece.setTile(move);

            // check if the move tile is occupied
            Piece enemyPiece = move.getPiece() == null ? null : move.getPiece();
            if (enemyPiece != null) pieceList.remove(enemyPiece);

            // check if moving the piece blocks a check
            originalTile.setPiece(null);
            move.setPiece(selectedPiece);
            if (!detectCheck()) blockingMoves.add(move);
            originalTile.setPiece(selectedPiece);
            move.setPiece(enemyPiece);

            if (enemyPiece != null) pieceList.add(enemyPiece);
            if (isKing) selectedPiece.setTile(originalTile);
        }
        return blockingMoves;
    }

    private void movePiece(Tile chosenTile) {
        Piece selectedPiece = selectedTile.getPiece();
        selectedPiece.setTile(chosenTile);

        if (chosenTile.getPiece() != null) removePieceFromGame(chosenTile);
        chosenTile.setPiece(selectedPiece);

        selectedTile.removePiece();
        selectedTile.setBackground(selectedTile.getColor());
    }
    private void removePieceFromGame(Tile chosenTile) {
        Piece eliminatedPiece = chosenTile.getPiece();
        pieceList.remove(eliminatedPiece);
    }
    private void addPieceToGame(Piece newPiece) {
        Tile tile = newPiece.getTile();
        tile.setPiece(newPiece);
        pieceList.add(newPiece);
    }

    // handle user input
    @Override
    public void actionPerformed(ActionEvent e) {
        Tile clickedTile = (Tile)e.getSource();
        Piece clickedPiece = clickedTile.getPiece();

        // if a piece has yet to be selected
        if (!selected && clickedPiece != null) {
            validMoves.clear();
            boolean whitePiece = clickedPiece.getColor() == piece_color.white;
            if ((whiteTurn && whitePiece) || (!whiteTurn && !whitePiece)) {
                selectedTile = clickedTile;
                addHighlighting(clickedPiece);
            }
            selected = validMoves.size() != 0;
        }
        // if a piece has previously been selected
        else if (selected) {
            // move piece to tile if clicked tile was a valid move
            if (validMoves.contains(clickedTile)) {
                movePiece(clickedTile);
                detectCheck();
                markChecked();
                detectCheckmate();
                whiteTurn = !whiteTurn;
            }
            removeHighlighting();
            selected = false;
        }
    }
}

