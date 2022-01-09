package Board;

import Board.Pieces.Piece;
import Board.Pieces.piece_color;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class AI {
    Board state;
    Chess logicManager;

    public AI(Chess logicManager, Board state) {
        this.state = state;
        this.logicManager = logicManager;
        System.out.println("AI init");
    }

    private ArrayList<Piece> getBlackPieces(ArrayList<Piece> pieces) {
        ArrayList<Piece> blackPieces = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getColor() == piece_color.black) {
                blackPieces.add(piece);
            }
        }
        return blackPieces;
    }

    private ArrayList<Piece> getWhitePieces(ArrayList<Piece> pieces) {
        ArrayList<Piece> whitePieces = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece.getColor() == piece_color.white) {
                whitePieces.add(piece);
            }
        }
        return whitePieces;
    }

    private int evaluatePosition(Board position) {
        ArrayList<Piece> pieces = position.getPieces();
        ArrayList<Piece> black = getBlackPieces(pieces);
        ArrayList<Piece> white = getWhitePieces(pieces);

        return black.size() - white.size();
    }

    private ArrayList<PieceAndMove> minimaxRoot(int depth) {
        ArrayList<PieceAndMove> results = new ArrayList<>();
        ArrayList<Piece> black = getBlackPieces(state.getPieces());

        // run each valid move through minimax
        for (Piece piece : black) {
            Tile originalTile = piece.getTile();
            for (Tile move : logicManager.filterMoves(piece)) {
                Piece enemyPiece = move.getPiece();
                if (enemyPiece != null) state.getPieces().remove(enemyPiece);
                piece.setTile(move);
                originalTile.setPiece(null);
                move.setPiece(piece);

                int res = minimax(state, depth, true);
                System.out.println(res);

                originalTile.setPiece(piece);
                move.setPiece(enemyPiece);
                piece.setTile(originalTile);
                if (enemyPiece != null) state.getPieces().add(enemyPiece);

                results.add(new PieceAndMove(piece, move, res));
            }
        }

        return  results;
    }
    private int minimax(Board position, int depth, boolean maximizingPlayer) {
        if (depth == 0 || !Objects.equals(logicManager.checkGameConditions(), "")) {
            // return evaluation of position
            return evaluatePosition(position);
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            Chess boardManager = new Chess(position);

            for (Piece piece : getBlackPieces(position.getPieces())) {
                // perform move
                Tile originalTile = piece.getTile();
                ArrayList<Tile> moves = boardManager.filterMoves(piece);
                for (Tile move : moves) {
                    Piece enemyPiece = move.getPiece();
                    if (enemyPiece != null) state.getPieces().remove(enemyPiece);
                    piece.setTile(move);
                    originalTile.setPiece(null);
                    move.setPiece(piece);

                    // recursive call
                    int eval = minimax(position, depth-1, false);
                    maxEval = Math.max(maxEval, eval);

                    originalTile.setPiece(piece);
                    move.setPiece(enemyPiece);
                    piece.setTile(originalTile);
                    if (enemyPiece != null) state.getPieces().add(enemyPiece);
                }
            }
            return maxEval;
        }
        else {
            int minEval = Integer.MAX_VALUE;
            Chess boardManager = new Chess(position);

            for (Piece piece : getWhitePieces(position.getPieces())) {
                Tile originalTile = piece.getTile();
                ArrayList<Tile> moves = boardManager.filterMoves(piece);
                for (Tile move : moves) {
                    Piece enemyPiece = move.getPiece();
                    if (enemyPiece != null) state.getPieces().remove(enemyPiece);
                    piece.setTile(move);
                    originalTile.setPiece(null);
                    move.setPiece(piece);

                    // recursive call
                    int eval = minimax(position, depth-1, false);
                    minEval = Math.min(minEval, eval);

                    originalTile.setPiece(piece);
                    move.setPiece(enemyPiece);
                    piece.setTile(originalTile);
                    if (enemyPiece != null) state.getPieces().add(enemyPiece);
                }

            }
            return minEval;
        }
    }

    private static class PieceAndMove {
        Piece piece;
        Tile move;
        int val;

        PieceAndMove(Piece piece, Tile move, int val) {
            this.piece = piece;
            this.move = move;
            this.val = val;
        }
    }
    public void generateMinimax() {
        ArrayList<PieceAndMove> results = minimaxRoot(3);

        // select largest value
        PieceAndMove max = new PieceAndMove(null, null, 0);
        int value = Integer.MIN_VALUE;
        for (PieceAndMove move : results) {
            if (move.val > value) {
                value = move.val;
                max = move;
            }
        }

        // perform the move
        logicManager.movePiece(max.move, max.piece.getTile());
    }
}
