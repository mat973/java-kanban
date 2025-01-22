package exeptions;

public class SubtaskNotFoundException extends RuntimeException {
    public SubtaskNotFoundException(String s) {
        super(s);
    }
}
