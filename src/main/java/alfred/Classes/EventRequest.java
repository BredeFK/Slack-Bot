package alfred.Classes;

public class EventRequest {
    private String token;
    private String challenge;
    private String type;


    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public String getChallenge() {
        return challenge;
    }

    @Override
    public String toString() {
        return String.format("%ntoken: %s%nchallenge: %s%ntype: %s", token, challenge, type);
    }
}
