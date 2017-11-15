package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.Comparator;

/**
 * A player that chooses the move the gives the opponent the fewest legal moves.
 */
public class MobilityPlayer extends Player {
    public MobilityPlayer(Color color) {
        super(color);
    }

    @Override
    public Square getMove(Othello othello) {
        final Board currentBoard = othello.getBoard();

        return getLegalMoves(currentBoard).stream()
                .filter(it -> it.isLegalMove(color))
                .min(Comparator.comparingLong(it -> {
                    Board board = Board.deepCopy(currentBoard);
                    board.setPiece(board.getSquare(it.RANK, it.FILE), color);
                    return countOpponentLegalMoves(board);
                }))
                .orElseThrow(IllegalStateException::new);
    }

    private long countOpponentLegalMoves(Board board) {
        return board.getPotentialMoves().stream()
                .filter(it -> it.isLegalMove(color.opposite()))
                .count();
    }
}
