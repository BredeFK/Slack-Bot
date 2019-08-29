// POSTMessageResponse is a class for returned response when posting message
public class POSTMessageResponse {
    private boolean ok;
    private String channel;
    private String error;
    private String warning;

    public POSTMessageResponse() {

    }

    public boolean isOk() {
        return ok;
    }

    public String getChannel() {
        return channel;
    }

    public String getError() {
        return error;
    }

    public String getWarning() {
        return warning;
    }
}
