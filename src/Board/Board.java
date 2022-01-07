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
    ArrayList<Piece> pieceList = new ArrayList<>();
    ArrayList<Tile> validMoves = new ArrayList<>();
    King[] kings = new King[2];
    Tile[][] tiles = new Tile[8][8];
    Tile selectedTile;
    boolean selected = false;
    boolean whiteTurn = true;
    boolean PVP;
    AI ai = new AI();
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
        addPieces();
        this.logicManager = new Chess(this);
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
    private void addPieceToGame(Piece newPiece) {
        Tile tile = newPiece.getTile();
        tile.setPiece(newPiece);
        pieceList.add(newPiece);
    }

    // getters and setters
    public Tile[][] getTiles() {
        return this.tiles;
    }
    public ArrayList<Piece> getPieces() {
        return this.pieceList;
    }
    public King[] getKings() {
        return this.kings;
    }

    // add highlighting to represent valid moves
    private void addHighlighting(Piece selectedPiece) {
        validMoves = logicManager.filterMoves(selectedPiece);

        for (Tile validMove : validMoves) {
            validMove.setBackground(Color.CYAN);
        }
    }
    private void removeHighlighting() {
        for (Tile validMove : validMoves) {
            validMove.setBackground(validMove.getColor());
        }
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
                checkGameOver();
                whiteTurn = !whiteTurn;
                // if AI is enabled then computer will make a move
                if (!PVP) ComputerMove();
            }

            removeHighlighting();
            selected = false;
        }
    }

    // the AI needs to know when they are checked
    private void ComputerMove() {
        ArrayList<Piece> blackPieces = new ArrayList<>();
        for (Piece piece : pieceList) {
            if (piece.getColor() == piece_color.black) {
                blackPieces.add(piece);
            }
        }
        AI.PieceAndMove pAm = ai.getMove(blackPieces);
        Tile curr = pAm.piece.getTile();
        Tile randomMove = pAm.move;

        logicManager.movePiece(randomMove, curr);
        checkGameOver();
        whiteTurn = !whiteTurn;
    }
}