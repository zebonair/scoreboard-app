package scoreboardapp.exception;

public class SameTeamException extends IllegalArgumentException {
    public SameTeamException() {
        super("Home Team and Away Team can't be the same");
    }
}
