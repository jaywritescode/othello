package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class RandomMovePlayerTest {

    Player player;
    Othello othello;

    static Field boardField;

    @BeforeAll
    static void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        boardField.setAccessible(true);
    }

    @BeforeEach
    void initTest() throws Exception {
        player = new RandomMovePlayer(Color.BLACK);
        othello = new Othello(player, null);
    }

    @Test
    @DisplayName("always gets a legal square")
    void testGetMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "        " +
                "   w    " +
                "  ww    " +
                "  wwbw  " +
                " bbbbb  " +
                "   b    " +
                "        " +
                "        "
        );
        boardField.set(othello, board);

        Set<Square> legalSquares = Stream.of(
                board.getSquare('d', 1),
                board.getSquare('b', 2),
                board.getSquare('c', 2),
                board.getSquare('e', 2),
                board.getSquare('b', 3),
                board.getSquare('e', 3),
                board.getSquare('f', 3),
                board.getSquare('g', 3),
                board.getSquare('b', 4),
                board.getSquare('g', 4)
        ).collect(Collectors.toSet());

        for (int i = 0; i < 20; ++i) {
            assertThat(player.getMove(othello)).isIn(legalSquares);
        }
    }
}
