public class QuoteOfTheDay {
    private boolean statusOK = false;
    private String quote;
    private String author;
    private String imageURL;
    private String category;
    private String date;
    private String copyRight;

    public QuoteOfTheDay() {

    }

    // For testing
    public QuoteOfTheDay(int i) {
        this.quote = "In spite of everything I shall rise again: I will take up my pencil, which I have forsaken in my great discouragement, and I will go on with my drawing.";
        this.author = "Vincent Van Gogh";
        this.imageURL = "https://theysaidso.com/img/bgs/man_on_the_mountain.jpg";
        this.category = "inspire" + i;
        this.date = "2019-08-28";
        this.copyRight = "2017-19 theysaidso.com";
    }

    // Converts object to formatted json that can be sent to Slacks "Block Kit Builder" https://api.slack.com/tools/block-kit-builder
    public String toJson() {
        String text = "_" + quote + "_\\n\\n- *" + author + "*";

        return "[\n" +
                "\t{\n" +
                "\t\t\"type\": \"section\",\n" +
                "\t\t\"text\": {\n" +
                "\t\t\t\"type\": \"mrkdwn\",\n" +
                "\t\t\t\"text\": \"" + text + "\"\n" +
                "\t\t},\n" +
                "\t\t\"accessory\": {\n" +
                "\t\t\t\"type\": \"image\",\n" +
                "\t\t\t\"image_url\": \"" + imageURL + "\",\n" +
                "\t\t\t\"alt_text\": \"quote-" + category + "\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"type\": \"context\",\n" +
                "\t\t\"elements\": [\n" +
                "\t\t\t{\n" +
                "\t\t\t\t\"type\": \"mrkdwn\",\n" +
                "\t\t\t\t\"text\": \"*Last updated:* " + date + "\\n:copyright: " + copyRight + "\\n\"" +
                "\t\t\t}\n" +
                "\t\t]\n" +
                "\t}\n" +
                "]";
    }
}
