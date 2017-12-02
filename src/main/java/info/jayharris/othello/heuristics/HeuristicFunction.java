package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class HeuristicFunction {

    public final Color color;
    public final Function<Comparator<Square>, Collector<Square, ?, Optional<Square>>> optimizer;

    public HeuristicFunction(Color color) {
        this(color, OptimizingReducers.MAXIMIZE_HEURISTIC_VALUE);
    }

    public HeuristicFunction(Color color, OptimizingReducers direction) {
        this.color = color;
        this.optimizer = direction.collectorFunction;
    }

    /**
     * Gets the relative quality of this board, from the perspective of {@code color}.
     *
     * @param board the board
     * @return the heuristic score
     */
    public abstract long apply(Board board);

    /**
     * Gets a comparator that compares the heuristic values of two boards and
     * orders smaller values first.
     *
     * By default, the comparator copies the board, applies the move to the copied
     * board and applies this to the copy. Subclasses can override this method for
     * optimizations.
     *
     * @param board the current board
     * @return a comparator that compares potential moves applied to the given board
     */
    public Comparator<Square> comparator(Board board) {
        return Comparator.comparingLong(square -> {
            Board copy = Board.deepCopy(board);
            copy.setPiece(copy.getSquare(square), color);
            return apply(copy);
        });
    }

    /**
     * Compare the heuristic values of all possible moves, return the "best" one.
     *
     * @return a collector that reduces to the best move
     */
    public final Collector<Square, ?, Optional<Square>> bestMove(Board board) {
        return optimizer.apply(comparator(board));
    }

    /**
     * Ordering functions —- do we want the move with the maximum heuristic
     * value or the move with the minimum heuristic value?
     */
    enum OptimizingReducers {
        MAXIMIZE_HEURISTIC_VALUE(Collectors::maxBy),
        MINIMIZE_HEURISTIC_VALUE(Collectors::minBy);

        final Function<Comparator<Square>, Collector<Square, ?, Optional<Square>>> collectorFunction;

        OptimizingReducers(Function<Comparator<Square>, Collector<Square, ?, Optional<Square>>> collectorFunction) {
            this.collectorFunction = collectorFunction;
        }
    }
}
