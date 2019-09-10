package Classes;

public class EnvVars {
    private String TOKEN;
    private String CHANNEL_GENERAL;

    public EnvVars() {
        TOKEN = System.getenv("SLACK-BOT-TOKEN");
        CHANNEL_GENERAL = System.getenv("CHANNEL-ID-GENERAL");
    }

    public String getChannelGeneral() {
        return CHANNEL_GENERAL;
    }

    public String getTOKEN() {
        return TOKEN;
    }
}
