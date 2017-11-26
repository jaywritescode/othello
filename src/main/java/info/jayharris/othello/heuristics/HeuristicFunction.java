package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.Comparator;

public abstract class HeuristicFunction {

    public final Color color;

    public HeuristicFunction(Color color) {
        this.color = color;
    }

    public abstract long apply(Board board);

    public Comparator<Square> comparator(Board board) {
        return Comparator.comparingLong(square -> {
            Board copy = Board.deepCopy(board);
            copy.setPiece(copy.getSquare(square), color);
            return this.apply(copy);
        });
    }
}
