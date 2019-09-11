package Classes;

import java.util.ArrayList;

public class GithubUser {

    private String channelID;
    private String login;
    private int id;
    private String node_id;
    private String avatar_url;
    private String html_url;
    private String repos_url;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String bio;
    private int public_repos;
    private int followers;
    private int following;
    private String message;
    private ArrayList<Repository> repositories;

    public GithubUser() {
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getMessage() {
        return message;
    }

    public String getRepos_url() {
        return repos_url;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setRepositories(ArrayList<Repository> repositories) {
        this.repositories = repositories;
    }

    public ArrayList<Repository> getRepositories() {
        return repositories;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return String.format("%nchannelID: %s%nlogin: %s%nid: %d%nnode_id: %s%navatar_url: %s%nhtml_url: %s%nrepos_url: %s%nname: %s%ncompany: %s%nblog: %s%nlocation: %s%nbio: %s%npublic_repos: %d%nfollowers: %d%nfollowing: %d%nmessage: %s",
                channelID, login, id, node_id, avatar_url, html_url, repos_url, name, company, blog, location, bio, public_repos, followers, following, message);
    }

    public String toJson() {
        StringBuilder text = new StringBuilder();
        String output = "";

        text.append("`GitHub`\\n");

        if (name != null) {
            text.append(String.format("*%s*\\n", name));
        }

        text.append(String.format("%s\\n\\n\\n", login));

        if (bio != null) {
            text.append(String.format("```%s```\\n", bio));
        }

        if (company != null) {
            text.append(String.format(":briefcase: %s\\n", company));
        }

        if (location != null) {
            text.append(String.format(":house: %s\\n", location));
        }

        output = "{\n" +
                "  \"channel\": \"" + channelID + "\",\n" +
                "  \"attachments\": [\n" +
                "    {\n" +
                "      \"blocks\": [\n" +
                "          {\n" +
                "            \"type\": \"section\",\n" +
                "            \"text\": {\n" +
                "              \"type\": \"mrkdwn\",\n" +
                "              \"text\": \"" + text.toString() + "\"\n" +
                "            },\n" +
                "            \"accessory\": {\n" +
                "              \"type\": \"image\",\n" +
                "              \"image_url\": \"" + avatar_url + "\",\n" +
                "              \"alt_text\": \"avatar-" + login + "\"\n" +
                "            }\n" +
                "          },\n";

        // Only add repositories if they have any
        if (public_repos > 0) {
            output += "          {\n" +
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
                    "              \"options\": [\n";

            for (Repository repo : repositories) {
                String language = (repo.getLanguage() != null) ? " (" + repo.getLanguage() + ")" : " (N/A)";
                output += "                {\n" +
                        "                  \"text\": {\n" +
                        "                    \"type\": \"plain_text\",\n" +
                        "                    \"text\": \"" + repo.getName() + language + "\",\n" +
                        "                    \"emoji\": true\n" +
                        "                  },\n" +
                        "                  \"value\": \"" + repo.getUrl() + "\"\n" +
                        "                },\n";
            }


            output += "              ]\n" +
                    "            }\n" +
                    "          },\n";
        }
        output += "          {\n" +
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


        return output;
    }

}
