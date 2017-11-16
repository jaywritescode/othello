package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Othello.Color;

/**
 * This heuristic prefers moves that leave the opponent with fewer possible
 * legal responses.
 */
public class MobilityHeuristic extends HeuristicFunction {

    public MobilityHeuristic(Color color) {
        super(color);
    }

    @Override
    public long apply(Board board) {
        long count = board.getPotentialMoves().stream()
                .filter(it -> it.isLegalMove(color.opposite()))
                .count();
        return -count;
    }
}
