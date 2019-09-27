package alfred.models.dailyquote;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long dbID;

    private String quote;
    private String length;
    private String author;

    // Switched from ArrayList to List so it would work: https://discourse.hibernate.org/t/map-array-list-on-particular-column/610/2
    @ElementCollection
    private List<String> tags;

    private String category;
    private Date date;
    private String permalink;
    private String title;
    private String background;
    private String id;

    public Quote() {
    }

    public long getDbID() {
        return dbID;
    }

    public void setDbID(long dbID) {
        this.dbID = dbID;
    }

    public String getQuote() {
        return quote;
    }

    public int getLength() {
        return Integer.parseInt(length);
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() {
        return date;
    }

    public String getPermalink() {
        return permalink;
    }

    public String getTitle() {
        return title;
    }

    public String getBackground() {
        return background;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd.MM.yyyy");
        return String.format("%nquote: %s%nlength: %s%nauthor %s%ncategory: %s%ndate: %s%npermalink: %s%ntitle: %s%nbackground: %s%nid: %s",
                quote, length, author, category, fmt.format(date), permalink, title, background, id);
    }
}
