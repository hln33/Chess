package Board.AI;

import Board.Board;
import Board.Pieces.Piece;
import Board.Tile;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    ArrayList<Piece> pieces;
    Board state;

    public AI() {
        System.out.println("AI init");
    }

    public class PieceAndMove {
        public Piece piece;
        public Tile move;

        PieceAndMove(Piece piece, Tile move) {
            this.piece = piece;
            this.move = move;
        }
    }

    public PieceAndMove getMove(ArrayList<Piece> pieces) {
        this.pieces = pieces;
        Random rand = new Random();
        int n = rand.nextInt(pieces.size()-1);
        Piece randomPiece = pieces.get(n);
        ArrayList<Tile> availableMoves = randomPiece.getAvailable_moves();

        // keep searching until we find a piece that has moves
        while (availableMoves.size() == 0) {
            n = rand.nextInt(pieces.size()-1);
            randomPiece = pieces.get(n);
            availableMoves = randomPiece.getAvailable_moves();
        }

        n = rand.nextInt(availableMoves.size());
        Tile randomMove = availableMoves.get(n);
        return new PieceAndMove(randomPiece, randomMove);
    }
}
