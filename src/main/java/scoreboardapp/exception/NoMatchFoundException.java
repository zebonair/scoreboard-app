package scoreboardapp.exception;

public class NoMatchFoundException extends IllegalArgumentException {
    public NoMatchFoundException() {
        super("No match found with the given ID.");
    }
}
