package alfred.models.dailyquote;

import java.util.ArrayList;

public class Content {

    private long id;

    private String copyright;

    private ArrayList<Quote> quotes;

    public Content() {
    }

    public String getCopyright() {
        return copyright;
    }

    public long getId() {
        return id;
    }

    public ArrayList<Quote> getQuotes() {
        return quotes;
    }

    public Quote getSingleQuote() throws Exception {
        if (!quotes.isEmpty())
            return quotes.get(0);

        throw new Exception("Error: Quotes array is empty");
    }

    @Override
    public String toString() {
        return String.format("%ncopyright: %s%nquotes: %s", copyright, quotes.get(0).toString());
    }
}
