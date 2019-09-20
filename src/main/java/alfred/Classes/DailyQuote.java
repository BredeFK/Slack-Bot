package alfred.Classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DailyQuote {

    private Error error;
    private Content contents;
    private String channelID;
    private static final Logger logger = Logger.getLogger(DailyQuote.class.getName());

    public DailyQuote() {

    }

    public Error getError() {
        return error;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public Content getContents() {
        return contents;
    }

    @Override
    public String toString() {
        return String.format("%nerror: %s%ncontents: { %s %n}%nchannelIdD: %s",
                error, contents.toString(), channelID);
    }

    // Converts object to formatted json that can be sent to Slacks "Block Kit Builder" https://api.slack.com/tools/block-kit-builder
    public String toJson() {

        if (!contents.getQuotes().isEmpty()) {
            Quote quote = contents.getQuotes().get(0);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            String formatDate = "";

            try {
                formatDate = sdf.format(quote.getDate());
            } catch (ParseException e) {
                logger.log(Level.WARNING, "Error " + e.getMessage());
                return "";
            }

            String text = String.format("`Quote`\\n_%s_\\n\\n- *%s*", quote.getQuote(), quote.getAuthor());

            // See Templates.md for better understanding of this json
            String output = "{\n\"channel\": \"%s\",\n\"attachments\": [\n{\n\"blocks\": [\n{\n\"type\": \"section\",\n" +
                    "\"text\": {\n\"type\": \"mrkdwn\",\n\"text\": \"%s\"\n}\n},\n{\n\"type\": \"context\",\n\"elements\": [\n" +
                    "{\n\"type\": \"mrkdwn\",\n\"text\": \"*Last updated:* %s\\n:copyright: %s\\n\"\n}\n]\n}\n]\n}\n]\n}";

            return String.format(output, channelID, text, formatDate, contents.getCopyright());
        } else {
            logger.log(Level.WARNING, "Error : contents is empty!");
            return "";
        }
    }
}
