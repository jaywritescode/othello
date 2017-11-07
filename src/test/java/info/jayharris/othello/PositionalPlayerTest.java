package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class PositionalPlayerTest {

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
        player = new PositionalPlayer(Color.BLACK);
        othello = new Othello(player, null);
    }

    @Test
    @DisplayName("chooses the move with the highest score")
    void testGetMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "        " +
                "        " +
                "        " +
                "   wb   " +
                "   wbb  " +
                "   www  " +
                "    bw  " +
                "        "
        );
        boardField.set(othello, board);

        // Scores
        // c3:   7 + 0 = 7
        // c4:   4 + 0 = 4
        // c5:   4 + 0 + 4 = 8
        // c6:   7 + 0 = 7
        // c7:  -4 + 4 = 0
        // d7:  -3 + 4 = -1
        // f8:   8 - 4 + 7 = 11
        // g5:  -3 + 7 = 4
        // g7: -24 + 7 = -17

        assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('f', 8));
    }

}