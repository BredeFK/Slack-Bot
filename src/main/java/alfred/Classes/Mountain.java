package alfred.Classes;

public class Mountain {

    private String channelID;
    private boolean falt_ned; // fallen


    public boolean isFallen() {
        return this.falt_ned;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    @Override
    public String toString() {
        return String.format("%nfalt_ned: %s", falt_ned);
    }

    public String toJson(String mountainName) {
        String text = "";

        if (falt_ned) {
            text = String.format(":mountain:%s *has* fallen:exploding_head::tada:", mountainName);
        } else {
            text = String.format(":mountain:%s has *not* fallen:slightly_frowning_face:", mountainName);
        }

        // Template example in Templates.md
        return String.format("{\n\"channel\": \"%s\",\n\"attachments\": [\n" +
                        "{\n\"blocks\": [\n{\n\"type\": \"section\",\n\"text\": {\n" +
                        "\"type\": \"mrkdwn\",\n\"text\": \"%s\"\n}\n}\n]\n}\n]\n}",
                channelID, text);
    }
}
