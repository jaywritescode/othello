package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FrontierEvaporationHeuristicTest {

    @Test
    @DisplayName("returns the (negative) number of [color] discs adjacent to an empty square")
    public void testFrontierEvaporationHeuristic() throws Exception {
        Board board = BoardFactory.getFactory().fromString(
                "  wwww  " +
                "b bwbw  " +
                "bbbwbwww" +
                "bwwbbwww" +
                "bwbbwwww" +
                " wwwwwww" +
                "        " +
                "        "
        );

        assertThat(new FrontierEvaporationHeuristic(Color.BLACK).apply(board)).isEqualTo(-6);
        assertThat(new FrontierEvaporationHeuristic(Color.WHITE).apply(board)).isEqualTo(-14);
    }
}
