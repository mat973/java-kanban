package exeptions;

public class EpicNotExistException extends RuntimeException {
    public EpicNotExistException(String message) {
        super(message);
    }
}
