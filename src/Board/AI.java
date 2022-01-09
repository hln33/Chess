package Board;

import Board.Pieces.*;

import java.util.ArrayList;
import java.util.Objects;

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

    private int evaluatePosition() {
        ArrayList<Piece> pieces = logicManager.getPieces();
        int value = 0;
        for (Piece piece : pieces) {
            value += getPieceValue(piece);
        }
        System.out.println(logicManager.getPieces().size());
        return value;
    }
    private int getPieceValue(Piece piece) {
        int val = 0;

        if (piece instanceof Pawn) {
            val = 10;
        }
        else if (piece instanceof Rook) {
            val = 50;
        }
        else if (piece instanceof Knight) {
            val = 30;
        }
        else if (piece instanceof Bishop) {
            val = 30;
        }
        else if (piece instanceof Queen) {
            val = 90;
        }
        else if (piece instanceof King) {
            val = 900;
        }

        return piece.getColor() == piece_color.black ? val : -val;
    }

    private ArrayList<PieceAndMove> minimaxRoot(int depth) {
        ArrayList<PieceAndMove> results = new ArrayList<>();
        ArrayList<Piece> black = getBlackPieces(logicManager.getPieces());

        // run each valid move through minimax
        for (Piece piece : black) {
            Tile originalTile = piece.getTile();
            for (Tile move : logicManager.filterMoves(piece)) {
                Piece enemyPiece = move.getPiece();
                performMove(originalTile, move, piece, enemyPiece);
                int res = minimax(depth, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                //System.out.println(res);
                undoMove(originalTile, move, piece, enemyPiece);
                results.add(new PieceAndMove(piece, move, res));
            }
        }
        return  results;
    }
    private int minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || !Objects.equals(logicManager.checkGameConditions(), "")) {
            // return evaluation of position
            return evaluatePosition();
        }

        ArrayList<Piece> pieceList = logicManager.getPieces();
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            outer:
            for (Piece piece : getBlackPieces(pieceList)) {
                // perform move
                Tile originalTile = piece.getTile();
                ArrayList<Tile> moves = logicManager.filterMoves(piece);
                for (Tile move : moves) {
                    Piece enemyPiece = move.getPiece();
                    performMove(originalTile, move, piece, enemyPiece);
                    int eval = minimax(depth-1, alpha, beta, false);
                    undoMove(originalTile, move, piece, enemyPiece);

                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, maxEval);
                    if (beta <= alpha) break outer;
                }
            }
            return maxEval;
        }
        else {
            int minEval = Integer.MAX_VALUE;
            outer:
            for (Piece piece : getWhitePieces(pieceList)) {
                Tile originalTile = piece.getTile();
                ArrayList<Tile> moves = logicManager.filterMoves(piece);
                for (Tile move : moves) {
                    Piece enemyPiece = move.getPiece();
                    performMove(originalTile, move, piece, enemyPiece);
                    int eval = minimax(depth-1, alpha, beta, false);
                    undoMove(originalTile, move, piece, enemyPiece);

                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, minEval);
                    if (beta <= alpha) break outer;
                }

            }
            return minEval;
        }
    }
    private void performMove(Tile original, Tile move, Piece moved, Piece enemy) {
        if (enemy != null) logicManager.getPieces().remove(enemy);
        original.setPiece(null);
        moved.setTile(move);
        move.setPiece(moved);
    }
    private void undoMove(Tile original, Tile move, Piece moved, Piece enemy) {
        original.setPiece(moved);
        moved.setTile(original);
        move.setPiece(enemy);
        if (enemy != null) logicManager.getPieces().add(enemy);
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
