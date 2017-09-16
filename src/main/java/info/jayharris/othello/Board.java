package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.ZippingIterator;

import java.util.*;
import java.util.function.Function;

public class Board {

    public static final int SIZE = 8;

    private final Square[][] squares;
    private final Set<Square> occupied;

    private Board() {
        squares = new Square[SIZE][SIZE];
        for (int rank = 0; rank < SIZE; ++rank) {
            for (int file = 0; file < SIZE; ++file) {
                squares[rank][file] = new Square(rank, file);
            }
        }

        occupied = new HashSet<>();
    }

    /**
     * Put a disc of the given color on the given square, if it's a legal move.
     *
     * @param square the square
     * @param color the color
     * @return true iff the disc was successfully put on the square
     */
    public boolean setPiece(Square square, Color color) {
        if (square.isOccupied()) {
            return false;
        }

        if (flipInAllDirections(square, color)) {
            square.setColor(color);
            return true;
        }
        return false;
    }

    private boolean flipInAllDirections(Square square, Color color) {
        return EnumSet.allOf(Direction.class).stream()
                       .map(dir -> flipInDirection(square, color, dir))
                       .reduce(false, Boolean::logicalOr);
    }

    private boolean flipInDirection(Square square, Color color, Direction direction) {
        boolean success = false;
        DirectionalIterator iter = DirectionalIterator.INSTANCE.reset(square, direction);

        while (iter.hasNext() && (square = iter.next()).color == color.opposite()) {
            square.flip();
            success = true;
        }

        return success;
    }

    protected Square getSquare(int rank, int file) {
        return squares[rank][file];
    }

    /**
     * Get the square given by the file and rank in algebraic notation
     *
     * @param file the file
     * @param rank the rank
     * @return the square
     */
    public Square getSquare(char file, int rank) {
        return getSquare(rank - 1, file - 'a');
    }

    /**
     * Get the square given its algebraic notation
     *
     * @param s the algebraic notation
     * @return the square
     */
    public Square getSquare(String s) {
        char file = s.charAt(0);
        int rank = s.charAt(1) - '1';

        return getSquare(file, rank);
    }

    /**
     * Create the board for a new game.
     *
     * @return the board
     */
    public static Board init() {
        Board board = new Board();

        int n = SIZE / 2 - 1;
        int w = SIZE / 2 - 1;
        int s = SIZE / 2;
        int e = SIZE / 2;

        board.squares[n][w].setColor(Color.WHITE);
        board.squares[n][e].setColor(Color.BLACK);
        board.squares[s][w].setColor(Color.BLACK);
        board.squares[s][e].setColor(Color.WHITE);

        board.occupied.addAll(Arrays.asList(
                board.squares[n][w], board.squares[n][e], board.squares[s][w], board.squares[s][e]));

        return board;
    }

    /**
     * Get a pretty text-string version of the board.
     *
     * @return a representation of the board
     */
    public String pretty() {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Square> squares = iterator();

        sb.append("\n  abcdefgh\n");
        squares.forEachRemaining(square -> {
            if (square.FILE == 0) {
                sb.append("\n").append(square.RANK + 1).append(" ");
            }
            sb.append(square.pretty());
        });

        return sb.toString();
    }

