package Classes;

public class Repository {
    private int id;
    private String name;
    private String url;
    private String description;
    private String language;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    @Override
    public String toString() {
        return String.format("%nid: %d%nname: %s%nurl: %s%ndescription: %s%nlanguage: %s%n%n",
                id, name, url, description, language);
    }
}
