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
                    piece.setTile(move);

                    Piece enemyPiece = move.getPiece();
                    if (enemyPiece != null) state.getPieces().remove(enemyPiece);

                    originalTile.setPiece(null);
                    move.setPiece(piece);

                    // recursive call
                    int eval = minimax(position, depth-1, false);
                    maxEval = Math.max(maxEval, eval);

                    originalTile.setPiece(piece);
                    move.setPiece(enemyPiece);

                    if (enemyPiece != null) state.getPieces().add(enemyPiece);
                    piece.setTile(originalTile);
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
                    piece.setTile(move);

                    Piece enemyPiece = move.getPiece();
                    if (enemyPiece != null) state.getPieces().remove(enemyPiece);

                    originalTile.setPiece(null);
                    move.setPiece(piece);

                    // recursive call
                    int eval = minimax(position, depth-1, false);
                    minEval = Math.min(minEval, eval);

                    originalTile.setPiece(piece);
                    move.setPiece(enemyPiece);

                    if (enemyPiece != null) state.getPieces().add(enemyPiece);
                    piece.setTile(originalTile);
                }

            }
            return minEval;
        }
    }

    public void generateMove(ArrayList<Piece> pieces) {
        Random rand = new Random();
        int n = rand.nextInt(pieces.size()-1);
        Piece randomPiece = pieces.get(n);
        ArrayList<Tile> availableMoves = logicManager.filterMoves(randomPiece);

        // keep searching until we find a piece that has moves
        while (availableMoves.size() == 0) {
            n = rand.nextInt(pieces.size());
            randomPiece = pieces.get(n);
            availableMoves = logicManager.filterMoves(randomPiece);
        }

        n = rand.nextInt(availableMoves.size());
        Tile randomMove = availableMoves.get(n);
        logicManager.movePiece(randomMove, randomPiece.getTile());
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
        ArrayList<PieceAndMove> results = new ArrayList<>();

        // get black pieces
        ArrayList<Piece> black = getBlackPieces(state.getPieces());

        // run each valid move through minimax
        for (Piece piece : black) {
            for (Tile move : logicManager.filterMoves(piece)) {
                int res = minimax(state, 0, true);
                results.add(new PieceAndMove(piece, move, res));
            }
        }

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
