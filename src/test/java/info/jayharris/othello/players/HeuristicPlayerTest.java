package info.jayharris.othello.players;

import info.jayharris.othello.Board;
import info.jayharris.othello.BoardFactory;
import info.jayharris.othello.Othello;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.Player;
import info.jayharris.othello.heuristics.HeuristicFunction;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
public class HeuristicPlayerTest {

    @Mock HeuristicFunction function;

    Player player;
    Othello othello;

    static Field boardField;

    @BeforeAll
    void init() throws Exception {
        boardField = Othello.class.getDeclaredField("board");
        boardField.setAccessible(true);
    }

    @BeforeEach
    void initTest() throws Exception {
        MockitoAnnotations.initMocks(this);

        Board board = BoardFactory.instance().fromString(
                "        " +
                "  bw    " +
                "  bb    " +
                " bbbbw  " +
                "  bbbb  " +
                "  wwb   " +
                "     b  " +
                "        "
        );

        Map<Board, Long> mockHeuristicValues = new HashMap<>();
        Stream.of(
                ImmutablePair.of(board.getSquare('c', 1), 5L),
                ImmutablePair.of(board.getSquare('b', 2), -17L),
                ImmutablePair.of(board.getSquare('a', 3), 6L),
                ImmutablePair.of(board.getSquare('f', 3), 7L),
                ImmutablePair.of(board.getSquare('a', 4), -1L),
                ImmutablePair.of(board.getSquare('a', 5), 3L),
                ImmutablePair.of(board.getSquare('f', 6), 13L)
        ).forEach(pair -> {
            Board copy = Board.deepCopy(board);
            copy.setPiece(copy.getSquare(pair.getLeft()), Color.WHITE);
            mockHeuristicValues.put(copy, pair.getRight());
        });

        when(function.apply(any())).thenAnswer(invocation -> mockHeuristicValues.get(invocation.getArgument(0)));

        player = new HeuristicPlayer(Color.WHITE, function);
        othello = new Othello(null, player);
        boardField.set(othello, board);
    }

    @Test
    @DisplayName("it chooses the move with the greatest heuristic value")
    void testMaximizingHeuristic() throws Exception {
        HeuristicPlayer player = new HeuristicPlayer(Color.WHITE, function);

        assertThat(player.getMove(othello)).isEqualTo(othello.getBoard().getSquare('f', 6));
    }

    @Test
    @DisplayName("it chooses the move with the smallest heuristic value")
    void testMinimizingHeuristic() throws Exception {
        HeuristicPlayer player = new HeuristicPlayer(Color.WHITE, HeuristicPlayer.minimize(function));

        assertThat(player.getMove(othello)).isEqualTo(othello.getBoard().getSquare('b', 2));
    }
}
