package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.Outcome.Winner;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import static info.jayharris.othello.BoardAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertSame;


class OthelloTest {

    static Field boardField;
    static Field currentField;

    @BeforeAll
    static void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        currentField = Othello.class.getDeclaredField("current");

        Stream.of(boardField, currentField).forEach(field -> field.setAccessible(true));
    }

    @Test
    @DisplayName("runs the next ply")
    void nextPly() throws Exception {
        Player black = new Player(Color.BLACK) {
            @Override
            public Square getMove(Othello othello) {
                return othello.getBoard().getSquare('c', 4);
            }
        };
        Player white = new Player(Color.WHITE) {
            @Override
            public Square getMove(Othello othello) {
                return null;
            }
        };

        Othello othello = new Othello(black, white);

        assertSame(white, othello.nextPly());
        assertThat(othello.getBoard()).matches(BoardFactory.instance().fromString(
                "        " +
                "        " +
                "        " +
                "  bbb   " +
                "   bw   " +
                "        " +
                "        " +
                "        "
        ));
    }

    @Test
    @DisplayName("should not play an illegal move")
    void nextPlyIllegalMove() throws Exception {
        Stream.Builder<Square> movesBuilder = Stream.builder();

        Player black = new Player(Color.BLACK) {
            @Override
            public Square getMove(Othello othello) {
                return movesBuilder.build().findFirst().orElseThrow(IllegalStateException::new);
            }
        };
        Player white = new Player(Color.WHITE) {
            @Override
            public Square getMove(Othello othello) {
                return null;
            }
        };

        Othello othello = new Othello(black, white);
        movesBuilder.add(othello.getBoard().getSquare('c', 5));

        assertThatIllegalStateException().isThrownBy(othello::nextPly);
        assertThat(othello.getBoard()).matches(BoardFactory.instance().newGame());
    }

    @Nested
    @DisplayName("next player")
    class NextPlayer {

        Othello othello;
        Board board;
        Player black, white;

        @Test
        @DisplayName("other player has legal move")
        void testOtherPlayerHasLegalMove() throws Exception {
            board = BoardFactory.instance().fromString(
                    "        " +
                    "        " +
                    "   ww   " +
                    "  bbwb  " +
                    "  wbbw  " +
                    "    bbw " +
                    "     w  " +
                    "        "
            );

            black = new Player(Color.BLACK) {
                @Override
                public Square getMove(Othello othello) {
                    return null;
                }
            };
            white = new Player(Color.WHITE) {
                @Override
                public Square getMove(Othello othello) {
                    return othello.getBoard().getSquare('d', 6);
                }
            };

            othello = new Othello(black, white);

            boardField.set(othello, board);
            currentField.set(othello, white);

            Assertions.assertThat(othello.nextPly()).isEqualTo(black);
        }

        @Test
        @DisplayName("other player has no legal move")
        void testOtherPlayerHasNoLegalMove() throws Exception {
            board = BoardFactory.instance().fromString(
                    "        " +
                    "        " +
                    " wwwwwwb" +
                    "wwwwwwbb" +
                    "bwwwbbbb" +
                    "bbwbbbbb" +
                    "bbbbbbbb" +
                    "bbbbbbbb"
            );

            black = new Player(Color.BLACK) {
                @Override
                public Square getMove(Othello othello) {
                    return othello.getBoard().getSquare('h', 2);
                }
            };
            white = new Player(Color.WHITE) {
                @Override
                public Square getMove(Othello othello) {
                    return null;
                }
            };

            othello = new Othello(black, white);

            boardField.set(othello, board);
            currentField.set(othello, black);

            Assertions.assertThat(othello.nextPly()).isEqualTo(black);
        }

        @Test
        @DisplayName("neither player has a legal move")
        void testGameOver() throws Exception {
            board = BoardFactory.instance().fromString(
                    "wwwwwwww" +
                    "wwwwwwwb" +
                    "wbbbbbb " +
                    "wwwwwwb " +
                    "wwwwww  " +
                    "wwwwww b" +
                    "wwwwwww " +
                    "wwwwwwww"
            );

            black = new Player(Color.BLACK) {
                @Override
                public Square getMove(Othello othello) {
                    return null;
                }
            };
            white = new Player(Color.WHITE) {
                @Override
                public Square getMove(Othello othello) {
                    return othello.getBoard().getSquare('h', 3);
                }
            };

            othello = new Othello(black, white);

            boardField.set(othello, board);
            currentField.set(othello, white);

            Assertions.assertThat(othello.nextPly()).isNull();
        }
    }

    @Nested
    @DisplayName("game over")
    class GameOver {

        Board board;
        Othello othello;

        @Test
        @DisplayName("black wins")
        void testWinner() throws Exception {
            board = BoardFactory.instance().fromString(
                    "wbwwwwww" +
                    "wbbbbwwb" +
                    "wbwbwwwb" +
                    "wbbwbbwb" +
                    "wbwwwbwb" +
                    "wbwwbbbb" +
                    "wbwwbbbb" +
                    "bbbbbbbb"
            );

            othello = new Othello(null, null);

            boardField.set(othello, board);

            Outcome actual = othello.gameOver();
            Assertions.assertThat(actual.getWinner()).isEqualTo(Winner.BLACK);
            Assertions.assertThat(actual.getWinnerScore()).isEqualTo(35);
            Assertions.assertThat(actual.getLoserScore()).isEqualTo(29);
        }

        @Test
        @DisplayName("it's a tie")
        void testTie() throws Exception {
            board = BoardFactory.instance().fromString(
                    "wwwwwwwb" +
                    "bbbwwwwb" +
                    "wbwwwbwb" +
                    "wbwwbbwb" +
                    "wbwbwwwb" +
                    "wwbbbwbb" +
                    "wwwbbbwb" +
                    "bbbbbbbb"
            );

            othello = new Othello(null, null);

            boardField.set(othello, board);

            Outcome actual = othello.gameOver();
            Assertions.assertThat(actual.getWinner()).isEqualTo(Winner.TIE);
            Assertions.assertThat(actual.getWinnerScore()).isEqualTo(32);
            Assertions.assertThat(actual.getLoserScore()).isEqualTo(32);
        }
    }
}