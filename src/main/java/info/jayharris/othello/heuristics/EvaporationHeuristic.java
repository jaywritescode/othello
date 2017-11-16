package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Othello.Color;

public class EvaporationHeuristic extends HeuristicFunction {

    public EvaporationHeuristic(Color color) {
        super(color);
    }

    public long apply(Board board) {
        long count = board.count(color);
        return -(count == 0 ? Long.MAX_VALUE : count);
    }
}