    protected Iterator<Square> iterator() {
        return new Iterator<Square>() {

            int rank = 0;
            int file = 0;

            @Override
            public boolean hasNext() {
                return rank < SIZE;
            }

            @Override
            public Square next() {
                Square square = getSquare(rank, file);

                ++file;
                if (file == SIZE) {
                    file = 0;
                    ++rank;
                }

                return square;
            }
        };
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Board{");
        sb.append("occupied=").append(occupied);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;

        ZippingIterator<Square> iter = IteratorUtils.zippingIterator(
                iterator(), board.iterator());

        while (iter.hasNext()) {
            if (iter.next().getColor() != iter.next().getColor()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        Iterator<Square> iter = iterator();
        int result = 1;

        Color color;
        while (iter.hasNext()) {
            color = iter.next().getColor();
            result = 31 * result + (color == null ? 0 : Objects.hashCode(color));
        }
        return result;
    }

    class Square {

        public final int RANK, FILE;
        private Color color;

        private Square nw, n, ne, e, se, s, sw, w;

        /**
         * Constructor.
         *
         * @param rank the square's rank. The top row of the board is rank 0.
         * @param file the square's file. The left column of the board is file 0.
         */
        public Square(int rank, int file) {
            this.RANK = rank;
            this.FILE = file;
            this.color = null;
        }

        private void setColor(Color color) {
            this.color = color;
        }

        /**
         * Get the square's color.
         *
         * @return the square's color
         */
        public Color getColor() {
            return color;
        }

        private void flip() {
            setColor(color.opposite());
        }

        /**
         * Get whether this square is currently occupied.
         *
         * @return true iff this square is occupied
         */
        public boolean isOccupied() {
            return Objects.nonNull(color);
        }

        public Square getNW() {
            if (nw == null) {
                nw = (RANK - 1 >= 0 && FILE - 1 >= 0 ? squares[RANK - 1][FILE - 1] : null);
            }
            return nw;
        }

        public Square getN() {
            if (n == null) {
                n = (RANK - 1 >= 0 ? squares[RANK - 1][FILE] : null);
            }
            return n;
        }

        public Square getNE() {
            if (ne == null) {
                ne = (RANK - 1 >= 0 && FILE + 1 < SIZE ? squares[RANK - 1][FILE + 1] : null);
            }
            return ne;
        }

        public Square getE() {
            if (e == null) {
                e = (FILE + 1 < SIZE ? squares[RANK][FILE + 1] : null);
            }
            return e;
        }

        public Square getSE() {
            if (se == null) {
                se = (RANK + 1 < SIZE && FILE + 1 < SIZE ? squares[RANK + 1][FILE + 1] : null);
            }
            return se;
        }

        public Square getS() {
            if (s == null) {
                s = (RANK + 1 < SIZE ? squares[RANK + 1][FILE] : null);
            }
            return s;
        }

        public Square getSW() {
            if (sw == null) {
                sw = (RANK + 1 < SIZE && FILE - 1 >= 0 ? squares[RANK + 1][FILE - 1] : null);
            }
            return sw;
        }

        public Square getW() {
            if (w == null) {
                w = (FILE - 1 >= 0 ? squares[RANK][FILE - 1] : null);
            }
            return w;
        }

        /**
         * Get a pretty text-version of this square.
         *
         * @return a representation of this square
         */
        public String pretty() {
            if (color == Color.WHITE) {
                return "\u25cb";
            }
            else if (color == Color.BLACK) {
                return "\u25cf";
            }
            else {
                return " ";
            }
        }

        /**
         * Get the algebraic notation representation of this square.
         *
         * @return the square's algebraic notation
         */
        public String algebraicNotation() {
            return String.format("%s%s", 'a' + FILE, '1' + RANK);
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("Square{");
            sb.append("RANK=").append(RANK);
            sb.append(", FILE=").append(FILE);
            sb.append(", color=").append(color);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Square square = (Square) o;
            return RANK == square.RANK &&
                           FILE == square.FILE;
        }

        @Override
        public int hashCode() {
            return Objects.hash(RANK, FILE);
        }
    }

    /**
     * An iterator that traverses the board in a given direction and stops
     * when it passes the edge of the board.
     */
    private enum DirectionalIterator implements Iterator<Square> {
        INSTANCE;

        Square current;
        Direction direction;

        DirectionalIterator reset(Square current, Direction direction) {
            this.current = current;
            this.direction = direction;
            return INSTANCE;
        }

        @Override
        public boolean hasNext() {
            return Objects.nonNull(direction.go(current));
        }

        @Override
        public Square next() {
            return direction.go(current);
        }
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

    public static void main(String... args) {
        Board board = Board.init();

        System.out.println(board.pretty());

        board.setPiece(board.getSquare("c4"), Color.BLACK);

        System.out.println(board.pretty());

        board.setPiece(board.getSquare("e3"), Color.WHITE);

        System.out.println(board.pretty());
    }
}
