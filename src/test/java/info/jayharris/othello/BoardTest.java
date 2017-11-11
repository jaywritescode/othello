package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static info.jayharris.othello.BoardAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class BoardTest {

    @Test
    @DisplayName("initializes the board")
    void testInit() throws Exception {
        Board board = Board.init();

        assertThat(board).matches(BoardFactory.instance().newGame());
        Assertions.assertThat(board.getOccupied()).containsOnly(
                board.getSquare('d', 4),
                board.getSquare('d', 5),
                board.getSquare('e', 4),
                board.getSquare('e', 5)
        );
        Assertions.assertThat(board.getPotentialMoves()).containsOnly(
                board.getSquare('c', 3),
                board.getSquare('c', 4),
                board.getSquare('c', 5),
                board.getSquare('c', 6),
                board.getSquare('d', 3),
                board.getSquare('d', 6),
                board.getSquare('e', 3),
                board.getSquare('e', 6),
                board.getSquare('f', 3),
                board.getSquare('f', 4),
                board.getSquare('f', 5),
                board.getSquare('f', 6)
        );
    }

    @Nested
    @DisplayName("::deepCopy")
    @TestInstance(Lifecycle.PER_CLASS)
    class DeepCopy {

        Board board, copy;

        @BeforeAll
        void init() throws Exception {
            board = BoardFactory.instance().fromString(
                    " wwwww  " +
                    "  wwwbb " +
                    "w wwwbbb" +
                    "wwbbbwbb" +
                    "wwwbwbwb" +
                    "wwbbbbbb" +
                    " bbbww  " +
                    "  bww   "
            );

            copy = Board.deepCopy(board);
        }

        @Test
        @DisplayName("copy matches original")
        void testCopyMatchesOriginal() throws Exception {
            assertThat(copy).matches(board);
        }

        @Test
        @DisplayName("copy's squares are different references from original's squares")
        void testNotSameSquares() throws Exception {
            for (int rank = 0; rank < Board.SIZE; ++rank) {
                for (int file = 0; file < Board.SIZE; ++file) {
                    assertThat(copy.getSquare(rank, file)).isNotSameAs(board.getSquare(rank, file));
                }
            }
        }

        @Test
        @DisplayName("copy's occupied set points to copy's squares")
        void testCopyOccupiedSquaresBelongOnlyToCopy() throws Exception {
            copy.getOccupied().forEach(copySquare -> assertThat(copySquare).isSameAs(copy.getSquare(copySquare.RANK, copySquare.FILE)));
        }

        @Test
        @DisplayName("copy's potential moves set points to copy's squares")
        void testCopyPotentialMovesBelongOnlyToCopy() throws Exception {
            copy.getPotentialMoves().forEach(copySquare -> assertThat(copySquare).isSameAs(copy.getSquare(copySquare.RANK, copySquare.FILE)));
        }
    }

    @Nested
    @DisplayName("#setPiece")
    class SetPiece {

        Board board;

        @BeforeEach
        void init() throws Exception {
            board = BoardFactory.instance().fromString(
                    "        " +
                    "   w    " +
                    "  bww   " +
                    "  bwb   " +
                    "  wwbb  " +
                    "  w wb  " +
                    "        " +
                    "        "
            );
        }

        @Test
        @DisplayName("should update the game board")
        void testUpdatesGameBoard() throws Exception {
            Square move = board.getSquare('e', 2);
            board.setPiece(move, Color.BLACK);

            assertThat(board).matches(BoardFactory.instance().fromString(
                    "        " +
                    "   wb   " +
                    "  bbb   " +
                    "  bwb   " +
                    "  wwbb  " +
                    "  w wb  " +
                    "        " +
                    "        "
            ));
        }

        @Test
        @DisplayName("updates the set of occupied squares")
        void testUpdatesOccupiedSet() throws Exception {
            Set<Square> original = board.getOccupied();

            Square move = board.getSquare('e', 2);
            board.setPiece(move, Color.BLACK);

            Set<Square> actual = board.getOccupied();
            Set<Square> expected = Stream.concat(original.stream(), Stream.of(move)).collect(Collectors.toSet());

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("updates the set of potential move squares")
        void testUpdateFrontierSet() throws Exception {
            Set<Square> original = board.getPotentialMoves();

            Square move = board.getSquare('e', 2);
            board.setPiece(move, Color.BLACK);

            Set<Square> actual = board.getPotentialMoves();
            Set<Square> expected = Stream.concat(original.stream(), move.getNeighbors().stream())
                    .filter(Square::isUnoccupied)
                    .collect(Collectors.toSet());

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("should fail if given an illegal move")
        void testFailOnIllegalMove() throws Exception {
            Square move = board.getSquare('a', 1);
            assertThatIllegalArgumentException().isThrownBy(() -> board.setPiece(move, Color.BLACK));
        }
    }

    @Test
    @DisplayName("should tell if the player has any legal moves")
    void testHasMoveFor() throws Exception {
        Player white = mock(Player.class);
        when(white.getColor()).thenReturn(Color.WHITE);

        Player black = mock(Player.class);
        when(black.getColor()).thenReturn(Color.BLACK);

        Board board;
        board = BoardFactory.instance().fromString(
                "wwwwwwww" +
                "wwwwwwww" +
                "wwwwwwww" +
                "wwwwwww " +
                "wwwwww  " +
                "wwwwww b" +
                "wwwwwww " +
                "wwwwwwww"
        );

        assertFalse(board.hasMoveFor(white));
        assertFalse(board.hasMoveFor(black));

        board = BoardFactory.instance().fromString(
                "   www  " +
                "  bwwb b" +
                "wbwwwwbb" +
                "wwwwbwbb" +
                "wwwbwwbb" +
                "wwwwwbbb" +
                "  bbbbbb" +
                " bbbbbbb"
        );

        assertTrue(board.hasMoveFor(white));
        assertTrue(board.hasMoveFor(black));
    }

    @Test
    @DisplayName("should get the square's neighbors")
    void testGetNeighbors() throws Exception {
        Board board = BoardFactory.instance().newGame();

        Set<Square> expected = Stream.of(
                board.getSquare('a', 4),
                board.getSquare('b', 4),
                board.getSquare('b', 5),
                board.getSquare('b', 6),
                board.getSquare('a', 6)
        ).collect(Collectors.toSet());
        assertEquals(expected, board.getSquare('a', 5).getNeighbors());

        // return cached value
        assertEquals(expected, board.getSquare('a', 5).getNeighbors());
    }

    @Test
    @DisplayName("should tell if this move is legal")
    void testIsLegalMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "  wwww  " +
                "b bwbw  " +
                "bbbwbwww" +
                "bwwbbwww" +
                "bwbbwwww" +
                " wwwwwww" +
                "        " +
                "        "
        );

        // square is occupied
        assertFalse(board.getSquare('a', 2).isLegalMove(Color.BLACK));

        // neighboring square is null
        // neighboring square is empty
        // neighboring square is same color
        assertFalse(board.getSquare('h', 2).isLegalMove(Color.WHITE));

        // iterator hits an empty square
        assertFalse(board.getSquare('b', 1).isLegalMove(Color.BLACK));

        // iterator hits the end of the board
        assertFalse(board.getSquare('f', 7).isLegalMove(Color.WHITE));

        assertTrue(board.getSquare('a', 6).isLegalMove(Color.BLACK));
    }
}