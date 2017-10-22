package info.jayharris.othello;

import info.jayharris.othello.Board.Square;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Function;

public class BoardUtils {

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
