package Board;
import Board.Pieces.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

// TO-DO: (descending importantance)
// 1. clean up code
// 2. fix bugs (especially blockCheckMoves function)
// 3. prevent a piece (ANY KIND) from moving if it would cause their king to become checked

public class Board extends JPanel implements ActionListener {
    ArrayList<Piece> pieceList = new ArrayList<>();
    King[] kings = new King[2];
    ArrayList<Tile> validMoves = new ArrayList<>();
    Tile[][] tiles = new Tile[8][8];
    Tile selectedTile;
    boolean selected = false;
    boolean checked;
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
        if (checked) {
            // remove all valid moves except those that would block a check
            validMoves = BlockCheckMoves(selectedPiece);
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
    }

    // check if a move caused a king to go checked
    private boolean checked() {
        for (King king : kings) {
            Tile kingTile = king.getLocation();

            for (Piece piece : pieceList) {
                if (piece instanceof King || piece.getColor() == king.getColor()) continue;
                if (piece.getAvailable_moves().contains(kingTile)) {
                    kingTile.setBackground(Color.RED);
                    king.setChecked(true);
                    return true;
                }
            }
        }

        // if we made it here then no kings are checked
        for (King king : kings) {
            Tile kingTile = king.getLocation();

            kingTile.setBackground(kingTile.getColor());
            king.setChecked(false);
        }
        return false;
    }
    // removes any moves from the valid move list that would cause a king to go checked
    private void removeCheckedMoves(King king) {
        for (Tile move : king.getAvailable_moves()) {
            for (Piece piece : pieceList) {
                if (piece == king || piece.getColor() == king.getColor()) continue;
                if (piece.getAvailable_moves().contains(move)) {
                    validMoves.remove(move);
                }
            }
        }
    }
    // returns list of tiles that would block a check (will only be called if a game is in checked state)
    private ArrayList<Tile> BlockCheckMoves(Piece selectedPiece) {
        Tile originalTile = selectedPiece.getLocation();
        ArrayList<Tile> blockingMoves = new ArrayList<>();
        boolean isKing = selectedPiece instanceof King;

        // put piece on a valid tile then check if it blocked a check
        for (Tile move : selectedPiece.getAvailable_moves()) {
            if (isKing) selectedPiece.setLocation(move);

            // account for if there is already a piece on the move tile
            Piece enemyPiece = move.getPiece() == null ? null : move.getPiece();
            if (enemyPiece != null) pieceList.remove(enemyPiece);

            // move piece
            originalTile.removePiece();
            move.setPiece(selectedPiece);
            // check if checked
            if (!checked()) {
                blockingMoves.add(move);
            }
            // move piece back to original spot
            originalTile.setPiece(selectedPiece);
            move.removePiece();

            if (enemyPiece != null) pieceList.add(enemyPiece);

            if (isKing) {
                move.setBackground(move.getColor());
                selectedPiece.setLocation(originalTile);
            }
        }

        return blockingMoves;
    }

    private void movePiece(Tile chosenTile) {
        Piece selectedPiece = selectedTile.getPiece();

        selectedPiece.setLocation(chosenTile);
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
        Tile tile = newPiece.getLocation();
        tile.setPiece(newPiece);
        pieceList.add(newPiece);
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

    // handle user input
    @Override
    public void actionPerformed(ActionEvent e) {
        Tile clickedTile = (Tile)e.getSource();
        Piece clickedPiece = clickedTile.getPiece();

        // if a piece has yet to be selected
        if (!selected && clickedPiece != null) {
            validMoves = new ArrayList<>();

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
            // move piece to tile if clicked tile was a valid move
            if (validMoves.contains(clickedTile)) {
                removePieceFromGame(clickedTile);
                movePiece(clickedTile);
                checked = checked();
                whiteTurn = !whiteTurn;
            }
            removeHighlighting();
            selected = false;
        }
    }
}

