package info.jayharris.othello;

import info.jayharris.othello.Outcome.Winner;

import java.util.Map;
import java.util.stream.Collectors;

import static info.jayharris.othello.Outcome.Winner.*;

public class Othello {

    public enum Color {
        BLACK, WHITE;

        public Color opposite() {
            return this == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
    };

    private final Board board;
    private final Player black, white;
    private Player current;

    public Othello(Player black, Player white) {
        this.board = Board.init();

        this.black = this.current = black;
        this.white = white;
    }

    public Outcome play() {
        while (nextPly() != null);
        return gameOver();
    }

    public Player nextPly() {
        Board.Square move;

        do {
            move = current.getMove(this);
        } while (!move.isLegalMove(current.getColor()));

        board.setPiece(move, current.getColor());

        return nextPlayer();
    }

    private Player nextPlayer() {
        Player next = (current == black ? white : black);

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

    public static void main(String... args) {
        Othello othello = new Othello(new TerminalPlayer(Color.BLACK), new TerminalPlayer(Color.WHITE));
        Outcome outcome = othello.play();

        switch (outcome.getWinner()) {
            case BLACK:
                System.out.println("black wins");
                break;
            case WHITE:
                System.out.println("white wins");
                break;
            case TIE:
            default:
                System.out.println("it's a tie");
                break;
        }
        System.out.println(String.format("the score is %s to %s", outcome.getWinnerScore(), outcome.getLoserScore()));
    }
}
