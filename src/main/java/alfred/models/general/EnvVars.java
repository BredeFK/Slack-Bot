package alfred.models.general;

public class EnvVars {
    private final String SLACK_TOKEN;
    private final String CHANNEL_GENERAL;
    private final String WEBSITE_NAME;
    private final String WEBSITE_URL;
    private final String CONTACT_EMAIL;
    private final String IPIFY_TOKEN;
    private final String SLACK_SIGNING_SECRET;

    public EnvVars() {
        SLACK_TOKEN = System.getenv("SLACK-BOT-TOKEN");
        CHANNEL_GENERAL = System.getenv("CHANNEL-ID-GENERAL");
        WEBSITE_NAME = System.getenv("WEBSITE-NAME");
        WEBSITE_URL = System.getenv("WEBSITE-URL");
        CONTACT_EMAIL = System.getenv("CONTACT-EMAIL");
        IPIFY_TOKEN = System.getenv("IPIFY-TOKEN");
        SLACK_SIGNING_SECRET = System.getenv("SLACK-SIGNING-SECRET");
    }

    public String getChannelGeneral() {
        return CHANNEL_GENERAL;
    }

    public String getSlackToken() {
        return SLACK_TOKEN;
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

    public String getIpifyToken() {
        return IPIFY_TOKEN;
    }

    public String getSlackSigningSecret() {
        return SLACK_SIGNING_SECRET;
    }
}
