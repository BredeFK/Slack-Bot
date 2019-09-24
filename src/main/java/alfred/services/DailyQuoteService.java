package alfred.services;

import alfred.models.DailyQuote;
import alfred.repositories.DailyQuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyQuoteService {

    @Autowired
    private DailyQuoteRepository dqRepository;

    public List<DailyQuote> list() {
        return dqRepository.findAll();
    }

    public Long add(DailyQuote dailyQuote) {
        dqRepository.save(dailyQuote);
        return dailyQuote.getId();
    }

}
