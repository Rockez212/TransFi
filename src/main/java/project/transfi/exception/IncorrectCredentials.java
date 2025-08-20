package project.transfi.exception;

public class IncorrectCredentials extends RuntimeException {
    public IncorrectCredentials(String message) {
        super(message);
    }
}
