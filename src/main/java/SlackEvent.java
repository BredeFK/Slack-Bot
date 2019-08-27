public class SlackEvent {

    private String token;
    private String challenge;
    private String type;

    public SlackEvent() {
    }

    public String getToken() {
        return token;
    }

    public String getChallenge() {
        return challenge;
    }

    public String getType() {
        return type;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Return string in json format
    @Override
    public String toString() {
        return '{' +
                "\n\t\"token\": \"" + token +
                "\",\n\t\"challenge\": \"" + challenge +
                "\",\n\t\"type\": \"" + type +
                "\"\n}";
    }
}
