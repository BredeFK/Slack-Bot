package alfred.Classes;

import java.util.Date;

public class Repository {
    private String channelID;
    private int id;
    private String name;
    private String full_name;
    private String url;
    private String html_url;
    private String description;
    private String language;
    private Boolean fork;
    private Date updated_at;
    private License license;
    private Repository source;

    public int getId() {
        return id;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public String getFull_name() {
        return full_name;
    }

    public Boolean getFork() {
        return fork;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public License getLicense() {
        return license;
    }

    // Return source-parent if repository is forked
    public Repository getSource() throws Exception {
        if (fork)
            return source;
        else
            throw new Exception("Repository is not forked and a source-parent doesn't exist");
    }

    @Override
    public String toString() {

        // Check for and dodge nullpointers
        String _source = (fork) ? source.toString() : "null";
        String _licence = (license != null) ? license.toString() : "null";
        String _language = (language != null) ? language : "N/A";
        String desc = (description != null) ? description : "";

        return String.format("%nid: %d%nname: %s%nfull_name: %s%nurl: %s%nhtml_url: %s%ndescription: %s%nlanguage: %s%nfork: %s%nupdated_at: %s%nlicence: {%n %s %n}%nsource: {%n %s %n}%n%n",
                id, name, full_name, url, html_url, desc, _language, fork, updated_at, _licence, _source);
    }

    // Returns a string in json format to be sent as a message in slack
    public String toJson() {

        StringBuilder text = new StringBuilder();

        text.append(String.format("`GitHub Repository %s`\\n*%s*\\n", full_name, name));

        if (fork) {
            text.append(String.format("_Forked from <%s|%s> _\\n", source.getHtml_url(), source.getFull_name()));
        }

        if (description != null) {
            text.append(String.format("```%s```\\n", description));
        }

        if (language != null) {
            text.append(String.format(":small_blue_diamond:%s\\t", language));
        }

        if (license != null) {
            text.append(String.format(":scales: %s\\t", license.getName()));
        }

        text.append(String.format("_Last updated on_ %ta %tb %td %tZ %tY", updated_at, updated_at, updated_at, updated_at, updated_at));

        // See Templates.md for better understanding of this json
        return String.format("{\n\"channel\": \"%s\",\n\"attachments\": [\n{\n\"blocks\": [\n{\n\"type\": \"section\",\n" +
                "\"text\": {\n\"type\": \"mrkdwn\",\n\"text\": \"%s\"\n}\n},\n{\n\"type\": \"context\",\n\"elements\": [\n" +
                "{\n\"type\": \"mrkdwn\",\n\"text\": \"%s\"\n}\n]\n}\n]\n}\n]\n}", channelID, text.toString(), html_url);

    }
}
