import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuoteOfTheDay {

    private Error error;
    private Content contents;
    private String channelID;
    private static final Logger logger = Logger.getLogger(EventAPI.class.getName());

    public QuoteOfTheDay() {

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
        return contents.toString();
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

            String text = "_" + quote.getQuote() + "_\\n\\n- *" + quote.getAuthor() + "*";

            return "{\n" +
                    "  \"channel\": \"" + channelID + "\",\n" +
                    "  \"attachments\": [\n" +
                    "    {\n" +
                    "      \"blocks\": [\n" +
                    "        {\n" +
                    "          \"type\": \"section\",\n" +
                    "          \"text\": {\n" +
                    "            \"type\": \"mrkdwn\",\n" +
                    "            \"text\": \"" + text + "\"\n" +
                    "          },\n" +
                    "          \"accessory\": {\n" +
                    "            \"type\": \"image\",\n" +
                    "            \"image_url\": \"" + quote.getBackground() + "\",\n" +
                    "            \"alt_text\": \"quote-" + quote.getCategory() + "\"\n" +
                    "          }\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"type\": \"context\",\n" +
                    "          \"elements\": [\n" +
                    "            {\n" +
                    "              \"type\": \"mrkdwn\",\n" +
                    "              \"text\": \"*Last updated:* " + formatDate + "\\n:copyright: " + contents.getCopyright() + "\\n\"\n" +
                    "            }\n" +
                    "          ]\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        } else {
            logger.log(Level.WARNING, "Error : contents is empty!");
            return "";
        }
    }
}
