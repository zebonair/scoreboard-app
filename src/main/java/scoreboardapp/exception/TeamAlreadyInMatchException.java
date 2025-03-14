package scoreboardapp.exception;

public class TeamAlreadyInMatchException extends IllegalArgumentException {
    public TeamAlreadyInMatchException(String team) {
        super(team + " is already in an ongoing match.");
    }
}
