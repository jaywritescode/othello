package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.Outcome.Winner;
import org.apache.commons.collections4.IteratorUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.stream.Stream;

import static info.jayharris.othello.BoardAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.junit.jupiter.api.Assertions.assertSame;


class OthelloTest {

    private static Field boardField;

    @BeforeAll
    static void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        boardField.setAccessible(true);
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

        assertSame(white, othello.nextPly(black));
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

        assertThatIllegalStateException().isThrownBy(() -> othello.nextPly(black));
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
            Assertions.assertThat(othello.nextPly(white)).isEqualTo(black);
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
            Assertions.assertThat(othello.nextPly(black)).isEqualTo(black);
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
            Assertions.assertThat(othello.nextPly(white)).isNull();
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

    @Test
    @DisplayName("run the entire game")
    void endToEndTest() {
        Iterator<Character> moves = IteratorUtils.arrayIterator(new StringBuilder()
                .append("d3c5f6f5e6e3c3f3c4b4b5c2a3b3d2c1e1d7e7a4a5d6c6a6a7b6d8b2c7f8")
                .append("f2g5e8f7g3d1f4e2f1h3g2c8g6h5h4g4h2b7a8h1b8g7b1a1a2g1g8h8h7h6")
                .toString()
                .toCharArray());

        Player black = new Player(Color.BLACK) {
            @Override
            public Square getMove(Othello othello) {
                char file = moves.next();
                int rank = moves.next() - '1' + 1;

                return othello.getBoard().getSquare(file, rank);
            }
        };

        Player white = new Player(Color.WHITE) {
            @Override
            public Square getMove(Othello othello) {
                char file = moves.next();
                int rank = moves.next() - '1' + 1;

                return othello.getBoard().getSquare(file, rank);
            }
        };

        Othello othello = new Othello(black, white);

        Outcome outcome = othello.play();
        System.out.println(othello.getBoard().pretty());

        Assertions.assertThat(outcome.getWinner()).isEqualTo(Winner.BLACK);
        Assertions.assertThat(outcome.getWinnerScore()).isEqualTo(35);
        Assertions.assertThat(outcome.getLoserScore()).isEqualTo(29);
    }
}