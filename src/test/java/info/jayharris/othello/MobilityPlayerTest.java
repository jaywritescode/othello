package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class MobilityPlayerTest {

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
        player = new MobilityPlayer(Color.BLACK);
        othello = new Othello(player, null);
    }

    @Test
    @DisplayName("chooses move with the lowest opponent's score")
    void testGetMove() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "    bbb " +
                "    wb  " +
                " wbwwbbb" +
                "bbwbwbbb" +
                "wbwwwbbb" +
                " bwwwww " +
                "  bwwwww"
        );
        boardField.set(othello, board);

        // Scores:
        // d1: 13
        // b2: 14
        // c2: 15
        // d2: 12
        // a3: 13
        // a6: 10
        // h6: 12
        // b7: 12
        // c8: 13
        // d8: 14
        // e8: 15
        // f8: 15
        // g8: 14
        // h8: 13

        assertThat(player.getMove(othello)).isSameAs(othello.getBoard().getSquare('a', 6));
    }
}
