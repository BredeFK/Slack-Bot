package alfred.services;

import alfred.models.DBquote;
import alfred.repositories.DBquoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DBquoteService {

    @Autowired
    private DBquoteRepository dBquoteRepository;

    public List<DBquote> list() {
        return dBquoteRepository.findAll();
    }

    public Long add(DBquote dBquote) {
        dBquoteRepository.save(dBquote);
        return dBquote.getId();
    }

}