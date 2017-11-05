package info.jayharris.othello;

import java.util.Map;
import java.util.stream.Collectors;

public class Othello {

    public enum Color {
        BLACK, WHITE;

        public Color opposite() {
            return this == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
    };

    private final Board board;
    private final Player black, white;
    private int turnsPlayed = 0;

    public Othello(Player black, Player white) {
        this.board = Board.init();

        this.black = black;
        this.white = white;
    }

    public Outcome play() {
        Player current = black;

        while ((current = nextPly(current)) != null);
        return gameOver();
    }

    /**
     * Plays the next ply of the game.
     *
     * @param current the player whose turn it is to move
     * @return the player whose turn it is to move after {@code current} plays
     */
    public Player nextPly(Player current) {
        current.begin(this);
        while (true) {
            try {
                board.setPiece(current.getMove(this), current.getColor());

                ++turnsPlayed;
                current.done(this);
                return nextPlayer(current);
            }
            catch (IllegalArgumentException e) {
                current.fail(this, e);
            }
        }
    }

    private Player nextPlayer(Player current) {
        Player next;

        next = (current == black ? white : black);

        if (board.hasMoveFor(next)) {
            return next;
        }
        if (board.hasMoveFor(current)) {
            return current;
        }
        return null;
    }

    public Outcome gameOver() {
        Map<Color, Long> scores = board.getOccupied().stream()
                .collect(Collectors.groupingBy(Board.Square::getColor, Collectors.counting()));
        return Outcome.whoWon(scores.get(Color.BLACK), scores.get(Color.WHITE));
    }

    public Board getBoard() {
        return board;
    }

    public int getTurnsPlayed() {
        return turnsPlayed;
    }

    public static void main(String... args) {
        Othello othello = new Othello(new RandomMovePlayer(Color.BLACK), new RandomMovePlayer(Color.WHITE));
        othello.play();
        System.out.println(othello.getBoard().pretty());
    }
}
