package alfred.services;

import alfred.models.dailyquote.DailyQuote;
import alfred.models.db.DBquote;
import alfred.repositories.DBquoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DailyQuoteService {
    @Autowired
    private DBquoteRepository dBquoteRepository;

    public List<DailyQuote> list() {
        // Get all DBquotes from db
        List<DBquote> quotes = dBquoteRepository.findAll();

        // Initialize DailyQuotes list
        List<DailyQuote> dailyQuotes = new ArrayList<>();

        // Convert from DBquote to DailyQuote object
        for (DBquote quote : quotes) {

            // Convert to DailyQuote
            DailyQuote dailyQuote = new DailyQuote(quote);

            // Add to list
            dailyQuotes.add(dailyQuote);
        }

        // Return converted DailyQuotes list
        return dailyQuotes;
    }

    public Long add(DailyQuote dailyQuote) {

        // Convert to DBquote
        DBquote dBquote = new DBquote(dailyQuote);

        // Add to db
        dBquoteRepository.save(dBquote);

        // return id
        return dBquote.getId();
    }
}
