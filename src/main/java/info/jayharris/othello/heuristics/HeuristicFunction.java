package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Othello.Color;

public abstract class HeuristicFunction {

    public final Color color;

    public HeuristicFunction(Color color) {
        this.color = color;
    }

    /**
     * The heuristic function.
     *
     * @param board the board to apply the function to
     * @return the board's heuristic value. The player prefers larger values.
     */
    public abstract long apply(Board board);
}
