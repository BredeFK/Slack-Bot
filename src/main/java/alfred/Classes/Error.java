package alfred.Classes;

public class Error {

    private int code;
    private String message;

    public Error() {
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("%d - %s", code, message);
    }
}
