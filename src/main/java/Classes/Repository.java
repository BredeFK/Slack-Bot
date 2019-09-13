package Classes;

public class Repository {
    private int id;
    private String name;
    private String full_name;
    private String url;
    private String html_url;
    private String description;
    private String language;
    private Boolean fork;
    private String updated_at;
    private License license;
    private Repository source;

    public int getId() {
        return id;
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
        String _source = (fork) ? source.toString() : "null";
        String _licence = (license != null) ? license.toString() : "null";
        String _language = (language != null) ? language : "N/A";
        String desc = (description != null) ? description : "";
        return String.format("%nid: %d%nname: %s%nfull_name: %s%nurl: %s%nhtml_url: %s%ndescription: %s%nlanguage: %s%nfork: %s%nupdated_at: %s%nlicence: {%n %s %n}%nsource: {%n %s %n}%n%n",
                id,
                name,
                full_name,
                url,
                html_url,
                desc,
                _language,
                fork,
                updated_at,
                _licence,
                _source);
    }

    // Returns a string in json format to be sent as a message in slack
    public String toJson() {
        return "";
    }
}
