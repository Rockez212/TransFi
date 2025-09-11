package project.transfi.exception;

public class UserAlreadyHasBankAccount extends RuntimeException {
    public UserAlreadyHasBankAccount(String message) {
        super(message);
    }
}
