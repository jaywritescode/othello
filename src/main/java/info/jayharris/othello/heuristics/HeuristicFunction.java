package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Othello.Color;

import java.util.function.ToLongFunction;

public abstract class HeuristicFunction {

    public final Color color;

    public HeuristicFunction(Color color) {
        this.color = color;
    }

    public abstract long apply(Board board);
}
