package Board;

import Board.Pieces.Piece;

import java.util.ArrayList;
import java.util.Random;

public class AI {
    ArrayList<Piece> pieces;
    Board state;
    Chess logicManager;

    public AI(Chess logicManager) {
        this.logicManager = logicManager;
        System.out.println("AI init");
    }

    public void generateMove(ArrayList<Piece> pieces) {
        this.pieces = pieces;
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
}
