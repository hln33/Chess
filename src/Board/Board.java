package Board;
import Board.Pieces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Objects;

// TO-DO: (descending importance)
// 1. clean up code

public class Board extends JPanel implements ActionListener {
    ArrayList<Tile> validMoves = new ArrayList<>();
    Tile[][] tiles = new Tile[8][8];
    Tile selectedTile;
    boolean selected = false;
    boolean whiteTurn = true;
    boolean PVP;
    AI ai;
    Chess logicManager;

    // game over detection
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener l) {
        pcs.addPropertyChangeListener(l);
    }
    public void removePropertyChangeListener(PropertyChangeListener l) {
        pcs.removePropertyChangeListener(l);
    }
    private void checkGameOver() {
        String message = logicManager.checkGameConditions();
        if (!Objects.equals(message, "")) {
            pcs.firePropertyChange("GameOver", "", message);
        }
    }

    // set up board
    public Board(boolean PVP) {
        this.PVP = PVP;

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
        this.logicManager = new Chess();
        this.ai = new AI(logicManager, this);
        addPieces();
    }
    private void addPawns() {
        Pawn newPawn;
        // black pawns
        for (int col = 0; col < 8; ++col) {
            newPawn = new Pawn(this, tiles[1][col], piece_color.black);
            logicManager.addPieceToGame(newPawn);
        }
        // white pawns
        for (int col = 0; col < 8; ++col) {
            newPawn = new Pawn(this, tiles[6][col], piece_color.white);
            logicManager.addPieceToGame(newPawn);
        }
    }
    private void addRooks() {
        Rook newRook;
        // black rooks
        newRook = new Rook(this, tiles[0][0], piece_color.black);
        logicManager.addPieceToGame(newRook);
        newRook = new Rook(this, tiles[0][7], piece_color.black);
        logicManager.addPieceToGame(newRook);

        // white rooks
        newRook = new Rook(this, tiles[7][0], piece_color.white);
        logicManager.addPieceToGame(newRook);
        newRook = new Rook(this, tiles[7][7], piece_color.white);
        logicManager.addPieceToGame(newRook);
    }
    private void addKnights() {
        Knight newKnight;
        // black knights
        newKnight = new Knight(this, tiles[0][1], piece_color.black);
        logicManager.addPieceToGame(newKnight);
        newKnight = new Knight(this, tiles[0][6], piece_color.black);
        logicManager.addPieceToGame(newKnight);

        // white knights
        newKnight = new Knight(this, tiles[7][1], piece_color.white);
        logicManager.addPieceToGame(newKnight);
        newKnight = new Knight(this, tiles[7][6], piece_color.white);
        logicManager.addPieceToGame(newKnight);
    }
    private void addBishops() {
        Bishop newBishop;
        // black bishops
        newBishop = new Bishop(this, tiles[0][2], piece_color.black);
        logicManager.addPieceToGame(newBishop);
        newBishop = new Bishop(this, tiles[0][5], piece_color.black);
        logicManager.addPieceToGame(newBishop);

        // white bishops
        newBishop = new Bishop(this, tiles[7][2], piece_color.white);
        logicManager.addPieceToGame(newBishop);
        newBishop = new Bishop(this, tiles[7][5], piece_color.white);
        logicManager.addPieceToGame(newBishop);
    }
    private void addQueens() {
        Queen newQueen;
        // black queen
        newQueen = new Queen(this, tiles[0][4], piece_color.black);
        logicManager.addPieceToGame(newQueen);

        // white queen
        newQueen = new Queen(this, tiles[7][4], piece_color.white);
        logicManager.addPieceToGame(newQueen);
    }
    private void addKings() {
        King newKing;
        // black king
        newKing = new King(this, tiles[0][3], piece_color.black);
        logicManager.addPieceToGame(newKing);

        // white king
        newKing = new King(this, tiles[7][3], piece_color.white);
        logicManager.addPieceToGame(newKing);
    }
    private void addPieces() {
        addPawns();
        addRooks();
        addKnights();
        addBishops();
        addQueens();
        addKings();
    }

    // getters and setters
    public Tile[][] getTiles() {
        return this.tiles;
    }

    // add highlighting to represent valid moves
    private void addHighlighting(Piece selectedPiece) {
        validMoves = logicManager.filterMoves(selectedPiece);

        for (Tile validMove : validMoves) {
            validMove.setBackground(Color.CYAN);
        }
    }
    private void removeHighlighting() {
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                tiles[row][col].setBackground(tiles[row][col].getColor());
            }
        }
    }

    private void ComputerMove() {
        removeHighlighting();
        ai.generateMinimax();
        checkGameOver();
        logicManager.markChecked();
        whiteTurn = !whiteTurn;
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
                logicManager.movePiece(clickedTile, selectedTile);
                logicManager.markChecked();
                checkGameOver();
                whiteTurn = !whiteTurn;
                // if AI is enabled then computer will make a move
                if (!PVP) ComputerMove();
            }

            removeHighlighting();
            selected = false;
        }
    }
}