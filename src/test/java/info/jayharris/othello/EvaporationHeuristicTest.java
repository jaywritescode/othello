package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.heuristics.EvaporationHeuristic;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EvaporationHeuristicTest {

    @Test
    @DisplayName("returns the (negative) number of [color] discs on the board")
    public void testApplyWithDiscsOnBoard() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "b       " +
                " b  w   " +
                " bbw b  " +
                "  wwbb  " +
                "   bbb  " +
                "        " +
                "        " +
                "        "
        );

        assertThat(new EvaporationHeuristic(Color.BLACK).apply(board)).isEqualTo(-10);
    }

    @Test
    @DisplayName("returns negative infinity if we have zero discs on the board")
    public void testApplyNoDiscsOnBoard() throws Exception {
        Board board = BoardFactory.instance().fromString(
                "        " +
                "  b     " +
                "  bb    " +
                "  bbbbb " +
                "   bbb  " +
                "      b " +
                "       b" +
                "        "
        );

        assertThat(new EvaporationHeuristic(Color.WHITE).apply(board)).isEqualTo(-Long.MAX_VALUE);

    }
}
