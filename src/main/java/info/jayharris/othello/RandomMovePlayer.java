package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RandomMovePlayer extends Player {

    private final Random random = new Random();

    public RandomMovePlayer(Color color) {
        super(color);
    }

    @Override
    public Square getMove(Othello othello) {
        List<Square> moves = othello.getBoard().getLegalMovesFor(this).stream().collect(Collectors.toList());

        if (moves.isEmpty()) {
            throw new IllegalStateException();
        }

        int i = random.nextInt(moves.size());
        return moves.listIterator(i).next();
    }
}
