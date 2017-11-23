package info.jayharris.othello.players;

import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.heuristics.StableDiscsHeuristic;
import info.jayharris.othello.players.HeuristicPlayer;

/**
 * A player that tries to maximize the number of stable discs they have on the board.
 */
@Deprecated
public class StableDiscsPlayer extends HeuristicPlayer {

    public StableDiscsPlayer(Color color) {
        super(color, new StableDiscsHeuristic(color));
    }
}
