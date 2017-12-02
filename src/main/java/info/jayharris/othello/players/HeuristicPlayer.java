package info.jayharris.othello.players;

import info.jayharris.othello.Board;
import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello;
import info.jayharris.othello.Othello.Color;
import info.jayharris.othello.Player;
import info.jayharris.othello.heuristics.HeuristicFunction;

public class HeuristicPlayer extends Player {

    final HeuristicFunction h;

    public HeuristicPlayer(Color color, HeuristicFunction h) {
        super(color);
        this.h = h;
    }

    @Override
    public Square getMove(Othello othello) {
        Board currentBoard = othello.getBoard();

        return getLegalMoves(currentBoard).stream()
                .filter(it -> it.isLegalMove(color))
                .collect(h.bestMove(currentBoard))
                .orElseThrow(IllegalStateException::new);
    }
}
