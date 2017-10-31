package info.jayharris.othello;

import info.jayharris.othello.Board.Square;
import info.jayharris.othello.Othello.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;
import java.util.regex.Pattern;

public class TerminalPlayer extends Player {

    BufferedReader reader;
    final static Pattern pattern = Pattern.compile("^[a-h][1-8]$", Pattern.CASE_INSENSITIVE);

    public TerminalPlayer(Color color) {
        super(color);
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public Square getMove(Othello othello) {
        System.out.println(othello.getBoard().pretty());
        System.out.print(String.format("%s to move: ", getColor()));

        Square move = null;
        while (true) {
            try {
                move = getLegalMove(othello);
            }
            catch (InvalidInputException e) {
                System.out.println(e.getMessage());
            }

            return move;
        }
    }

    private Square getLegalMove(Othello othello) throws InvalidInputException {
        Set<Square> legalMoves = getLegalMoves(othello.getBoard());

        Square move;
        try {
            String line;
            if (!pattern.matcher(line = reader.readLine()).matches()) {
                throw new InvalidAlgebraicNotationException(line);
            }
            move = othello.getBoard().getSquare(line.charAt(0), Integer.parseInt(line.substring(1)));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!legalMoves.contains(move)) {
            throw new IllegalMoveException(move);
        }
        return move;
    }

    private class InvalidInputException extends Exception {
        public InvalidInputException(String s) {
            super(s);
        }
    }

    private class InvalidAlgebraicNotationException extends InvalidInputException {
        InvalidAlgebraicNotationException(String input) {
            super(String.format("%s is not valid algebraic notation.", input));
        }
    }

    private class IllegalMoveException extends InvalidInputException {
        IllegalMoveException(Square square) {
            super(String.format("%s is an illegal move.", square.algebraicNotation()));
        }
    }
}
