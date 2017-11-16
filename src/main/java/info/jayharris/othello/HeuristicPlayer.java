package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.heuristics.HeuristicFunction;

import java.util.Comparator;

public class HeuristicPlayer extends Player {

    final HeuristicFunction h;

    public HeuristicPlayer(Color color, HeuristicFunction h) {
        super(color);
        this.h = h;
    }

    @Override
    public Square getMove(Othello othello) {
        Board currentBoard = othello.getBoard();

        return getLegalMoves(currentBoard).stream()
                .filter(it -> it.isLegalMove(color))
                .max(Comparator.comparingLong(square -> {
                    Board board = Board.deepCopy(currentBoard);
                    board.setPiece(board.getSquare(square.RANK, square.FILE), color);
                    return h.apply(board);
                }))
                .orElseThrow(IllegalStateException::new);
    }
}
