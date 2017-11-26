package info.jayharris.othello.heuristics;

import info.jayharris.othello.Board;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StableDiscsHeuristicTest {

    @Test
    @DisplayName("returns the number of stable discs the player has less the number of stable discs the opponent has")
    public void testStableDiscsHeuristic() throws Exception {
        Board board = BoardFactory.getFactory().fromString(
                "   www  " +
                "  bwwb b" +
                "wbwwwwbb" +
                "wwwwbwbb" +
                "wwwbwwbb" +
                "wwwwwbbb" +
                "  bbbbbb" +
                " bbbbbbb"
        );

        assertThat(new StableDiscsHeuristic(Color.BLACK).apply(board)).isEqualTo(22);
    }
}
