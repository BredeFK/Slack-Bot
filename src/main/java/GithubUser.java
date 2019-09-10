public class GithubUser {

    private String channelID;
    private String login;
    private int id;
    private String node_id;
    private String avatar_url;
    private String html_url;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String bio;
    private int public_repos;
    private int followers;
    private int following;
    private String message;

    public GithubUser() {
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String toJson() {
        String text = "";

        if (name != null) {
            text += "*" + name + "*\\n";
        }

        text += login + "\\n\\n\\n";

        if (bio != null) {
            text += "_" + bio + "_\\n";
        }

        if (company != null) {
            text += ":briefcase: " + company + "\\n";
        }

        if (location != null) {
            text += ":house: " + location + "\\n";
        }

        return "{\n" +
                "  \"channel\": \"" + channelID + "\",\n" +
                "  \"attachments\": [\n" +
                "    {\n" +
                "      \"blocks\": [\n" +
                "          {\n" +
                "            \"type\": \"section\",\n" +
                "            \"text\": {\n" +
                "              \"type\": \"mrkdwn\",\n" +
                "              \"text\": \"" + text + "\"\n" +
                "            },\n" +
                "            \"accessory\": {\n" +
                "              \"type\": \"image\",\n" +
                "              \"image_url\": \"" + avatar_url + "\",\n" +
                "              \"alt_text\": \"avatar-" + login + "\"\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"section\",\n" +
                "            \"text\": {\n" +
                "              \"type\": \"mrkdwn\",\n" +
                "              \"text\": \"Repositories\"\n" +
                "            },\n" +
                "            \"accessory\": {\n" +
                "              \"type\": \"static_select\",\n" +
                "              \"placeholder\": {\n" +
                "                \"type\": \"plain_text\",\n" +
                "                \"text\": \"Select a repository\",\n" +
                "                \"emoji\": true\n" +
                "              },\n" +
                "              \"options\": [\n" +
                "                {\n" +
                "                  \"text\": {\n" +
                "                    \"type\": \"plain_text\",\n" +
                "                    \"text\": \"Repository 1\",\n" +
                "                    \"emoji\": true\n" +
                "                  },\n" +
                "                  \"value\": \"value-0\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"text\": {\n" +
                "                    \"type\": \"plain_text\",\n" +
                "                    \"text\": \"Repository 2\",\n" +
                "                    \"emoji\": true\n" +
                "                  },\n" +
                "                  \"value\": \"value-1\"\n" +
                "                },\n" +
                "                {\n" +
                "                  \"text\": {\n" +
                "                    \"type\": \"plain_text\",\n" +
                "                    \"text\": \"Repository 3\",\n" +
                "                    \"emoji\": true\n" +
                "                  },\n" +
                "                  \"value\": \"value-2\"\n" +
                "                }\n" +
                "              ]\n" +
                "            }\n" +
                "          },\n" +
                "          {\n" +
                "            \"type\": \"context\",\n" +
                "            \"elements\": [\n" +
                "              {\n" +
                "                \"type\": \"mrkdwn\",\n" +
                "                \"text\": \"" + html_url + "\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
    }

}
