package alfred.models.db;

import alfred.models.dailyquote.DailyQuote;
import alfred.models.dailyquote.Quote;
import alfred.models.general.Error;

import javax.persistence.*;

@Entity
public class DBquote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Error error;

    @OneToOne(cascade = CascadeType.ALL)
    private Quote quote;

    private String copyright;


    public DBquote(DailyQuote dailyQuote) throws Exception {
        this.error = dailyQuote.getError();
        this.quote = dailyQuote.getContents().getSingleQuote();
        this.copyright = dailyQuote.getContents().getCopyright();
    }

    public long getId() {
        return id;
    }

    public Quote getQuote() {
        return quote;
    }

    public String getCopyright() {
        return copyright;
    }

    public Error getError() {
        return error;
    }

    public DBquote() {

    }
}
