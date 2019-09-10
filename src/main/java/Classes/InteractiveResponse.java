package Classes;

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
        return "\ntype: " + type + "\nuser: {\n" + user.toString() + "\n}\napi_app_id: "
                + api_app_id + "\ntoken: " + token + "\nresponse_url: " + response_url + "\naction: {\n" + actions.get(0).toString() + "\n}";
    }
}
