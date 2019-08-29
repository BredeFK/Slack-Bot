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
        StringBuilder output = new StringBuilder("\"copyright\": \"" + copyright + "\",\n");

        for (Quote quote : quotes) {
            output.append(quote.toString());
        }

        return output.toString();
    }
}
