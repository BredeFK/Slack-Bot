// POSTMessageResponse is a class for returned response when posting message
public class SlackResponse {
    private boolean ok;
    private String error;
    private String warning;

    public SlackResponse() {

    }

    public boolean isOk() {
        return ok;
    }

    public String getError() {
        return error;
    }

    public String getWarning() {
        return warning;
    }
}
