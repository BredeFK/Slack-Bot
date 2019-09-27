package alfred.models.dailyquote;

import alfred.models.db.DBquote;

import java.util.ArrayList;

public class Content {

    private String copyright;

    private ArrayList<Quote> quotes;

    public Content() {
    }

    public Content(DBquote dBquote) {
        this.copyright = dBquote.getCopyright();

        this.quotes = new ArrayList<>();
        this.quotes.add(dBquote.getQuote());
    }

    public String getCopyright() {
        return copyright;
    }

    public ArrayList<Quote> getQuotes() {
        return quotes;
    }

    public Quote getSingleQuote() {
        if (!quotes.isEmpty())
            return quotes.get(0);

        return new Quote();
    }

    @Override
    public String toString() {
        return String.format("%ncopyright: %s%nquotes: %s", copyright, quotes.get(0).toString());
    }
}
