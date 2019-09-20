package alfred.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Quote {

    private String quote;
    private String length;
    private String author;
    private ArrayList<String> tags;
    private String category;
    private String date;
    private String permalink;
    private String title;
    private String background;
    private String id;

    public Quote() {
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

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getCategory() {
        return category;
    }

    public Date getDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(date);
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
        return String.format("%nquote: %s%nlength: %s%nauthor %s%ncategory: %s%ndate: %s%npermalink: %s%ntitle: %s%nbackground: %s%nid: %s",
                quote, length, author, category, date, permalink, title, background, id);
    }
}
