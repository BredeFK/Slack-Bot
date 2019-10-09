package alfred.models.general;

public class EnvVars {
    private final String TOKEN;
    private final String CHANNEL_GENERAL;
    private final String WEBSITE_NAME;
    private final String WEBSITE_URL;
    private final String CONTACT_EMAIL;

    public EnvVars() {
        TOKEN = System.getenv("SLACK-BOT-TOKEN");
        CHANNEL_GENERAL = System.getenv("CHANNEL-ID-GENERAL");
        WEBSITE_NAME = System.getenv("WEBSITE-NAME");
        WEBSITE_URL = System.getenv("WEBSITE-URL");
        CONTACT_EMAIL = System.getenv("CONTACT-EMAIL");
    }

    public String getChannelGeneral() {
        return CHANNEL_GENERAL;
    }

    public String getToken() {
        return TOKEN;
    }

    public String getWebsiteName() {
        return WEBSITE_NAME;
    }

    public String getWebsiteURL() {
        return WEBSITE_URL;
    }

    public String getContactEmail() {
        return CONTACT_EMAIL;
    }
}
