package alfred.models.dailyquote;

import alfred.models.general.Error;

import java.text.SimpleDateFormat;

public class DailyQuote {

    private long id;

    private Error error;

    private Content contents;
    
    private String channelID;

    public DailyQuote() {

    }

    public long getId() {
        return id;
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

        Quote quote = null;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        String formatDate = "";

        try {
            quote = contents.getSingleQuote();
            formatDate = sdf.format(quote.getDate());
        } catch (Exception e) {
            return e.getMessage();
        }

        String text = String.format("`Quote`\\n_%s_\\n\\n- *%s*", quote.getQuote(), quote.getAuthor());

        // See Templates.md for better understanding of this json
        String output = "{\n\"channel\": \"%s\",\n\"attachments\": [\n{\n\"blocks\": [\n{\n\"type\": \"section\",\n" +
                "\"text\": {\n\"type\": \"mrkdwn\",\n\"text\": \"%s\"\n}\n},\n{\n\"type\": \"context\",\n\"elements\": [\n" +
                "{\n\"type\": \"mrkdwn\",\n\"text\": \"*Last updated:* %s\\n:copyright: %s\\n\"\n}\n]\n}\n]\n}\n]\n}";

        return String.format(output, channelID, text, formatDate, contents.getCopyright());
    }
}
