package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.BoardUtils.Direction;
import info.jayharris.othello.BoardUtils.DirectionalIterator;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.heuristics.StableDiscsHeuristic;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A player that tries to maximize the number of stable discs they have on the board.
 */
public class StableDiscsPlayer extends HeuristicPlayer {

    public StableDiscsPlayer(Color color) {
        super(color, new StableDiscsHeuristic(color));
    }
}
