package info.jayharris.othello;

import info.jayharris.othello.Board.Square;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class BoardUtils {

    /**
     * Returns true iff both boards have the same colors in the same squares.
     *
     * @param a a Board
     * @param b another Board
     * @return true iff both boards have the same colors in the same squares
     */
    public static boolean matches(Board a, Board b) {
        for (int file = 0; file < Board.SIZE; ++file) {
            for (int rank = 0; rank < Board.SIZE; ++rank) {
                if (a.getSquare(rank, file).getColor() != b.getSquare(rank, file).getColor()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static DirectionalIterator directionalIterator(Square start, Direction direction) {
        return new DirectionalIterator(start, direction);
    }

    enum Direction {
        NW(Square::getNW),
        N(Square::getN),
        NE(Square::getNE),
        E(Square::getE),
        SE(Square::getSE),
        S(Square::getS),
        SW(Square::getSW),
        W(Square::getW);

        private final Function<Square, Square> fn;

        Direction(Function<Square, Square> fn) {
            this.fn = fn;
        }

        Square go(Square from) {
            return fn.apply(from);
        }
    }

    /**
     * An iterator that traverses the board in a given direction and stops
     * when it passes the edge of the board.
     */
    static class DirectionalIterator implements Iterator<Square> {

        Square current;
        final Direction direction;

        DirectionalIterator(Square start, Direction direction) {
            this.current = start;
            this.direction = direction;
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(direction.go(current));
        }

        @Override
        public Square next() {
            return this.current = direction.go(current);
        }
    }
}
