import io.github.cdimascio.dotenv.Dotenv;

public class EnvVars {
    private String TOKEN;
    private String CHANNEL_GENERAL;

    public EnvVars() {
        // TODO : get path to .env file more dynamically
        Dotenv dotenv = Dotenv.configure().directory("C:\\Users\\BredeKlausen\\OneDrive - ITverket AS\\Dokumenter\\Kompetanseheving\\Java\\alfred\\").load();

        TOKEN = dotenv.get("SLACK-BOT-TOKEN");
        CHANNEL_GENERAL = dotenv.get("CHANNEL-ID-GENERAL");
    }

    public String getChannelGeneral() {
        return CHANNEL_GENERAL;
    }

    public String getTOKEN() {
        return TOKEN;
    }
}
