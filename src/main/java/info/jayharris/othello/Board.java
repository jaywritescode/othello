package info.jayharris.othello;

import info.jayharris.othello.BoardUtils.Direction;
import info.jayharris.othello.BoardUtils.DirectionalIterator;
import info.jayharris.othello.Othello.Color;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.ZippingIterator;
import org.apache.commons.lang3.Validate;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Board {

    public static final int SIZE = 8;

    private final Square[][] squares;
    private final Set<Square> occupied;             // squares that are currently occupied
    private final Set<Square> potentialMoves;       // squares that are unoccupied and adjacent to at least one occupied square

    private Board() {
        squares = new Square[SIZE][SIZE];
        for (int rank = 0; rank < SIZE; ++rank) {
            for (int file = 0; file < SIZE; ++file) {
                squares[rank][file] = new Square(rank, file);
            }
        }

        occupied = new HashSet<>();
        potentialMoves = new HashSet<>();
    }

    private Board(Board original) {
        this();

        for (int rank = 0; rank < SIZE; ++rank) {
            for (int file = 0; file < SIZE; ++file) {
                this.getSquare(rank, file).setColor(original.getSquare(rank, file).getColor());
            }
        }

        original.getOccupied().forEach(originalSquare ->
            this.occupied.add(this.getSquare(originalSquare.RANK, originalSquare.FILE)));
        original.getPotentialMoves().forEach(originalSquare ->
            this.potentialMoves.add(this.getSquare(originalSquare.RANK, originalSquare.FILE)));
    }

    /**
     * Put a disc of the given color on the given square, if it's a legal move.
     *
     * @param square the square
     * @param color the color
     * @throws IllegalArgumentException if this is not a legal move
     */
    public void setPiece(Square square, Color color) {
        Validate.isTrue(square.isLegalMove(color));

        square.setColor(color);
        flipInAllDirections(square, color);

        occupied.add(square);
        potentialMoves.remove(square);
        potentialMoves.addAll(
                square.getNeighbors().stream()
                        .filter(Square::isUnoccupied)
                        .collect(Collectors.toSet()));
    }

    private void flipInAllDirections(Square square, Color color) {
        Consumer<Direction> flipDirection = (direction) -> flipInDirection(square, color, direction);
        EnumSet.allOf(Direction.class).forEach(flipDirection);
    }

    private void flipInDirection(Square square, Color color, Direction direction) {
        if (square.willFlipLine(color, direction)) {
            DirectionalIterator iter = BoardUtils.directionalIterator(square, direction);
            Square next = iter.next();
            do {
                next.flip();
            } while ((next = iter.next()).getColor() != color);
        }
    }

    /**
     * Returns {@code true} if there is a legal move for {@code player}.
     *
     * @param player the player
     * @return {@code true} if there is a legal move for {@code player}
     */
    public boolean hasMoveFor(Player player) {
        return hasMoveFor(player.getColor());
    }

    private boolean hasMoveFor(Color color) {
        return potentialMoves.stream().anyMatch(it -> it.isLegalMove(color));
    }

    protected Square getSquare(int rank, int file) {
        return squares[rank][file];
    }

    /**
     * Returns the square referenced by {@code file} and {@code rank}.
     *
     * @param file the file
     * @param rank the rank
     * @return the square
     */
    public Square getSquare(char file, int rank) {
        return getSquare(rank - 1, file - 'a');
    }

    /**
     * Returns a set of occupied squares.
     *
     * @return an unmodifiable view of the set of occupied squares
     */
    public Set<Square> getOccupied() {
        return Collections.unmodifiableSet(occupied);
    }

    /**
     * Returns a set of unoccupied squares adjacent to at least one occupied square.
     *
     * @return an unmodifiable view of the set of potential moves
     */
    public Set<Square> getPotentialMoves() {
        return Collections.unmodifiableSet(potentialMoves);
    }

    /**
     * Creates the board for a new game.
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

        board.potentialMoves.addAll(
                board.occupied.stream()
                        .flatMap(square -> square.getNeighbors().stream())
                        .filter(Square::isUnoccupied)
                        .collect(Collectors.toSet()));

        return board;
    }

    /**
     * Creates a deep copy of the original board.
     *
     * @param original the original board
     * @return a copy of the board that is completely independent from the original
     */
    public static Board deepCopy(Board original) {
        return new Board(original);
    }

    public long count(Color color) {
        return occupied.stream().filter(square -> square.getColor() == color).count();
    }

    /**
     * Returns a pretty text-string version of the board.
     *
     * @return a representation of the board
     */
    public String pretty() {
        final StringBuilder sb = new StringBuilder();
        final Iterator<Square> squares = iterator();

        sb.append("\n  abcdefgh");
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

        final int RANK, FILE;
        Color color;

        Square nw, n, ne, e, se, s, sw, w;
        Set<Square> neighbors;

        /**
         * Constructor.
         *
         * @param rank the square's rank. The top row of the board is rank 0.
         * @param file the square's file. The left column of the board is file 0.
         */
        private Square(int rank, int file) {
            this.RANK = rank;
            this.FILE = file;
            this.color = null;
        }

        void setColor(Color color) {
            this.color = color;
        }

        /**
         * Returns the square's color.
         *
         * @return the square's color
         */
        Color getColor() {
            return color;
        }

        void flip() {
            setColor(color.opposite());
        }

        /**
         * Returns {@code true} if this square is occupied.
         *
         * @return {@code true} iff this square is occupied
         */
        boolean isUnoccupied() {
            return Objects.isNull(color);
        }

        /**
         * Returns this square's neighboring squares.
         *
         * @return a set of neighboring squares
         */
        Set<Square> getNeighbors() {
            if (neighbors == null) {
                neighbors = EnumSet.allOf(Direction.class).stream()
                                    .map(direction -> direction.go(this))
                                    .filter(Objects::nonNull)
                                    .collect(Collectors.toSet());
            }
            return neighbors;
        }

        /**
         * Returns {@code true} if it's legal to play a {@code color} disc on this square.
         *
         * @param color the color
         * @return {@code true} iff this square is a legal play for {@code color}
         */
        boolean isLegalMove(Color color) {
            return isUnoccupied() && EnumSet.allOf(Direction.class).stream()
                                            .anyMatch(direction -> willFlipLine(color, direction));

        }

        /**
         * Returns {@code true} if {@code color} playing on this square will flips discs in {@code direction}
         *
         * @param color
         * @param direction
         * @return true iff {@code color} playing on this square will flip {@code color.opposite()}
         *  discs in the given direction
         */
        boolean willFlipLine(Color color, Direction direction) {
            Square neighbor = direction.go(this);
            if (neighbor == null || neighbor.isUnoccupied() || neighbor.getColor() == color) {
                return false;
            }

            DirectionalIterator iter = BoardUtils.directionalIterator(neighbor, direction);
            while (iter.hasNext()) {
                neighbor = iter.next();
                if (neighbor.isUnoccupied()) {
                    return false;
                }
                if (neighbor.getColor() == color) {
                    return true;
                }
            }
            return false;
        }

        Square getNW() {
            if (nw == null) {
                nw = (RANK - 1 >= 0 && FILE - 1 >= 0 ? squares[RANK - 1][FILE - 1] : null);
            }
            return nw;
        }

        Square getN() {
            if (n == null) {
                n = (RANK - 1 >= 0 ? squares[RANK - 1][FILE] : null);
            }
            return n;
        }

        Square getNE() {
            if (ne == null) {
                ne = (RANK - 1 >= 0 && FILE + 1 < SIZE ? squares[RANK - 1][FILE + 1] : null);
            }
            return ne;
        }

        Square getE() {
            if (e == null) {
                e = (FILE + 1 < SIZE ? squares[RANK][FILE + 1] : null);
            }
            return e;
        }

        Square getSE() {
            if (se == null) {
                se = (RANK + 1 < SIZE && FILE + 1 < SIZE ? squares[RANK + 1][FILE + 1] : null);
            }
            return se;
        }

        Square getS() {
            if (s == null) {
                s = (RANK + 1 < SIZE ? squares[RANK + 1][FILE] : null);
            }
            return s;
        }

        Square getSW() {
            if (sw == null) {
                sw = (RANK + 1 < SIZE && FILE - 1 >= 0 ? squares[RANK + 1][FILE - 1] : null);
            }
            return sw;
        }

        Square getW() {
            if (w == null) {
                w = (FILE - 1 >= 0 ? squares[RANK][FILE - 1] : null);
            }
            return w;
        }

        /**
         * Returns a pretty text-version of this square.
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
         * Returns the algebraic notation representation of this square.
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
}
