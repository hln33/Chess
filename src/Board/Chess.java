package Board;

import Board.Pieces.King;
import Board.Pieces.Pawn;
import Board.Pieces.Piece;
import Board.Pieces.piece_color;

import java.awt.*;
import java.util.ArrayList;

public class Chess {
    private final ArrayList<Piece> Piece_List;
    private final ArrayList<King> kings = new ArrayList<>();

    public Chess() {
        this.Piece_List = new ArrayList<>();
    }

    public void movePiece(Tile clickedTile, Tile previousTile ) {
        Piece selectedPiece = previousTile.getPiece();
        selectedPiece.setTile(clickedTile);

        if (clickedTile.getPiece() != null) removePieceFromGame(clickedTile);
        clickedTile.setPiece(selectedPiece);

        previousTile.removePiece();
        previousTile.setBackground(previousTile.getColor());
    }
    public void addPieceToGame(Piece newPiece) {
        Tile tile = newPiece.getTile();
        tile.setPiece(newPiece);
        Piece_List.add(newPiece);
        if (newPiece instanceof King) kings.add((King) newPiece);
    }
    public void removePieceFromGame(Tile chosenTile) {
        Piece eliminatedPiece = chosenTile.getPiece();
        chosenTile.removePiece();
        Piece_List.remove(eliminatedPiece);
    }
    public ArrayList<Piece> getPieces() {
        return this.Piece_List;
    }

    public ArrayList<Tile> filterMoves(Piece selectedPiece) {
        return detectCheck() ? blockCheckMoves(selectedPiece) : removeCheckedMoves(selectedPiece);
    }
    // removes any moves from the valid move list that would cause a king to go checked
    private ArrayList<Tile> removeCheckedMoves(Piece selectedPiece) {
        ArrayList<Tile> newMoves = selectedPiece.getAvailable_moves();

        if (selectedPiece instanceof King) {
            for (Tile move : selectedPiece.getAvailable_moves()) {
                for (Piece piece : Piece_List) {
                    if (piece == selectedPiece || piece.getColor() == selectedPiece.getColor()) continue;

                    if (piece instanceof Pawn) {
                        if (((Pawn)piece).getEliminating_moves().contains(move)) newMoves.remove(move);
                    }
                    else if (piece.getAvailable_moves().contains(move)) {
                        newMoves.remove(move);
                    }
                }
            }
            return newMoves;
        }

        Tile position = selectedPiece.getTile();
        position.removePiece();
        if (detectCheck()) newMoves.clear();
        position.setPiece(selectedPiece);
        return newMoves;
    }
    // returns list of tiles that would block a check (will only be called if a game is in checked state)
    private ArrayList<Tile> blockCheckMoves(Piece selectedPiece) {
        Tile originalTile = selectedPiece.getTile();
        ArrayList<Tile> blockingMoves = new ArrayList<>();

        // put piece on a valid tile then check if it blocked a check
        for (Tile move : selectedPiece.getAvailable_moves()) {
            selectedPiece.setTile(move);

            // check if the move tile is occupied
            Piece enemyPiece = move.getPiece();
            if (enemyPiece != null) Piece_List.remove(enemyPiece);

            // check if moving the piece blocks a check
            originalTile.setPiece(null);
            move.setPiece(selectedPiece);
            if (!detectCheck()) blockingMoves.add(move);
            originalTile.setPiece(selectedPiece);
            move.setPiece(enemyPiece);

            if (enemyPiece != null) Piece_List.add(enemyPiece);
            selectedPiece.setTile(originalTile);
        }
        return blockingMoves;
    }

    public String checkGameConditions() {
        detectCheck();
        markChecked();
        return detectCheckmate();
    }
    // detect if a king has been checked
    private boolean detectCheck() {
        for (King king : kings) {
            Tile kingTile = king.getTile();

            for (Piece piece : Piece_List) {
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
    private String detectCheckmate() {
        for (King king : kings) {
            if (king.Checked() && blockCheckMoves(king).isEmpty()) {
                boolean whiteWins = king.getColor() == piece_color.black;
                return whiteWins ? "WHITE WINS" : "BLACK WINS";
            }
        }
        return "";
    }
}
