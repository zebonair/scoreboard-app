package scoreboardapp.exception;

public class MatchAlreadyFinishedException extends IllegalArgumentException {
    public MatchAlreadyFinishedException() {
        super("Match is already finished.");
    }
}
