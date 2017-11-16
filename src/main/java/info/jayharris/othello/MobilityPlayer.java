package info.jayharris.othello;

import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.heuristics.MobilityHeuristic;

/**
 * A player that chooses the move the gives the opponent the fewest legal moves.
 */
public class MobilityPlayer extends HeuristicPlayer {

    public MobilityPlayer(Color color) {
        super(color, new MobilityHeuristic(color));
    }
}
