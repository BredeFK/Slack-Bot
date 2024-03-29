package alfred.models.slack;

import java.util.ArrayList;

public class InteractiveResponse {
    private String type;
    private User user;
    private String api_app_id;
    private String token;
    private String response_url;
    private ArrayList<Action> actions;

    public ArrayList<Action> getActions() {
        return actions;
    }

    public User getUser() {
        return user;
    }

    public String getResponse_url() {
        return response_url;
    }

    public String getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public String getApi_app_id() {
        return api_app_id;
    }

    @Override
    public String toString() {
        return String.format("%ntype: %s%nuser: {%n %s %n}%napi_app_id: %s%ntoken: %s%nresponse_url: %s%naction: {%n %s %n}",
                type, user.toString(), api_app_id, token, response_url, actions.get(0).toString());
    }
}
