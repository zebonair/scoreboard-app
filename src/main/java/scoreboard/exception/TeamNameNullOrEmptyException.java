package scoreboard.exception;

public class TeamNameNullOrEmptyException extends IllegalArgumentException {
    public TeamNameNullOrEmptyException() {
        super("Team names cannot be null or empty.");
    }
}
