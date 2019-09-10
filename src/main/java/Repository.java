public class Repository {
    private int id;
    private String name;
    private String html_url;
    private String description;
    private String language;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public String toString() {
        return "id: " + id + "\nname: " + name + "\nhtml_url: " + html_url + "\ndesc: " + description + "\nlanguage: " + language + "\n\n";
    }
}
