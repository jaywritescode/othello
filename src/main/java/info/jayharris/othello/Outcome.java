package info.jayharris.othello;

class Outcome {

    enum Winner { BLACK, WHITE, TIE };

    private final Winner winner;
    private final long winnerScore, loserScore;

    final String WINNER_TPL = "%s wins! (%s-%s)";
    final String TIE_TPL = "It's a tie! (%s-%s)";

    private Outcome(Winner winner, long winnerScore, long loserScore) {
        this.winner = winner;
        this.winnerScore = winnerScore;
        this.loserScore = loserScore;
    }

    private static OutcomeBuilder builder() {
        return new OutcomeBuilder();
    }

    public static Outcome whoWon(long blackScore, long whiteScore) {
        return builder().whiteScore(whiteScore).blackScore(blackScore).build();
    }

    public Winner getWinner() {
        return winner;
    }

    public long getWinnerScore() {
        return winnerScore;
    }

    public long getLoserScore() {
        return loserScore;
    }

    @Override
    public String toString() {
        if (winner == Winner.TIE) {
            return String.format(TIE_TPL, getWinnerScore(), getLoserScore());
        }
        return String.format(WINNER_TPL, winner, getWinnerScore(), getLoserScore());
    }

    private static class OutcomeBuilder {

        long blackScore, whiteScore;

        OutcomeBuilder blackScore(long score) {
            this.blackScore = score;
            return this;
        }

        OutcomeBuilder whiteScore(long score) {
            this.whiteScore = score;
            return this;
        }

        Outcome build() {
            Winner winner;
            long winnerScore, loserScore;

            if (whiteScore > blackScore) {
                winner = Winner.WHITE;
                winnerScore = whiteScore;
                loserScore = blackScore;
            }
            else if (blackScore > whiteScore) {
                winner = Winner.BLACK;
                winnerScore = blackScore;
                loserScore = whiteScore;
            }
            else {
                winner = Winner.TIE;
                winnerScore = whiteScore;
                loserScore = blackScore;
            }

            return new Outcome(winner, winnerScore, loserScore);
        }
    }
}
