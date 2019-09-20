package alfred.Classes;

import java.util.ArrayList;

public class Content {
    private String copyright;
    private ArrayList<Quote> quotes;

    public Content() {
    }

    public String getCopyright() {
        return copyright;
    }

    public ArrayList<Quote> getQuotes() {
        return quotes;
    }

    @Override
    public String toString() {
        return String.format("%ncopyright: %s%nquotes: %s", copyright, quotes.get(0).toString());
    }
}
