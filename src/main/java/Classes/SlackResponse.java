package Classes;

import java.util.ArrayList;
import java.util.Objects;

// POSTMessageResponse is a class for returned response when posting message
public class SlackResponse {
    private boolean ok;
    private String error;
    private String warning;
    private ArrayList<Objects> actions;

    public SlackResponse() {

    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setError(String error) {
        this.error = error;
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

    @Override
    public String toString() {
        return String.format("%nok: %s%nerror: %s%nwarning: %s%nactions: %s",
                ok, error, warning, actions.get(0).toString());
    }
}
