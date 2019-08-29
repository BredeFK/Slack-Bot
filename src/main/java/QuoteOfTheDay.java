public class QuoteOfTheDay {
    private boolean statusOK = false;
    private String quote;
    private String author;
    private String imageURL;
    private String category;
    private String date;
    private String copyRight;
    private String channelID;

    public QuoteOfTheDay() {

    }

    // For testing
    public QuoteOfTheDay(String channelID) {
        this.channelID = channelID;
        this.quote = "In spite of everything I shall rise again: I will take up my pencil, which I have forsaken in my great discouragement, and I will go on with my drawing.";
        this.author = "Vincent Van Gogh";
        this.imageURL = "https://theysaidso.com/img/bgs/man_on_the_mountain.jpg";
        this.category = "inspire";
        this.date = "2019-08-28";
        this.copyRight = "2017-19 theysaidso.com";
    }

    // Converts object to formatted json that can be sent to Slacks "Block Kit Builder" https://api.slack.com/tools/block-kit-builder
    public String toJson() {
        String text = "_" + quote + "_\\n\\n- *" + author + "*";

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
                "            \"image_url\": \"" + imageURL + "\",\n" +
                "            \"alt_text\": \"quote-" + category + "\"\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"type\": \"context\",\n" +
                "          \"elements\": [\n" +
                "            {\n" +
                "              \"type\": \"mrkdwn\",\n" +
                "              \"text\": \"*Last updated:* " + date + "\\n:copyright: " + copyRight + "\\n\"\n" +
                "            }\n" +
                "          ]\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }
}
