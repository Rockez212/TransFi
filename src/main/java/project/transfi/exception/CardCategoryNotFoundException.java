package project.transfi.exception;

public class CardCategoryNotFoundException extends RuntimeException {
    public CardCategoryNotFoundException(String message) {
        super(message);
    }
}
